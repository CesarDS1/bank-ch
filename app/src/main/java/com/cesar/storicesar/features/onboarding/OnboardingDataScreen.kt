package com.cesar.storicesar.features.onboarding

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.cesar.storicesar.R
import com.cesar.storicesar.entities.User
import kotlinx.coroutines.flow.StateFlow


@Composable
fun OnboardingData(
    uiState: RegisterUIState,
    registerStateFlow: StateFlow<RegisterState>,
    onSave: () -> Unit,
    onSaveSuccess: (User) -> Unit
) {
    registerStateFlow.collectAsState().value.also {
        if (it is RegisterState.Success) {
            onSaveSuccess(it.user)
        }
    }

    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
    ) {
        Text(
            text = stringResource(id = R.string.onboarding_register),
            modifier = Modifier.padding(vertical = 16.dp)
        )
        OutlinedTextField(
            value = uiState.name,
            singleLine = true,
            onValueChange = { uiState.setNameRegister(it) },
            label = { Text("Name") },
            modifier = Modifier.fillMaxWidth(),
            isError = uiState.errorNameMessage.isNotBlank(),
        )
        if (uiState.errorNameMessage.isNotBlank()) {
            Text(uiState.errorNameMessage, color = Color.Red)
        }
        OutlinedTextField(
            value = uiState.surname,
            onValueChange = { uiState.setSurnameRegister(it) },
            label = { Text("Surname") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            isError = uiState.errorSurNameMessage.isNotBlank(),
        )
        if (uiState.errorSurNameMessage.isNotBlank()) {
            Text(uiState.errorSurNameMessage, color = Color.Red)
        }
        OutlinedTextField(
            value = uiState.email,
            onValueChange = { uiState.setEmailRegister(it) },
            label = { Text("Email") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            isError = uiState.errorEmailMessage.isNotBlank(),
        )
        if (uiState.errorEmailMessage.isNotBlank()) {
            Text(uiState.errorEmailMessage, color = Color.Red)
        }
        OutlinedTextField(
            value = uiState.password,
            onValueChange = { uiState.setPasswordRegister(it) },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation(),
            isError = uiState.errorPasswordMesage.isNotBlank(),
            singleLine = true,
        )
        if (uiState.errorPasswordMesage.isNotBlank()) {
            Text(uiState.errorPasswordMesage, color = Color.Red)
        }
        Button(
            onClick = {
                onSave()
            }, modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        ) {
            Text(text = stringResource(id = R.string.onboarding_continue_btn))
        }
    }

}