Gradle Conventions
==================

This project defines the settings for Gradle conventions used in the Javastro organisation. It includes configurations for build scripts, dependencies, and other settings that ensure consistency across the project.

To use these conventions then in the top level `settings.gradle.kts` file of the project you should include

```kotlin
plugins {
    id("org.javastro.build") version "0.2.3"
}
```


## Library Projects


 For a monorepo with lots of subprojects then the best way to set things up is 

```kotlin
plugins {
    id("org.javastro.build.feature.lifecycle.root-monorepo")  // automates adding the projects
}

subprojects {
    beforeEvaluate {
        group = "org.javastro.ivoa.core.quarkus" // set the group the same for all the subprojects
    }
}
```
in the top level `build.gradle.kts`

note that the default is to name subprojects after the directory name - for various automations to work properly these names should be unique within the monorepo.

## Quarkus Applications

```kotlin
plugins {
    id("org.javastro.build.module.quarkus-app")
}
```

Quarkus is described as being "kubernetes-ready" and consequently the conventions strive to use the quarkus [kubernetes](https://quarkus.io/guides/deploying-to-kubernetes) and [helm](https://docs.quarkiverse.io/quarkus-helm/dev/index.html) extensions to create a single source of truth with the source code for the application. The helm approach to system properties is described [here](https://docs.quarkiverse.io/quarkus-helm/dev/index.html#mapping-system-properties) and one approach that works well is to use [configMaps](https://quarkus.io/guides/kubernetes-config) natively to map to the exact properties within the `application.properties` - To do this the config maps should be defined in `src/main/kubernetes/kubernetes.yml`

The gradle conventions plugin will in addition copy any `yaml` files in `src/main/helm/templates` to the `build/helm/templates` directory so that they can be used in the helm chart. The `helm` plugin will then package these into the helm chart.

* to build an image (without testing - should only be done to save time in CD if previous test build run has succeded!)

```shell
./gradlew  build -x test -Dquarkus.container-image.build=true
```

in addition to push to the default [OCI repository](https://images.dev.uksrc.org/harbor/projects/10/repositories) for which the credentials have already been set up in the Javastro project.


```shell
./gradlew  build -x test -Dquarkus.container-image.build=true -Dquarkus.container-image.push=true
```


## Versioning

### libraries
For monorepo multi-module library projects the versioning is managed with the [axion release plugin](https://axion-release-plugin.readthedocs.io/en/latest/). You should consult the documentation for full usage instructions but

```shell
./gradlew currentVersion
```

will show the current (computed) version for each component.

```shell
./gradlew release -Prelease.dryRun
```
will show what will happen if you try to release

### Quarkus Apps

Quarkus applications are versioned using the [semver plugin](https://github.com/jmongard/Git.SemVersioning.Gradle) - which computes versions based on looking at the [conventional commit labels](https://www.conventionalcommits.org/).

```kotlin
./gradlew -q   printVersion --rerun
```

will print the current (calculated) version (note the --rerun as this plugin does not disable caching of this task).


## Inspiration

This repository uses many ideas from https://github.com/jjohannes/idiomatic-gradle

