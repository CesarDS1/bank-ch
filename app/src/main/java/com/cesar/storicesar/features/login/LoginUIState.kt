package com.cesar.storicesar.features.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class LoginUIState {

    var email by mutableStateOf("")
        private set

    var errorEmailMessage by mutableStateOf("")
        private set

    var password by mutableStateOf("")
        private set

    var errorLogin by mutableStateOf(false)

    fun setEmailText(newValue: String) {
        email = newValue
    }

    fun setPasswordText(newPassword: String) {
        password = newPassword
    }

    fun validateFields(): Boolean {
        val pattern = Regex("^\\w+([.-]?\\w+)*@\\w+([.-]?\\w+)*(\\.\\w{2,})+\$")
        errorEmailMessage = if (email.isEmpty()) {
            "Email field cannot be empty"
        } else if (!pattern.matches(email)) {
            "Please fill with a valid email"
        } else {
            ""
        }

        return (errorEmailMessage.isEmpty() && password.isNotEmpty())
    }

}