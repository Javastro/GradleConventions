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
    implementation("io.quarkiverse.helm:quarkus-helm:1.2.3")
    testImplementation("io.quarkus:quarkus-junit5")
    testImplementation("io.rest-assured:rest-assured")
    compileOnly("org.osgi:org.osgi.annotation.bundle:2.0.0") // adding this to get rid of some compile time OGSI warnings that are not relevant to quarkus

}