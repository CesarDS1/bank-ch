package com.cesar.storicesar.features.onboarding

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class RegisterUIState {

    var name by mutableStateOf("")
        private set
    var surname by mutableStateOf("")
        private set
    var email by mutableStateOf("")
        private set
    var password by mutableStateOf("")
        private set

    var errorEmailMessage by mutableStateOf("")
        private set

    var errorNameMessage by mutableStateOf("")
        private set

    var errorSurNameMessage by mutableStateOf("")
        private set

    var errorPasswordMesage by mutableStateOf("")
        private set

    fun setNameRegister(newName: String) {
        name = newName
    }

    fun setSurnameRegister(newSurName: String) {
        surname = newSurName
    }

    fun setEmailRegister(newEmail: String) {
        email = newEmail
    }

    fun setPasswordRegister(newPassword: String) {
        password = newPassword
    }

    fun validateFields(): Boolean {
        val pattern = Regex("^\\w+([.-]?\\w+)*@\\w+([.-]?\\w+)*(\\.\\w{2,})+\$")
        errorEmailMessage = if (email.isEmpty()) {
            "Email field cannot be empty"
        } else if (!pattern.matches(email)) {
            "Please fill with an valid email"
        } else {
            ""
        }

        errorNameMessage = if (name.isEmpty()) {
            "Please fill name"
        } else {
            ""
        }

        errorSurNameMessage = if (surname.isEmpty()) {
            "Please fill name"
        } else {
            ""
        }

        errorPasswordMesage = if (password.isEmpty()) {
            "Please fill name"
        } else {
            ""
        }

        return (name.isNotEmpty() && surname.isNotEmpty() && errorEmailMessage.isEmpty() && password.isNotEmpty())
    }
}