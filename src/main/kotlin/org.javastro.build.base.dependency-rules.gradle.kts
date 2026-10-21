plugins {
    id("java")
}

repositories {
    mavenCentral()
    mavenLocal()
    maven {
        url= uri("https://central.sonatype.com/repository/maven-snapshots/")
    }

    maven {
        url= uri("https://repo.dev.uksrc.org/repository/maven-public/")
    }

}

//probably nice to do something more sophisticated like https://github.com/jjohannes/idiomatic-gradle/blob/main/gradle-conventions/src/main/kotlin/com.example.idiomatic.gradle.build.base.dependency-rules.gradle.kts
dependencies {
    implementation(platform("org.javastro:bom:2026.3"))
    implementation("org.slf4j:slf4j-api")
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testRuntimeOnly("org.slf4j:slf4j-simple")
}
