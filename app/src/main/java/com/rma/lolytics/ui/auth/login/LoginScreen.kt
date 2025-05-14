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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.rma.lolytics.R
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
        showErrorDialog = screenState is LoginUiState.Error
        if (screenState is LoginUiState.Success) {
            onLoginSuccess()
            loginViewModel.setIdleState()
        }
    }
        LoginScreenContent(
                isLoading = screenState is LoginUiState.Loading,
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
                Dialog(
                    onDismissRequest = {
                        showErrorDialog = false
                        loginViewModel.setIdleState()
                    }
                ) {
                    Card {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                text = "${(screenState as LoginUiState.Error).message}"
                            )
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End
                            ) {
                                TextButton(
                                    onClick = {
                                        showErrorDialog = false
                                        loginViewModel.setIdleState()
                                    }
                                ) {
                                    Text("Ok")
                                }
                            }
                        }
                    }
                }
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
    var email by remember{
        mutableStateOf("")
    }

    var password by remember {
        mutableStateOf("")
    }

    var isPasswordVisible by remember {
        mutableStateOf(false)
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
                TextField(
                    modifier = Modifier
                        .padding(bottom = 4.dp)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp)),
                    placeholder = {
                        Text(stringResource(R.string.auth_password))
                    },
                    trailingIcon = {
                        IconButton(
                            onClick = {
                                isPasswordVisible = !isPasswordVisible
                            }
                        ) {
                            Icon(
                                imageVector = if (isPasswordVisible) Icons.Filled.VisibilityOff
                                else
                                    Icons.Filled.Visibility,
                                contentDescription = null,
                            )
                        }
                    },
                    visualTransformation = if(isPasswordVisible)
                        VisualTransformation.None else
                        PasswordVisualTransformation(),
                    value = password,
                    onValueChange = {
                        password = it
                    },
                    singleLine = true,
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