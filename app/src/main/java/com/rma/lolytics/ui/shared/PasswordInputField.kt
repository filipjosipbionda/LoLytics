package com.rma.lolytics.ui.shared

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.rma.lolytics.R

@Composable
internal fun PasswordInputField(
    placeholderText: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    var isPasswordVisible by rememberSaveable {
        mutableStateOf(false)
    }

    var password by rememberSaveable {
        mutableStateOf("")
    }

    TextField(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp)),
        placeholder = {
            Text(placeholderText)
        },
        trailingIcon = {
            if (password.isNotEmpty()) {
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
            }
        },
        visualTransformation = if(isPasswordVisible)
            VisualTransformation.None else
            PasswordVisualTransformation(),
        value = password,
        onValueChange = {
            password = it
            onValueChange(it)
        },
        singleLine = true,
    )
}