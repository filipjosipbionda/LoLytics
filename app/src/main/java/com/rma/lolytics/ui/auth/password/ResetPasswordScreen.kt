package com.rma.lolytics.ui.auth.password

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rma.lolytics.R
import com.rma.lolytics.ui.auth.login.model.AuthUiState
import com.rma.lolytics.ui.shared.AppTopBar
import com.rma.lolytics.ui.shared.LoLyticsErrorDialog
import org.koin.androidx.compose.koinViewModel

@Composable
internal fun ResetPasswordScreen(
    title: String,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val resetPasswordViewModel = koinViewModel<ResetPasswordViewModel>()
    val resetPasswordScreenState by resetPasswordViewModel.passwordResetUiState.collectAsState()

    Scaffold(
        topBar = {
            AppTopBar(
                navigateBack = navigateBack,
                title = title
            )
        }
    ) { paddingValues ->
        ResetPasswordScreenContent(
            onSendResetPasswordEmail = { email ->
                resetPasswordViewModel.sendPasswordResetEmail(email)
            },
            screenState = resetPasswordScreenState,
            onDismissDialog = {
                resetPasswordViewModel.setIdleState()
            },
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
        )
    }
}

@Composable
private fun ResetPasswordScreenContent(
    onSendResetPasswordEmail: (String) -> Unit,
    onDismissDialog: () -> Unit,
    screenState: AuthUiState<*>,
    modifier: Modifier = Modifier
) {
    var showErrorDialog = screenState is AuthUiState.Error
    var text by rememberSaveable {
        mutableStateOf("")
    }
    val showSuccessToast = screenState is AuthUiState.Success

    if (
        showErrorDialog
    ) {
        LoLyticsErrorDialog(
            onConfirmClick = {
                showErrorDialog = false
                onDismissDialog()
            },
            onDismissRequest = {
                showErrorDialog = false
                onDismissDialog()
            },
            text = "${(screenState as AuthUiState.Error).message}"
        )
    }

    if (showSuccessToast) {
        Toast.makeText(
            LocalContext.current,
            stringResource(R.string.email_successfully_sent_text),
            Toast.LENGTH_LONG,
        ).show()
    }

    Column(
        modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = stringResource(R.string.reset_password_text),
            style = MaterialTheme.typography.bodyLarge.copy(
                color = MaterialTheme.colorScheme.secondary
            ),
            textAlign = TextAlign.Center
        )

        TextField(
            modifier = Modifier
                .padding(top = 16.dp)
                .clip(RoundedCornerShape(16.dp)),
            value = text,
            onValueChange = {
                text = it
            },
            singleLine = true
        )

        Button(
            onClick = {
                onSendResetPasswordEmail(text)
            },
            enabled = text.isNotEmpty(),
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text(stringResource(R.string.send_reset_email_button_text))
        }
    }
}

@Preview
@Composable
private fun ResetPasswordPreview() {
    ResetPasswordScreenContent(
        modifier = Modifier.fillMaxSize(),
        onSendResetPasswordEmail = {},
        screenState = AuthUiState.Idle,
        onDismissDialog = {}
    )
}
