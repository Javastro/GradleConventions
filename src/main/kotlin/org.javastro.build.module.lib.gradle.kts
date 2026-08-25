plugins {
    id("java-library")
    id("signing")
    id("org.javastro.build.base.versioning-axion")
    id("org.javastro.build.base.dependency-rules")
    id("org.javastro.build.feature.compilation")
    id("org.javastro.build.feature.unit-testing")
    id("org.javastro.build.feature.lib-publishing")
}




tasks.withType<Javadoc> {
    (options as StandardJavadocDocletOptions).tags("TODO:a:\"To Do:\".")
}

