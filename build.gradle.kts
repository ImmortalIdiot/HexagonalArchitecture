plugins {
    kotlin("jvm") version "2.0.10"
}

group = "com.immortalidiot"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}