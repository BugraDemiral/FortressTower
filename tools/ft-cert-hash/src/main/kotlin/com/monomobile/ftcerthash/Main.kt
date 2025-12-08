package com.monomobile.ftcerthash

import java.io.FileInputStream
import java.security.KeyStore
import java.security.MessageDigest

fun main(args: Array<String>) {
    val options = args.toList()
    val keystorePath = getOption(options, "--keystore")
    val alias = getOption(options, "--alias")
    val storePass = getOption(options, "--storepass")

    if (keystorePath == null || alias == null || storePass == null) {
        printUsage()
        return
    }

    try {
        val ks = KeyStore.getInstance(KeyStore.getDefaultType())
        FileInputStream(keystorePath).use { fis ->
            ks.load(fis, storePass.toCharArray())
        }
        val cert = ks.getCertificate(alias)
            ?: error("No certificate found for alias '$alias'")

        val encoded = cert.encoded
        val sha256 = sha256Hex(encoded)

        println("SHA-256 (FortressTower format):")
        println(sha256)
    } catch (t: Throwable) {
        System.err.println("Error: ${t.message}")
    }
}

private fun getOption(args: List<String>, name: String): String? {
    val index = args.indexOf(name)
    if (index == -1 || index == args.size - 1) return null
    return args[index + 1]
}

private fun printUsage() {
    println(
        """
        FortressTower cert hash helper

        Usage:
          java -jar ft-cert-hash.jar \
            --keystore your-release-key.jks \
            --alias your_alias \
            --storepass your_store_password
        """.trimIndent()
    )
}

private fun sha256Hex(bytes: ByteArray): String {
    val digest = MessageDigest.getInstance("SHA-256").digest(bytes)
    return digest.joinToString("") { "%02x".format(it) }
}
