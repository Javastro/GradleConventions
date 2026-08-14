Gradle Conventions
==================

This project defines the settings for Gradle conventions used in the Javastro organisation. It includes configurations for build scripts, dependencies, and other settings that ensure consistency across the project.




It seems that the gradle `buildSrc` mechanism cannot be used with these plugins - for a monorepo with lots of subprojects then the best way to set things up is something like

```kotlin
plugins {
    id("org.javastro.build.feature.lifecycle.root")  // automates adding the projects
}

subprojects {
    beforeEvaluate {
        group = "org.javastro.ivoa.core.quarkus"
    }
}
```
in the top level `build.gradle.kts`


## Versioning

the conventions plugins allow a `version.txt` file to be read if it exists and the version has not already been set in the `build.gradle.kts` file. In a multi project build, the `version.txt` file may be placed in the top level to keep versions in-line, although any individual subproject may override the version by setting it in its own version.

It gets many ideas from https://github.com/jjohannes/idiomatic-gradle