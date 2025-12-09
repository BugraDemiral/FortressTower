plugins {
    id("com.android.library") version "8.13.1" apply false
    id("com.android.application") version "8.13.1" apply false
    id("org.jetbrains.kotlin.android") version "2.0.0" apply false
    id("org.jetbrains.kotlin.jvm") version "2.0.0" apply false
    id("org.jetbrains.kotlin.plugin.compose") version "2.0.0" apply false
}

allprojects {
    // JitPack passes -Pgroup and -Pversion
    group = (findProperty("group") as String?) ?: "com.github.BugraDemiral"
    version = (findProperty("version") as String?) ?: "0.0.1-SNAPSHOT"
}

tasks.register<Delete>("clean") {
    delete(rootProject.layout.buildDirectory)
}