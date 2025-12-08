package com.monomobile.fortresstower.integrity

import android.content.Context
import android.os.Build
import android.os.Debug
import com.monomobile.fortresstower.*
import com.monomobile.fortresstower.native.NativeChecks
import java.io.File

internal class IntegrityEvaluator(
    private val context: Context,
    private val config: FortressConfig,
    private val signatureValidator: SignatureValidator = AndroidSignatureValidator(context, config),
    private val hookingDetector: HookingDetector = HookingDetector()
) {

    fun evaluate(): IntegrityResult {
        val rootSignal = detectRoot()
        val emulatorSignal = detectEmulator()
        val debugSignal = detectDebug()
        val tamperSignal = signatureValidator.validate()
        val hookSignal = detectHooking()

        return IntegrityResult(
            rootSignal = rootSignal,
            emulatorSignal = emulatorSignal,
            debugSignal = debugSignal,
            tamperSignal = tamperSignal,
            hookSignal = hookSignal
        )
    }

    private fun detectRoot(): RootSignal {
        val evidence = mutableListOf<String>()
        val rootPaths = listOf(
            "/system/app/Superuser.apk",
            "/sbin/su",
            "/system/bin/su",
            "/system/xbin/su",
            "/data/local/xbin/su",
            "/data/local/bin/su",
            "/system/sd/xbin/su",
            "/system/bin/failsafe/su",
            "/data/local/su"
        )

        val isRooted = rootPaths.any { path ->
            val file = File(path)
            if (file.exists()) {
                evidence += "Found su at $path"
                true
            } else false
        }

        return RootSignal(isRooted, evidence)
    }

    private fun detectEmulator(): EmulatorSignal {
        val reasons = mutableListOf<String>()

        fun addIf(cond: Boolean, reason: String) {
            if (cond) reasons += reason
        }

        val fp = Build.FINGERPRINT.lowercase()
        val model = Build.MODEL.lowercase()
        val product = Build.PRODUCT.lowercase()
        val brand = Build.BRAND.lowercase()
        val device = Build.DEVICE.lowercase()
        val hardware = Build.HARDWARE.lowercase()
        val manufacturer = Build.MANUFACTURER.lowercase()

        // Classic heuristics
        addIf(fp.startsWith("generic"), "generic fingerprint")
        addIf(fp.contains("test-keys"), "test-keys fingerprint")
        addIf(model.contains("google_sdk"), "google_sdk model")
        addIf(model.contains("emulator"), "Emulator model")
        addIf(model.contains("android sdk built for x86"), "Android SDK x86 model")
        addIf(manufacturer.contains("genymotion"), "Genymotion manufacturer")

        // Modern AVD patterns
        addIf(product.contains("sdk_gphone"), "sdk_gphone product")
        addIf(product.contains("emulator"), "emulator product")
        addIf(brand == "generic" && device.startsWith("generic"), "generic brand/device")
        addIf(hardware.contains("goldfish") || hardware.contains("ranchu"), "emulator hardware ($hardware)")

        val isEmulator = reasons.isNotEmpty()
        return EmulatorSignal(isEmulator, reasons)
    }

    private fun detectDebug(): DebugSignal {
        val attached = Debug.isDebuggerConnected() || Debug.waitingForDebugger()

        // Only run the native tracer check in more strict environments
        val nativeTracer = if (config.environment == FortressEnvironment.Production) {
            NativeChecks.isTracerPresent()
        } else {
            false
        }

        return DebugSignal(
            isDebuggerAttached = attached,
            nativeTracerDetected = nativeTracer
        )
    }

    private fun detectHooking(): HookSignal = hookingDetector.detect()
}