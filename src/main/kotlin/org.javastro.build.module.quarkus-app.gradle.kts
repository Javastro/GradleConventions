import io.quarkus.gradle.tasks.QuarkusTask

plugins {
    java
    id("io.quarkus")
    id("org.javastro.build.base.versioning")
    id("org.javastro.build.base.dependency-rules")
    id("org.javastro.build.feature.compilation")
    id("org.javastro.build.feature.unit-testing")
}

val quarkusVersion = "3.33.2"

dependencies {
    implementation(enforcedPlatform("io.quarkus:quarkus-bom:$quarkusVersion"))
    implementation("io.quarkus:quarkus-rest")
    implementation("io.quarkus:quarkus-rest-jackson")
    implementation("io.quarkus:quarkus-smallrye-openapi")
    implementation("io.quarkus:quarkus-kubernetes")
    //implementation("io.quarkus:quarkus-micrometer-registry-prometheus") TODO add in correct monitoring dependencies
    implementation("io.quarkus:quarkus-container-image-docker")
    implementation("io.quarkiverse.helm:quarkus-helm:1.4.2")
    testImplementation("io.quarkus:quarkus-junit5")
    testImplementation("io.rest-assured:rest-assured")
    compileOnly("org.osgi:org.osgi.annotation.bundle:2.0.0") // adding this to get rid of some compile time OGSI warnings that are not relevant to quarkus

}

// https://quarkus.io/guides/gradle-tooling#configuring-quarkus-builds https://quarkus.io/guides/config-reference
// set some global quarkus properties here - note that the systemProperty() call
// recommended in first of above does not seem to work - so doing the more obvious direct java call
// note also that these are build time properties - run time stuff should still go into each application.properties.
tasks.withType<QuarkusTask>().configureEach {
    System.setProperty("quarkus.container-image.registry","images.dev.uksrc.org")
    System.setProperty("quarkus.container-image.group", "javastro")
    System.setProperty("quarkus.container-image.build", "true")
    System.setProperty("quarkus.container-image.username", System.getenv("UKSRC_HARBOR_USERNAME")) // pick up from environment in uniform way
    System.setProperty("quarkus.container-image.password", System.getenv("UKSRC_HARBOR_PASSWORD"))
    System.setProperty("quarkus.container-image.push", "false") // however do not push by default
    System.setProperty("quarkus.kubernetes.ingress.ingress-class-name","nginx")
    System.setProperty("quarkus.kubernetes.deploy","false")
}