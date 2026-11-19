// Connect all root project tasks to corresponding subproject lifecycle tasks.
// This allows to build a complete project in a composite setup.
plugins {
    id("base")
    id("pl.allegro.tech.build.axion-release")
}

// Convenience for local development: when running './gradlew' without parameters, show the tasks...
defaultTasks("tasks")
if (gradle.startParameter.taskNames.isEmpty()) {
    // ...of group 'build' only
    tasks.withType<TaskReportTask>().configureEach { displayGroup = "build" }
}

if (!subprojects.isEmpty()) {
    tasks.assemble {
        dependsOn(subprojects.map { ":${it.name}:assemble" })
    }
    tasks.check {
        dependsOn(subprojects.map { ":${it.name}:check" })
    }
    tasks.build {
        dependsOn(subprojects.map { ":${it.name}:build" })
    }
    tasks.clean {
        dependsOn(subprojects.map { ":${it.name}:clean" })
    }
    tasks.register("publish") {
        dependsOn(subprojects.map { ":${it.name}:publish" })
    }
    tasks.register("publishSnapshots") {
        group = "publishing"
        description = "Publishes only the subprojects that are at snapshot versions"

        subprojects {
            val subproj = this
            if(subproj.version.toString().endsWith("SNAPSHOT")) {
                logger.lifecycle("Subproject [${subproj.name}] is a SNAPSHOT version. Registering for release.")
                dependsOn(":${subproj.name}:publish")

            } else {
                logger.lifecycle("Subproject [${subproj.name}] is unchanged since release. Skipping.")
            }

        }
    }

    val publishnew = tasks.register("publishnew") {
        group = "publishing"
        description = "Publishes subproject artifacts only when their version is newer than the destination repository."
    }

    subprojects {
        plugins.withId("maven-publish") {
            tasks.withType<PublishToMavenRepository>().all {
                val publishTask = this
                publishnew.configure {
                    dependsOn(publishTask)
                }
                onlyIf("the publication is newer than the destination repository when using publishnew") {
                    if (!gradle.taskGraph.hasTask(publishnew.get())) {
                        true
                    } else {
                        val publication = publishTask.publication
                        val repository = publishTask.repository
                        val credentials = repository.credentials
                        val newer = PublishedVersions.isNewer(
                            repository.url,
                            publication.groupId,
                            publication.artifactId,
                            publication.version,
                            credentials.username,
                            credentials.password
                        )
                        logger.lifecycle(
                            "${if (newer) "Publishing" else "Skipping"} " +
                                    "${publication.groupId}:${publication.artifactId}:${publication.version} " +
                                    "in ${repository.name}"
                        )
                        newer
                    }
                }
            }
        }
    }

}
