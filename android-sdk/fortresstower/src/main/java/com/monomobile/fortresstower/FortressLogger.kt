package com.monomobile.fortresstower

import com.monomobile.fortresstower.trust.TrustScore

/**
 * Optional callback invoked after each integrity evaluation.
 *
 * You can use this to:
 *  - send metrics to analytics
 *  - log to Crashlytics
 *  - stream to your backend
 */
fun interface FortressLogger {
    fun onEvaluation(result: IntegrityResult, score: TrustScore)
}