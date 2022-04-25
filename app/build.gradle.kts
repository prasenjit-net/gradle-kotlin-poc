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
    implementation("io.undertow:undertow-core:2.2.17.Final")
    implementation("io.undertow:undertow-servlet:2.2.17.Final")
    implementation("org.apache.commons:commons-lang3:3.12.0")
    implementation("commons-io:commons-io:2.11.0")
    implementation("org.apache.httpcomponents.client5:httpclient5:5.1.3")
//    implementation("org.apache.httpcomponents:httpcomponents-client:4.5.13")
}

