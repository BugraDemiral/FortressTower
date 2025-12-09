# 🏰 FortressTower Monorepo

Defense-grade mobile integrity SDK and supporting tooling.

This monorepo contains:

- `android-sdk/` – Android SDK (library + sample app)
- `tools/ft-cert-hash/` – CLI helper to derive signing certificate SHA-256 hashes

# 📦 Installation (via JitPack)

FortressTower is distributed through **JitPack**.

## 1️⃣ Add JitPack to `settings.gradle.kts`

```kotlin
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
}
```

## 2️⃣ Add the FortressTower dependency

```kotlin
dependencies {
    implementation("com.github.BugraDemiral:FortressTower:v0.1.5")
}
```

## 3️⃣ Initialize FortressTower

```kotlin
class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()

        FortressTower.init(
            context = this,
            config = FortressConfig(
                licenseKey = "FT-DEV-KEY",
                environment = FortressEnvironment.Debug,
                expectedSignatureSha256 = null
            )
        )
    }
}
```

## 4️⃣ Using FortressTower

```kotlin
val trust = FortressTower.currentTrustScore()

if (trust.level == TrustLevel.BLOCK) {
    // Block or restrict the operation
}
```
