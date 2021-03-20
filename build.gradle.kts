// SPDX-License-Identifier: MIT
// Copyright (c) 2021 anatawa12

plugins {
    java
}

group = "org.example"
version = "1.0-SNAPSHOT"
val kotlinVersion = "1.4.30"

tasks.compileJava {
    targetCompatibility = "${JavaVersion.VERSION_1_6}"
    sourceCompatibility = "${JavaVersion.VERSION_1_6}"
}

val kotlinStdlib by configurations.creating

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

    kotlinStdlib(kotlin("stdlib-jdk7", kotlinVersion))
}

val manifestJar by tasks.creating(Jar::class) {
    archiveBaseName.set("manifest-jar")
    archiveVersion.set("")

    kotlinStdlib.forEach { dep ->
        from(dep) {
            into("libs")
        }
    }

    manifest {
        val packageNameSuffix = """-[\d.]+\.jar$""".toRegex()
        attributes(mapOf(
            "MCKT-MF-Version" to "1",
            "MCKT-KT-Version" to kotlinVersion,
            "MCKT-KT-Parts" to kotlinStdlib.joinToString(",") { packageNameSuffix.replace(it.name, "") },
            "MCKT-KT-Jars" to kotlinStdlib.joinToString(",") { "libs/${it.name}" }
        ))
    }
}

val kotlinStdlibJar by tasks.creating(Zip::class) {
    archiveBaseName.set("kotlin-stdlib")
    archiveVersion.set("")
    archiveExtension.set("jar")
    destinationDirectory.set(buildDir.resolve("libs"))

    kotlinStdlib.forEach { dep ->
        from(zipTree(dep))
    }
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
    dependsOn(manifestJar)
    dependsOn(kotlinStdlibJar)
}
