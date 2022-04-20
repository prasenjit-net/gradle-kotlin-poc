plugins {
    id("java-library")
    kotlin("jvm") version "1.6.20"
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(11))
}

tasks.test {
    useJUnitPlatform()
}

dependencies {
    api(project(":model"))
    api("org.apache.commons:commons-lang3:3.12.0")
}
