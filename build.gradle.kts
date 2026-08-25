import pl.allegro.tech.build.axion.release.domain.hooks.HookContext

plugins {
    `kotlin-dsl`
    `maven-publish`
    id("pl.allegro.tech.build.axion-release") version "1.21.3"
}

group = "org.javastro.gradle"

scmVersion {
    hooks {
        pre(
            "fileUpdate", mapOf(
                "encoding" to "utf-8",
                "file" to rootDir.resolve("README.md").toString(),
                "pattern" to KotlinClosure2<String, HookContext, String>({ v, c -> "/version *\"${v}\"/" }),
                "replacement" to KotlinClosure2<String, HookContext, String>({ v, c -> "version \"${v}\"" })
            )
        )

    }
}
version = scmVersion.version

val quarkusVersion: String by project

dependencies {
    // examples of using a Gradle community plugin
    implementation("org.gradlex:jvm-dependency-conflict-resolution:2.5")
    implementation("org.gradlex:reproducible-builds:1.1")
    implementation("org.eclipse.jgit:org.eclipse.jgit:7.5.0.202512021534-r")
    implementation("org.kordamp.gradle.jandex:org.kordamp.gradle.jandex.gradle.plugin:2.3.0")
    implementation("io.quarkus:io.quarkus.gradle.plugin:$quarkusVersion")
    implementation("pl.allegro.tech.build.axion-release:pl.allegro.tech.build.axion-release.gradle.plugin:1.21.3") // solid for tag based versioning
    implementation("com.github.jmongard.git-semver-plugin:com.github.jmongard.git-semver-plugin.gradle.plugin:0.19.5") // versions from tags - not quite ready for multi version monorepos

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

