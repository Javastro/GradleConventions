// configure aspects of Java compilation.
plugins {
    id("java")
    id("org.gradlex.reproducible-builds")
}

java {
    toolchain.languageVersion = JavaLanguageVersion.of(17)
}

tasks.withType<JavaCompile>().configureEach {
    options.compilerArgs.add("-Xlint:all,-serial")
}

tasks.withType<Javadoc>().configureEach {
    options {
        this as StandardJavadocDocletOptions
        addStringOption("Xdoclint:all,-missing", "-Xwerror")
    }
}

sourceSets.all {
    // 'assemble' compiles all sources, including all test sources
    tasks.assemble { dependsOn(tasks.named(classesTaskName)) }
}
