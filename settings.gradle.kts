pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.PREFER_SETTINGS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "FortressTower"

// Point modules to the android-sdk subfolder
include(":fortresstower", ":sample-app")
project(":fortresstower").projectDir = file("android-sdk/fortresstower")
project(":sample-app").projectDir = file("android-sdk/sample-app")