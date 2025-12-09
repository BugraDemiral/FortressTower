import org.gradle.api.publish.maven.MavenPublication

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("maven-publish")
}

android {
    namespace = "com.monomobile.fortresstower"
    compileSdk = 36

    defaultConfig {
        minSdk = 23

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

    publishing {
        singleVariant("release") {
            withSourcesJar()
            // withJavadocJar() // optional, add later
        }
    }

}

dependencies {
    implementation("androidx.core:core-ktx:1.17.0")

    testImplementation("junit:junit:4.13.2")
    testImplementation("org.mockito:mockito-core:5.20.0")
}

afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("release") {
                from(components["release"])

                groupId = project.group.toString()
                artifactId = "FortressTower"
                version = project.version.toString()
            }
        }
    }
}