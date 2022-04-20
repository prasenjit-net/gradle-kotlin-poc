plugins {
    id("application")
    kotlin("jvm") version "1.6.20"
}

application {
    mainClass.set("net.prasenjit.poc.gradle.app.Main")
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(11))
}

tasks.test {
    useJUnitPlatform()
}

dependencies {
    implementation(project(":logic"))
}
