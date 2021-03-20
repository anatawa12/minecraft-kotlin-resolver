// SPDX-License-Identifier: MIT
// Copyright (c) 2021 anatawa12

plugins {
    java
}

group = "org.example"
version = "1.0-SNAPSHOT"

tasks.compileJava {
    targetCompatibility = "${JavaVersion.VERSION_1_6}"
    sourceCompatibility = "${JavaVersion.VERSION_1_6}"
}

repositories {
    maven(url = "https://libraries.minecraft.net/")
    maven(url = "https://files.minecraftforge.net/maven/")
    mavenCentral()
}

dependencies {
    implementation("net.minecraft:launchwrapper:1.11")
    implementation("net.minecraftforge:forge:1.12.2-14.23.5.2847:universal")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}
