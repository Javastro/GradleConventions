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

## Quarkus Applications

Quarkus is described as being "kubernetes-ready" and consequently the conventions strive to use the quarkus [kubernetes](https://quarkus.io/guides/deploying-to-kubernetes) and [helm](https://docs.quarkiverse.io/quarkus-helm/dev/index.html) extensions to create a single source of truth with the source code for the application. The helm approach to system properties is described [here](https://docs.quarkiverse.io/quarkus-helm/dev/index.html#mapping-system-properties) and one approach that works well is to use [configMaps](https://quarkus.io/guides/kubernetes-config) natively to map to the exact properties within the `application.properties` - To do this the config maps should be defined in `src/main/kubernetes/kubernetes.yml`

The gradle conventions plugin will in addition copy any `yaml` files in `src/main/helm/templates` to the `build/helm/templates` directory so that they can be used in the helm chart. The `helm` plugin will then package these into the helm chart.


## Inspiration

This repository uses many ideas from https://github.com/jjohannes/idiomatic-gradle

