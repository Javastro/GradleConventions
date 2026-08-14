plugins {
    `kotlin-dsl`
    `maven-publish`
}

group = "org.javastro.gradle"
version = "0.1"

val quarkusVersion: String by project

dependencies {
    // examples of using a Gradle community plugin
    implementation("org.gradlex:jvm-dependency-conflict-resolution:2.5")
    implementation("org.gradlex:reproducible-builds:1.1")
    implementation("org.eclipse.jgit:org.eclipse.jgit:7.5.0.202512021534-r")
    implementation("org.kordamp.gradle.jandex:org.kordamp.gradle.jandex.gradle.plugin:2.3.0")
    implementation("io.quarkus:io.quarkus.gradle.plugin:$quarkusVersion")
//    implementation("io.mvnpm.gradle.plugin:native-java-plugin:1.0.0") needs Java 21
}

publishing.repositories.maven {
    name = "uksrcrepo"
    credentials {
        username = (findProperty("uksrcNexusUsername") ?: System.getenv("UKSRC_REPO_USERNAME")) as String?
        password = (findProperty("uksrcNexusPassword") ?: System.getenv("UKSRC_REPO_PASSWORD")) as String?
    }
    url = uri("https://repo.dev.uksrc.org/repository/maven-releases/")
}

