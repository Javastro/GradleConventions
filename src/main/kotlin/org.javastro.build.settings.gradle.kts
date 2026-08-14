import org.gradle.kotlin.dsl.maven

dependencyResolutionManagement {
    repositories {
        mavenCentral()
        maven("https://repo.dev.uksrc.org/repository/maven-public/")

    }

    // https://docs.gradle.org/current/userguide/composite_builds.html#included_build_declaring_substitutions
    includeBuild(".")


}

// all directories with a 'build.gradle.kts' are modules (subprojects) of the build
rootDir.listFiles()?.filter { folder -> folder.isDirectory && folder.name != "buildSrc" && File(folder, "build.gradle.kts").exists() }?.forEach {
    include(it.name)
}
