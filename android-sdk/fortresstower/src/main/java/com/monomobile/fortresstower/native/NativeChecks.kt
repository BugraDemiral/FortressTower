package com.monomobile.fortresstower.native

/**
 * Wrapper for native integrity checks implemented with the NDK.
 */
object NativeChecks {

    init {
        runCatching {
            System.loadLibrary("fortresstower")
        }
    }

    /**
     * Uses ptrace to detect if another process is tracing / debugging us.
     */
    external fun isTracerPresent(): Boolean
}