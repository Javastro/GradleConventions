
// Targets the root project and all its sub-modules so that they all use the same Quarkus version.
project.rootProject.allprojects {
    extra["quarkusVersion"] = "3.33.4.1"

// note also needs  be set in the  gradle.properties file
}
