package com.rma.lolytics.ui.shared

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
internal fun LolyticsAlertDialog(
    title: String,
    text: String,
    confirmButtonText: String,
    cancelButtonText: String,
    onConfirmRequest: () -> Unit,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    ) {
        AlertDialog(
            modifier = modifier,
            title = {
                Text(
                    text = title
                )
            },
            text = {
                Text(
                    text = text
                )
            },
            onDismissRequest = onDismissRequest,
            confirmButton = {
                Button(
                    onClick = onConfirmRequest
                ) {
                    Text(
                        text = confirmButtonText,
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = onDismissRequest,
                ) {
                    Text(
                        text = cancelButtonText,
                    )
                }
            }
        )
}

@Preview
@Composable
private fun LolyticsAlertDialogPreview() {
    Surface {
        LolyticsAlertDialog(
            title = "Error dialog",
            onDismissRequest = {},
            onConfirmRequest = {},
            confirmButtonText = "OK",
            cancelButtonText = "Cancel",
            text = "Are you sure that you really want to " +
                    "delete this match? If you delete it, there is no way to retrieve it back",

            )
    }
}