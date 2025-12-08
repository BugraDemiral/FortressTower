package com.monomobile.fortresstower

/**
 * Configuration used during FortressTower initialisation.
 */
data class FortressConfig(
    val licenseKey: String,
    val expectedSignatureSha256: String? = null,
    val environment: FortressEnvironment = FortressEnvironment.Production,
    val logger: FortressLogger? = null
)

/**
 * Execution environment modifier. Affects logging / strictness (future use).
 */
enum class FortressEnvironment {
    Production,
    Staging,
    Debug
}