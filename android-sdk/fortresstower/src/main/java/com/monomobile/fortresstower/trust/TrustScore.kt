package com.monomobile.fortresstower.trust

data class TrustScore(
    val score: Int,
    val level: TrustLevel,
    val reasons: List<TrustReason>
)

enum class TrustLevel { BLOCK, LOW, MEDIUM, HIGH }

sealed class TrustReason {
    data object RootDetected : TrustReason()
    data object EmulatorDetected : TrustReason()
    data object DebuggerAttached : TrustReason()
    data object NativeTracerDetected : TrustReason()
    data object SignatureMismatch : TrustReason()
    data object HookingDetected : TrustReason()
}