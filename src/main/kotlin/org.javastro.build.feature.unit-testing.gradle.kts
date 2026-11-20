// configure aspects of Unit Testing
plugins {
    id("java")
    id("jacoco")
}


tasks.named<Test>("test") {
    useJUnitPlatform()
}

