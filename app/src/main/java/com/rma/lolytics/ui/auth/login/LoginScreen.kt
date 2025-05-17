package com.rma.lolytics.ui.auth.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rma.lolytics.R
import com.rma.lolytics.ui.auth.login.model.AuthUiState
import com.rma.lolytics.ui.shared.LoLyticsErrorDialog
import com.rma.lolytics.ui.shared.PasswordInputField
import org.koin.androidx.compose.koinViewModel

@Composable
internal fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onForgotPassword: () -> Unit,
    onRegister: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val loginViewModel = koinViewModel<LoginViewModel>()
    val screenState by loginViewModel.loginState.collectAsState()
    var showErrorDialog by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(screenState) {
        showErrorDialog = screenState is AuthUiState.Error
        if (screenState is AuthUiState.Success) {
            onLoginSuccess()
            loginViewModel.setIdleState()
        }
    }
        LoginScreenContent(
                isLoading = screenState is AuthUiState.Loading,
                onLogin = { email, password ->
                    loginViewModel.login(
                        email = email,
                        password = password
                    )
                },
                onForgotPassword = onForgotPassword,
                onRegister = onRegister,
                modifier = modifier
                    .fillMaxSize()
                    .imePadding()
            )

            if (showErrorDialog) {
                LoLyticsErrorDialog(
                    onDismissRequest = {
                        showErrorDialog = false
                    },
                    onConfirmClick = {
                        showErrorDialog = false
                        loginViewModel.setIdleState()
                    },
                    text = "${(screenState as AuthUiState.Error).message}"
                )
            }

    }

@Composable
private fun LoginScreenContent(
    isLoading: Boolean,
    onLogin: (String, String) -> Unit,
    onRegister: () -> Unit,
    onForgotPassword: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var email by rememberSaveable {
        mutableStateOf("")
    }

    var password by rememberSaveable {
        mutableStateOf("")
    }

    Column(
        modifier = modifier.background(color = MaterialTheme.colorScheme.background),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
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
            text = stringResource(R.string.login_enter_data_text),
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(vertical = 16.dp)
        )

        if (isLoading) {
            CircularProgressIndicator()
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(
                vertical = 16.dp,
                horizontal = 32.dp
            )
        ) {
            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp)),
                value = email,
                placeholder = {
                    Text(stringResource(R.string.auth_email))
                },
                onValueChange = {
                    email = it
                },
                singleLine = true,
            )
            Column(
                modifier = Modifier.padding(vertical = 16.dp),
                horizontalAlignment = Alignment.End
            ) {
                PasswordInputField(
                    placeholderText = stringResource(R.string.auth_password),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 4.dp),
                    onValueChange = {
                        password = it
                    }
                )
                TextButton(
                    onClick = onForgotPassword
                ) {
                    Text(
                        text = stringResource(R.string.login_password_reset_text),
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = MaterialTheme.colorScheme.primary
                        )
                    )
                }
            }

            Button(
                onClick = {
                    onLogin(email, password)
                },
                modifier = Modifier
                    .fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text(stringResource(R.string.login_button_text))
            }
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            HorizontalDivider(modifier = Modifier.weight(1f))

            Text(
                text = stringResource(R.string.login_or),
                modifier = Modifier.padding(horizontal = 8.dp)
            )

            HorizontalDivider(modifier = Modifier.weight(1f))
        }

        TextButton(
            onClick = onRegister
        ) {
            Text(stringResource(R.string.auth_register_button))
        }
    }
}

@Preview
@Composable
private fun LoginScreenContentPreview() {
        LoginScreenContent(
            onLogin = { _, _ -> },
            onRegister = {},
            onForgotPassword = {},
            modifier = Modifier.fillMaxSize(),
            isLoading = false
        )
}