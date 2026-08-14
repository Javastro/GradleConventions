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
    implementation("io.quarkus:quarkus-micrometer-opentelemetry")
    implementation("io.quarkus:quarkus-kubernetes")
    implementation("io.quarkus:quarkus-container-image-docker")
    implementation("io.quarkiverse.helm:quarkus-helm:1.4.2")
    testImplementation("io.quarkus:quarkus-junit5")
    testImplementation("io.rest-assured:rest-assured")
    compileOnly("org.osgi:org.osgi.annotation.bundle:2.0.0") // adding this to get rid of some compile time OGSI warnings that are not relevant to quarkus

}

//as properties are the highest level of config setting only set if not already set (this would be from command line)
fun setSystemPropertyIfNot(name: String, value: String) {
    if (System.getProperty(name) == null) {
        System.setProperty(name, value)
    }
}

// https://quarkus.io/guides/gradle-tooling#configuring-quarkus-builds https://quarkus.io/guides/config-reference
// set some global quarkus properties here - note that the systemProperty() call
// recommended in first of above does not seem to work - so doing the more obvious direct java call
// note also that these are build time properties - run time stuff should still go into each application.properties.
tasks.withType<QuarkusTask>().configureEach {
    setSystemPropertyIfNot("quarkus.container-image.registry", "images.dev.uksrc.org")
    setSystemPropertyIfNot("quarkus.container-image.group", "javastro")
    setSystemPropertyIfNot("quarkus.container-image.username", System.getenv("UKSRC_HARBOR_USERNAME")) // pick up from environment in uniform way
    setSystemPropertyIfNot("quarkus.container-image.password", System.getenv("UKSRC_HARBOR_PASSWORD"))
    setSystemPropertyIfNot("quarkus.container-image.push", "false") // however do not push by default
    // setSystemPropertyIfNot("quarkus.kubernetes.ingress.ingress-class-name", "nginx") assume that the default class is already set...

    // swagger interface
    setSystemPropertyIfNot("quarkus.swagger-ui.always-include", "true")
    //trying to get schema types in the ui in the included jar...
    setSystemPropertyIfNot("quarkus.swagger-ui.deep-linking","true")
    setSystemPropertyIfNot("quarkus.swagger-ui.theme","flattop")
    setSystemPropertyIfNot("mp.openapi.extensions.smallrye.operationIdStrategy","CLASS_METHOD")
    setSystemPropertyIfNot("mp.openapi.extensions.smallrye.duplicateOperationIdBehavior","FAIL")


}

