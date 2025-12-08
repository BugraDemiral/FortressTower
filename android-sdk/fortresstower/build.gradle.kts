plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("maven-publish")
    id("signing")
}

android {
    namespace = "com.monomobile.fortresstower"
    compileSdk = 35

    defaultConfig {
        minSdk = 23
        targetSdk = 35

        consumerProguardFiles("consumer-rules.pro")

        externalNativeBuild {
            cmake {
                cppFlags += "-std=c++17"
            }
        }

        ndk {
            abiFilters += listOf("armeabi-v7a", "arm64-v8a", "x86", "x86_64")
        }
    }

    externalNativeBuild {
        cmake {
            path = file("src/main/cpp/CMakeLists.txt")
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "consumer-rules.pro"
            )
        }
        debug {
            isMinifyEnabled = false
        }
    }

    buildFeatures {
        buildConfig = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.13.1")

    testImplementation("junit:junit:4.13.2")
    testImplementation("org.mockito:mockito-core:5.12.0")
}

group = project.findProperty("PUBLISH_GROUP_ID") as String? ?: "dev.monomobile.fortresstower"
version = project.findProperty("PUBLISH_VERSION") as String? ?: "1.0.0-SNAPSHOT"

afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("release") {
                from(components["release"])

                groupId = project.findProperty("PUBLISH_GROUP_ID") as String
                artifactId = project.findProperty("PUBLISH_ARTIFACT_ID") as String
                version = project.findProperty("PUBLISH_VERSION") as String
            }
        }
    }

    signing {
        val pub = publishing.publications["release"]
        sign(pub)
    }
}