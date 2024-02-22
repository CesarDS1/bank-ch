package com.cesar.storicesar.features.login

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cesar.storicesar.R
import kotlinx.coroutines.flow.StateFlow

@Composable
fun LoginContent(
    uiState: LoginUIState,
    uiStateFlow: StateFlow<LoginState>,
    onLogin: (email: String, password: String) -> Unit,
    onRegister: () -> Unit,
    onLoginSuccess: () -> Unit
) {
    val context = LocalContext.current

    uiStateFlow.collectAsState().value.also { state ->
        if (state is LoginState.Success) {
            onLoginSuccess()
        } else if (state is LoginState.Error) {
            showToast(context, stringResource(id = R.string.error_login))
        }
    }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {

        Text(
            text = stringResource(id = R.string.login),
            fontSize = 32.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(vertical = 16.dp)
                .fillMaxWidth()
        )
        OutlinedTextField(
            value = uiState.email,
            onValueChange = {
                uiState.setEmailText(it)
            },
            isError = uiState.errorEmailMessage.isNotBlank(),
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )

        if (uiState.errorEmailMessage.isNotBlank()) {
            Text(uiState.errorEmailMessage, color = Color.Red)
        }
        OutlinedTextField(
            value = uiState.password,
            onValueChange = { uiState.setPasswordText(it) },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = { onLogin(uiState.email, uiState.password) }, modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        ) {
            Text(text = stringResource(id = R.string.onboarding_continue_btn))
        }

        Button(
            onClick = { onRegister() }, modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        ) {
            Text(text = stringResource(id = R.string.onboarding_register))
        }

    }

}

fun showToast(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}