plugins {
    kotlin("jvm") version "1.6.20"
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(11))
}

tasks.test {
    useJUnitPlatform()
}
