
plugins {
    id("java-library")
    id("signing")
    id("org.javastro.build.base.versioning")
    id("org.javastro.build.base.quarkus-version")
    id("org.javastro.build.base.dependency-rules")
    id("org.javastro.build.feature.compilation")
    id("org.javastro.build.feature.unit-testing")
    id("org.javastro.build.feature.lib-publishing")
}

val quarkusVersion:String by project.extra

dependencies {
    implementation(platform("io.quarkus:quarkus-bom:$quarkusVersion"))

}


