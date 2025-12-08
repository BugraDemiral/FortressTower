package com.monomobile.fortresstower.integrity

import com.monomobile.fortresstower.TamperSignal

internal interface SignatureValidator {
    fun validate(): TamperSignal
}