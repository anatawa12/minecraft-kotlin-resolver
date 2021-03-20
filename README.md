# Minecraft Kotlin Resolver

The single file library to resolve kotlin version between two or more mods for launchwrapper.

# How to use

1. create coreMod for your mod.
2. Call `MCKTResolver.requestResolve()` from your coreMod's constructor
3. Configure manifest as shown below

You can see example configuration [here](#example-configuration)

# Manifest rule

### `MCKT-MF-Version`
The version of MCKT Manifest. must be `1`.

### `MCKT-KT-Version`
The version of Kotlin this mod will use and this mod embedding.

### `MCKT-KT-Parts`
The name of libraries this mod will use and this mod embedding.
Must be comma separated string with either `annotations`(means jetbrains annotations depended on from `kotlin-stdlib`), 
`kotlin-stdlib`, `kotlin-stdlib-jdk7`, `kotlin-stdlib-jdk8`, or `kotlin-reflect`.
For example, `annotations,kotlin-stdlib,kotlin-stdlib-jdk7`.

### `MCKT-KT-Jars`
The path from jar root to embedding kotlin jar.
If this exists, you must embed all parts of kotlin.
The order of jars must be same as `MCKT-KT-Parts`.
For example, `libs/annotations-13.0.jar,libs/kotlin-stdlib-1.4.30.jar,libs/kotlin-stdlib-jdk7-1.4.30.jar`

# Example configuration

<details>
<summary>build.gradle</summary>

```groovy
configurations {
    embedd
    implementation.extendsFrom(embedd)
}

dependencies {
    // version is automatically configured with kotlin plugin
    embedd "org.jetbrains.kotlin:kotlin-stdlib-jdk7"
}

jar {
    configurations.embedd.each {
        from (it) {
            into("libs")
        }
    }
    def kotlinVersion = project.plugins.findResult { 
        it instanceof org.jetbrains.kotlin.gradle.plugin.KotlinBasePluginWrapper 
                ? it.kotlinPluginVersion
                : null
    }
    def packageNameSuffix = /-[\d.]+\.jar$/
    manifest {
        attributes(
                "FMLCorePlugin": "<fully-qualified-name-of-core-mod-class>",
                "FMLCorePluginContainsFMLMod": "*",
                "MCKT-MF-Version": "1",
                "MCKT-KT-Version": kotlinVersion,
                "MCKT-KT-Parts": configurations.embedd.collect { packageNameSuffix.replace(it.name, "") }.join(","),
                "MCKT-KT-Jars": configurations.embedd.collect { "libs/${it.name}" }.join(",")
        )
    }
}
```

</details>

<details>
<summary>build.gradle.kts</summary>

```kotlin
val embedd by configurations.creating
configurations.implementation.get().extendsFrom(embedd)

dependencies {
    // version is automatically configured with kotlin plugin
    embedd(kotlin("kotlin-stdlib-jdk7"))
}

val jar by tasks.getting(Jar::class) {
    embedd.forEach { dep ->
        from(dep) {
            into("libs")
        }
    }

    val kotlinVersion = project.plugins
        .asSequence()
        .mapNotNull { (it as? org.jetbrains.kotlin.gradle.plugin.KotlinBasePluginWrapper)?.kotlinPluginVersion }
        .first()
    manifest {
        val packageNameSuffix = """-[\d.]+\.jar$""".toRegex()
        attributes(mapOf(
            "FMLCorePlugin" to "<fully-qualified-name-of-core-mod-class>",
            "FMLCorePluginContainsFMLMod" to "*",
            "MCKT-MF-Version" to "1",
            "MCKT-KT-Version" to kotlinVersion,
            "MCKT-KT-Parts" to embedd.joinToString(",") { packageNameSuffix.replace(it.name, "") },
            "MCKT-KT-Jars" to embedd.joinToString(",") { "libs/${it.name}" }
        ))
    }
}

```

</details>
