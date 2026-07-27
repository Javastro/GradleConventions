import org.eclipse.jgit.storage.file.FileRepositoryBuilder

plugins {
    id("java-library")
    id("maven-publish")
    id("signing")

}

java {
    withJavadocJar()
    withSourcesJar()
}




publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
            versionMapping {

                usage("java-api") {
                    fromResolutionResult()
                }
                usage("java-runtime") {
                    fromResolutionResult()
                }
            }
                pom {
                    licenses {
                        license {
                            name.set("GPL-3.0")
                            url.set("https://www.gnu.org/licenses/gpl-3.0.txt")
                        }
                    }
                    developers {
                        developer {
                            id.set("pahjbo")
                            name.set("Paul Harrison")
                            email.set("paul.harrison@manchester.ac.uk")
                        }
                        developer {
                            id.set("slloyd-src")
                            name.set("Stephen Lloyd")
                            email.set("stephen.lloyd@manchester.ac.uk")
                        }
                    }
                    scm {
                        connection.set("scm:git:git://github.com/javastro/ivoacore.git")
                        developerConnection.set("scm:git:ssh://github.com/javastro/ivoacore.git")
                        url.set("https://github.com/javastro/ivoacore")
                    }    
                    
                    
                    dependencies.artifactTypes.removeIf { d -> d.name == "bom" } 

// this removes from configurations - which we do not want as then it will not compile   https://stackoverflow.com/questions/68470193/exclude-dependency-from-pom-using-maven-publish-plugin
//                        setOf("apiElements", "runtimeElements")
//                            .flatMap { configName -> configurations[configName].hierarchy }
//                            .forEach { configuration ->
//                                configuration.dependencies.removeIf { dependency ->
//                                    dependency.name == "bom"
//                                }
//                            }

                }
            }
        }
    repositories {
        maven { //Only publish here whilst developing initial versions - ultimately want maven central.
            name = "uksrcrepo"
            credentials {
                username = (findProperty("uksrcNexusUsername") ?: System.getenv("UKSRC_REPO_USERNAME")) as String?
                password = (findProperty("uksrcNexusPassword") ?: System.getenv("UKSRC_REPO_PASSWORD")) as String?
            }
            val releasesRepoUrl = uri("https://repo.dev.uksrc.org/repository/maven-releases/")
            val snapshotsRepoUrl = uri("https://repo.dev.uksrc.org/repository/maven-snapshots/")
            url = uri(if (version.toString().endsWith("SNAPSHOT")) snapshotsRepoUrl else releasesRepoUrl)
            }
        }
    }

extra["isReleaseVersion"] = !version.toString().endsWith("SNAPSHOT")

signing {
    useGpgCmd()
    sign(publishing.publications["maven"])

}

tasks.withType<Sign>().configureEach {
    onlyIf("isReleaseVersion is set") { project.extra["isReleaseVersion"] as Boolean }
}


//TODO really want to get the git information automatically into POM - this is to experiment...
//https://github.com/ArcticLampyrid/gradle-git-version/blob/main/src/main/kotlin/com/github/arcticlampyrid/gradle/gitversion/GitVersionPlugin.kt
//
//https://github.com/palantir/gradle-git-version
tasks.register("printGitInfo") {
    doLast {
        var repo = FileRepositoryBuilder()
            .readEnvironment()
            .setGitDir(project.getProjectDir())
            .build();
        repo.fullBranch?.let { println("Git description: $it") }
        println("Project version: ${project.version}")
    }
}



//tasks.withType<GenerateModuleMetadata> {
//    // gradle does not like to publish the 'enforced-platform' and so warns against it
//    // not sure if it is harmful in our case - have changed to just platform to avoid the warning.
//    // perhaps look
//    // https://dev.to/mfvanek/creation-and-usage-of-bom-in-gradle-ca1
//    //suppressedValidationErrors.add("enforced-platform")
//}
