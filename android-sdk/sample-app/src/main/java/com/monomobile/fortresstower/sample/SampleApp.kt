package com.monomobile.fortresstower.sample

import android.app.Application
import android.util.Log
import com.monomobile.fortresstower.*

class SampleApp : Application() {
    override fun onCreate() {
        super.onCreate()

        FortressTower.init(
            this,
            FortressConfig(
                licenseKey = "FT-DEV-KEY",
                expectedSignatureSha256 = null, // or set real hash later
                environment = FortressEnvironment.Debug,
                logger = FortressLogger { result, score ->
                    Log.d(
                        "FortressTower",
                        buildString {
                            append("score=${score.score}, level=${score.level}")
                            if (score.reasons.isNotEmpty()) {
                                append(", reasons=")
                                append(score.reasons.joinToString { it::class.simpleName ?: "?" })
                            }
                            append(", root=${result.rootSignal.isRooted}")
                            append(", emulator=${result.emulatorSignal.isEmulator}")
                            append(", debugAttached=${result.debugSignal.isDebuggerAttached}")
                            append(", nativeTracer=${result.debugSignal.nativeTracerDetected}")
                            append(", hooked=${result.hookSignal.isHooked}")
                        }
                    )
                }
            )
        )
    }
}