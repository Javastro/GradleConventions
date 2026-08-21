import io.quarkus.gradle.tasks.QuarkusTask

plugins {
    java
    id("io.quarkus")
    id("org.javastro.build.base.versioning")
    id("org.javastro.build.base.quarkus-version")
    id("org.javastro.build.base.dependency-rules")
    id("org.javastro.build.feature.compilation")
    id("org.javastro.build.feature.unit-testing")
}

val quarkusVersion:String by project.extra


dependencies {
    implementation(enforcedPlatform("io.quarkus:quarkus-bom:$quarkusVersion"))
    implementation("io.quarkus:quarkus-rest")
    implementation("io.quarkus:quarkus-rest-jackson")
    implementation("io.quarkus:quarkus-smallrye-openapi")
    implementation("io.quarkus:quarkus-smallrye-health")
    implementation("io.quarkus:quarkus-opentelemetry") // TODO this needs further config
    implementation("io.quarkus:quarkus-kubernetes")
    implementation("io.quarkus:quarkus-container-image-docker")
    implementation("io.quarkiverse.helm:quarkus-helm:1.4.2")
    implementation("org.javastro.ivoa.core.quarkus:quarkus-config:0.1.0-SNAPSHOT")
    testImplementation("io.quarkus:quarkus-junit5")
    testImplementation("io.rest-assured:rest-assured")
    compileOnly("org.osgi:org.osgi.annotation.bundle:2.0.0") // adding this to get rid of some compile time OGSI warnings that are not relevant to quarkus

}

//as properties are the highest level of config setting only set if not already set (this would be from command line)
fun setSystemPropertyIfNot(name: String, value: String?) {
    if (System.getProperty(name) == null && value != null) {
        System.setProperty(name, value)
    }
}

// https://quarkus.io/guides/gradle-tooling#configuring-quarkus-builds https://quarkus.io/guides/config-reference
// set some global quarkus properties here - note that the systemProperty() call
// recommended in first of above does not seem to work - so doing the more obvious direct java call
// note also that these are build time properties - run time stuff should still go into each application.properties.
tasks.withType<QuarkusTask>().configureEach {
    // pick up credentials from environment in uniform way
    val harborUsername = (findProperty("uksrcHarborUsername") ?: System.getenv("UKSRC_HARBOR_USERNAME")) as String?
    val harborPassword = (findProperty("uksrcHarborPassword") ?: System.getenv("UKSRC_HARBOR_PASSWORD") ) as String?
    setSystemPropertyIfNot("quarkus.docker.buildx.platform", "linux/amd64,linux/arm64")
    setSystemPropertyIfNot("quarkus.container-image.registry", "images.dev.uksrc.org")
    setSystemPropertyIfNot("quarkus.container-image.group", "javastro")
    setSystemPropertyIfNot("quarkus.container-image.username", harborUsername)
    setSystemPropertyIfNot("quarkus.container-image.password", harborPassword)
    setSystemPropertyIfNot("quarkus.container-image.push", "false") // however do not push by default
    // setSystemPropertyIfNot("quarkus.kubernetes.ingress.ingress-class-name", "nginx") assume that the default class is already set...

    // swagger interface
    setSystemPropertyIfNot("quarkus.swagger-ui.always-include", "true")
    //trying to get schema types in the ui in the included jar...
    setSystemPropertyIfNot("quarkus.swagger-ui.deep-linking", "true")
    setSystemPropertyIfNot("quarkus.swagger-ui.theme", "flattop")
    setSystemPropertyIfNot("mp.openapi.extensions.smallrye.operationIdStrategy", "CLASS_METHOD")
    setSystemPropertyIfNot("mp.openapi.extensions.smallrye.duplicateOperationIdBehavior", "FAIL")

    // ingress
    setSystemPropertyIfNot("%prod.quarkus.kubernetes.ingress.expose", "true")
    setSystemPropertyIfNot("quarkus.kubernetes.image-pull-policy", "IfNotPresent") // done so that k3d image command will be effective...

    // helm - Note that because of non-support of OCI - the helm commandline needs to be used instead for pushing.
    setSystemPropertyIfNot("quarkus.helm.repository.url", "oci://images.dev.uksrc.org/helm")
    setSystemPropertyIfNot("quarkus.helm.repository.type", "OCI") //FIXME -the helm extension cannot actually do this https://github.com/quarkiverse/quarkus-helm/issues/371
    setSystemPropertyIfNot("quarkus.helm.repository.username", harborUsername)
    setSystemPropertyIfNot("quarkus.helm.repository.password", harborPassword)
    setSystemPropertyIfNot("quarkus.helm.repository.push", "false") // however do not push by default

}


// helm plugin will only copy _ files from src/main/helm - want to copy any other yaml and possibly merge some custom values....
tasks.register<Copy>("prepareHelmChart") {
    dependsOn("quarkusBuild")

    // Copy templates into generated Helm chart
    val chartDir = layout.buildDirectory.dir("helm/kubernetes/${project.name.lowercase()}")

    from(layout.projectDirectory.dir("src/main/helm/templates"))
    include("*.yaml")
    exclude("values-extra.yaml") // Exclude values.yaml to avoid overwriting the generated one
    into(chartDir.map { it.dir("templates") })
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE

    // Merge the values in values-extra.yaml into the generated Helm chart's values.yaml
    doLast {
        val generated = chartDir.get().file("values.yaml").asFile
        val extra = layout.projectDirectory
            .file("src/main/kubernetes/helm/values-extra.yaml")
            .asFile

        if (extra.exists()) {
            generated.appendText(
                "\n\n# ---- Project additions ----\n${extra.readText()}"
            )
        }
    }
}

tasks.named("build") {
    dependsOn("prepareHelmChart")
}