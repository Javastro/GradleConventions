// Versions managed https://github.com/jmongard/Git.SemVersioning.Gradle
plugins {
    id("com.github.jmongard.git-semver-plugin")
}
semver {
   releaseTagNameFormat = "v%s"
}
//Set the version for the current project:
version = semver.version
