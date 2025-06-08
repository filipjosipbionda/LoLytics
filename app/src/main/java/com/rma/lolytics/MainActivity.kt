package com.rma.lolytics

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
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
        // Declare the launcher at the top of your Activity/Fragment:
        createNotificationChannel(this)
        setContent {
            LoLyticsTheme {
                AppNavHost()
            }
        }
    }
}

private fun createNotificationChannel(context: Context) {
    val channel = NotificationChannel(
        "default_channel_id",
        "Default Channel",
        NotificationManager.IMPORTANCE_DEFAULT
    ).apply {
        description = "Match collection changed notification"
    }

    val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    notificationManager.createNotificationChannel(channel)
}