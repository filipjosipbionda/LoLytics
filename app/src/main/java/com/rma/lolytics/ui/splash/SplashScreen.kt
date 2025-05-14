package com.rma.lolytics.ui.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.rma.lolytics.R
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel

@Composable
internal fun SplashScreen(
    onNavigateToHome: () -> Unit,
    onNavigateToLogin: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val splashViewModel: SplashViewModel = koinViewModel<SplashViewModel>()

    LaunchedEffect(Unit) {
        delay(1000)
        if (splashViewModel.checkIfUserIsLoggedIn()) {
            onNavigateToHome()
        } else {
            onNavigateToLogin()
        }
    }

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier.height(100.dp)
        ) {
            Image(
                painter = painterResource(R.drawable.lolytics_logo),
                contentDescription = null,
                contentScale = ContentScale.Crop,
            )
        }

        Text(
            text = "Powered by beemzaki",
            style = MaterialTheme
                .typography
                .bodyMedium
                .copy(
                    fontWeight = FontWeight.Bold
                ),
        )
    }
}