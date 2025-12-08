package com.monomobile.fortresstower.integrity

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import com.monomobile.fortresstower.FortressConfig
import com.monomobile.fortresstower.TamperSignal
import java.security.MessageDigest

internal class AndroidSignatureValidator(
    private val context: Context,
    private val config: FortressConfig
) : SignatureValidator {

    override fun validate(): TamperSignal {
        val expected = config.expectedSignatureSha256
            ?.lowercase()
            ?.replace(":", "")
            ?.trim()

        if (expected.isNullOrBlank()) {
            return TamperSignal(
                isSignatureValid = true,
                details = "No expectedSignatureSha256 configured; skipping validation."
            )
        }

        return try {
            val pm = context.packageManager
            val pkgName = context.packageName

            val actualSha256 = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                val pkgInfo = pm.getPackageInfo(
                    pkgName,
                    PackageManager.GET_SIGNING_CERTIFICATES
                )
                val signer = pkgInfo.signingInfo?.apkContentsSigners?.first()
                signer?.toByteArray()?.let { sha256Hex(it) }
            } else {
                @Suppress("DEPRECATION")
                val pkgInfo = pm.getPackageInfo(
                    pkgName,
                    PackageManager.GET_SIGNATURES
                )
                @Suppress("DEPRECATION")
                val signer = pkgInfo.signatures?.first()
                signer?.toByteArray()?.let { sha256Hex(it) }
            }

            val matches = expected == actualSha256?.lowercase()

            TamperSignal(
                isSignatureValid = matches,
                details = if (matches) {
                    "Signature hash matches expected."
                } else {
                    "Signature hash mismatch. expected=$expected actual=$actualSha256"
                }
            )
        } catch (t: Throwable) {
            TamperSignal(
                isSignatureValid = false,
                details = "Signature validation error: ${t.message}"
            )
        }
    }

    private fun sha256Hex(bytes: ByteArray): String {
        val digest = MessageDigest.getInstance("SHA-256").digest(bytes)
        return digest.joinToString("") { "%02x".format(it) }
    }
}