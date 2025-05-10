package com.rma.lolytics

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.rma.lolytics.ui.navigation.AppNavHost
import com.rma.lolytics.ui.theme.LoLyticsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LoLyticsTheme {
                AppNavHost()
            }
        }
    }
}
