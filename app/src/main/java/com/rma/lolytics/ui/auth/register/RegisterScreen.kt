package com.rma.lolytics.ui.auth.register

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.rma.lolytics.R
import com.rma.lolytics.ui.auth.login.model.AuthUiState
import com.rma.lolytics.ui.shared.AppTopBar
import com.rma.lolytics.ui.shared.LoLyticsErrorDialog
import com.rma.lolytics.ui.shared.PasswordInputField
import org.koin.androidx.compose.koinViewModel

@Composable
internal fun RegisterScreen(
    title: String,
    navigateBack: () -> Unit,
    onRegistrationSuccess: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val registerViewModel = koinViewModel<RegisterViewModel>()
    val screenState = registerViewModel.registerState.collectAsState()

    if (screenState.value is AuthUiState.Success) {
        onRegistrationSuccess()
    }

    Scaffold(
        topBar = {
            AppTopBar(
                navigateBack = navigateBack,
                title = title
            )
        }
    ) { paddingValues ->
        RegisterScreenContent(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues),
            onRegisterClick = { email, password ->
                registerViewModel.register(
                    email = email,
                    password = password,
                )
            },
            screenState = screenState.value,
            onDismissDialog = {
                registerViewModel.setIdleState()
            }
        )
    }
}

@Composable
private fun RegisterScreenContent(
    onDismissDialog: () -> Unit,
    screenState: AuthUiState<*>,
    onRegisterClick: (String, String) -> Unit,
    modifier: Modifier = Modifier,
    ) {
    var email by rememberSaveable {
        mutableStateOf("")
    }

    var password by rememberSaveable {
        mutableStateOf("")
    }

    var confirmPassword by rememberSaveable {
        mutableStateOf("")
    }

    val isLoading = screenState is AuthUiState.Loading

    var showErrorDialog = screenState is AuthUiState.Error

    if (showErrorDialog) {
        LoLyticsErrorDialog(
            onDismissRequest = {
                onDismissDialog()
                showErrorDialog = false
            },
            onConfirmClick = {
                onDismissDialog()
                showErrorDialog = false
            },
            text = "${(screenState as AuthUiState.Error).message}"
        )
    }

    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 32.dp)
            .imePadding(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = stringResource(R.string.register_text),
            style = MaterialTheme.typography.titleLarge
        )

        if(isLoading) {
            CircularProgressIndicator()
        }

        TextField(
            modifier = Modifier
                .padding(vertical = 16.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp)),
            value = email,
            onValueChange = {
                email = it
            },
            placeholder = {
                Text(
                    text = stringResource(R.string.auth_email)
                )
            },
            singleLine = true
        )

        PasswordInputField(
            placeholderText = stringResource(R.string.auth_password),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            onValueChange = {
                password = it
            }
        )

        PasswordInputField(
            placeholderText = stringResource(R.string.register_confirm_password),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            onValueChange = {
                confirmPassword = it
            }
        )

        Button(
            enabled = isRegisterDataValid(
                email = email,
                password = password,
                confirmPassword = confirmPassword
            ),
            onClick = {
                onRegisterClick(
                    email,
                    password
                )
            }
        ) {
            Text(
                text = stringResource(R.string.register_screen_button_text)
            )
        }
    }
}

fun isRegisterDataValid(
    email: String,
    password: String,
    confirmPassword: String
): Boolean {
    return email.trim().isNotEmpty() && password == confirmPassword && password.isNotEmpty()
}