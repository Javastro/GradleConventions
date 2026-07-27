// configure aspects of Unit Testing
plugins {
    id("java")
}


tasks.named<Test>("test") {
    useJUnitPlatform()
}

testing {
    suites {
        register<JvmTestSuite>("integrationTest") {
            dependencies {
                implementation(project())
                runtimeOnly("org.slf4j:slf4j-simple")
            }
        }
    }
}
// This is needed because jvm-test-suites does not extend the testIntegration configuration from
// the base configuration, even when integrationTest is defined with "dependencies { implementation project() }"
configurations.named("integrationTestImplementation").configure {
    extendsFrom(configurations.getByName(JavaPlugin.IMPLEMENTATION_CONFIGURATION_NAME))
}
