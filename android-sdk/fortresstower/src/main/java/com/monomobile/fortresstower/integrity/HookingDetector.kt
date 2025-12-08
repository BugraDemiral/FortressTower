package com.monomobile.fortresstower.integrity

import com.monomobile.fortresstower.HookSignal
import java.io.File

internal class HookingDetector {

    fun detect(): HookSignal {
        val evidence = mutableListOf<String>()

        val suspiciousClasses = listOf(
            "de.robv.android.xposed.XposedBridge",
            "de.robv.android.xposed.XC_MethodHook",
            "com.saurik.substrate.MS$2",
            "lsposed",
            "ruru"
        )

        for (className in suspiciousClasses) {
            try {
                Class.forName(className)
                evidence += "Hooking class present: $className"
            } catch (_: Throwable) { }
        }

        try {
            val mapsFile = File("/proc/self/maps")
            if (mapsFile.exists()) {
                val content = mapsFile.readText()
                if (content.contains("frida", ignoreCase = true)) {
                    evidence += "Frida signature found in /proc/self/maps"
                }
            }
        } catch (_: Throwable) { }

        return HookSignal(
            isHooked = evidence.isNotEmpty(),
            evidence = evidence
        )
    }
}