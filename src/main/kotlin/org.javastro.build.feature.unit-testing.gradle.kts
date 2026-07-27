// configure aspects of Unit Testing
plugins {
    id("java")
}


tasks.named<Test>("test") {
    useJUnitPlatform()
}

