package com.monomobile.fortresstower.sample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.monomobile.fortresstower.FortressTower
import com.monomobile.fortresstower.trust.TrustScore

/*
logger = FortressLogger { result, score ->
    analytics.logEvent(
        "fortress_integrity_evaluated",
        mapOf(
            "score" to score.score,
            "level" to score.level.name,
            "root" to result.rootSignal.isRooted,
            "emulator" to result.emulatorSignal.isEmulator,
            "debugAttached" to result.debugSignal.isDebuggerAttached,
            "nativeTracer" to result.debugSignal.nativeTracerDetected,
            "hooked" to result.hookSignal.isHooked,
            "signatureValid" to result.tamperSignal.isSignatureValid
        )
    )
}
*/

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SampleScreen()
        }
    }
}

@Composable
fun SampleScreen() {
    var result by remember { mutableStateOf<TrustScore?>(null) }

    Surface(color = MaterialTheme.colorScheme.background) {
        Box(
            modifier = Modifier.fillMaxSize().padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("FortressTower Sample")
                Spacer(Modifier.height(16.dp))
                Button(onClick = { result = FortressTower.currentTrustScore() }) {
                    Text("Evaluate Trust")
                }
                Spacer(Modifier.height(16.dp))
                Text(
                    text = result?.let { "Score: ${it.score}, Level: ${it.level}" }
                        ?: "Press the button to evaluate."
                )
            }
        }
    }
}
