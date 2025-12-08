package com.monomobile.fortresstower

import com.monomobile.fortresstower.trust.TrustLevel
import com.monomobile.fortresstower.trust.TrustReason
import com.monomobile.fortresstower.trust.TrustScore

data class IntegrityResult(
    val rootSignal: RootSignal,
    val emulatorSignal: EmulatorSignal,
    val debugSignal: DebugSignal,
    val tamperSignal: TamperSignal,
    val hookSignal: HookSignal
)

data class RootSignal(
    val isRooted: Boolean,
    val evidence: List<String> = emptyList()
)

data class EmulatorSignal(
    val isEmulator: Boolean,
    val reasons: List<String> = emptyList()
)

data class DebugSignal(
    val isDebuggerAttached: Boolean,
    val nativeTracerDetected: Boolean = false
)

data class TamperSignal(
    val isSignatureValid: Boolean,
    val details: String? = null
)

data class HookSignal(
    val isHooked: Boolean,
    val evidence: List<String> = emptyList()
)

/**
 * Map raw integrity signals into a weighted com.monomobile.fortresstower.trust.TrustScore.
 */
fun IntegrityResult.toTrustScore(): TrustScore {
    var score = 100
    val reasons = mutableListOf<TrustReason>()

    if (rootSignal.isRooted) {
        score -= 40
        reasons += TrustReason.RootDetected
    }
    if (emulatorSignal.isEmulator) {
        score -= 30
        reasons += TrustReason.EmulatorDetected
    }
    if (debugSignal.isDebuggerAttached) {
        score -= 20
        reasons += TrustReason.DebuggerAttached
    }
    if (debugSignal.nativeTracerDetected) {
        score -= 30
        reasons += TrustReason.NativeTracerDetected
    }
    if (hookSignal.isHooked) {
        score -= 35
        reasons += TrustReason.HookingDetected
    }
    if (!tamperSignal.isSignatureValid) {
        score -= 50
        reasons += TrustReason.SignatureMismatch
    }

    if (score < 0) score = 0

    val level = when {
        score == 0 || !tamperSignal.isSignatureValid -> TrustLevel.BLOCK
        score <= 40 -> TrustLevel.LOW
        score <= 80 -> TrustLevel.MEDIUM
        else -> TrustLevel.HIGH
    }

    return TrustScore(score, level, reasons)
}