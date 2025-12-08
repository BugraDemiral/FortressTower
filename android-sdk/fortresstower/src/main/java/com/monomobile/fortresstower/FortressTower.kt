package com.monomobile.fortresstower

import android.annotation.SuppressLint
import android.content.Context
import com.monomobile.fortresstower.integrity.IntegrityEvaluator
import com.monomobile.fortresstower.trust.TrustScore

object FortressTower {

    @Volatile
    private var initialized = false

    private lateinit var config: FortressConfig

    @SuppressLint("StaticFieldLeak") // we only keep Application context, safe
    private lateinit var integrityEvaluator: IntegrityEvaluator

    fun init(context: Context, config: FortressConfig) {
        val appContext = context.applicationContext

        this.config = config
        this.integrityEvaluator = IntegrityEvaluator(appContext, config)
        initialized = true
    }

    private fun evaluateIntegrity(): IntegrityResult {
        check(initialized) { "FortressTower not initialized. Call init() first." }
        return integrityEvaluator.evaluate()
    }

    fun currentTrustScore(): TrustScore {
        val result = evaluateIntegrity()
        val score = result.toTrustScore()
        config.logger?.onEvaluation(result, score)
        return score
    }
}