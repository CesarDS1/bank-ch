package com.cesar.storicesar.features.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cesar.storicesar.data.repository.UserRepository
import com.cesar.storicesar.entities.StateResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: UserRepository
) : ViewModel() {

    private val _loginState: MutableStateFlow<LoginState> = MutableStateFlow(LoginState.Default)
    val loginState: StateFlow<LoginState> = _loginState

    internal val uiState: LoginUIState = LoginUIState()

    fun login(email: String, password: String) = viewModelScope.launch {
        uiState.errorLogin = false
        if (!uiState.validateFields()) {
            return@launch
        }
        repository.login(email, password).collect { result ->
            when (result) {
                is StateResult.Success -> _loginState.value = LoginState.Success
                is StateResult.Error -> _loginState.value = LoginState.Error
            }
        }
    }

    companion object {
        const val USER_DB = "User"
    }
}

sealed class LoginState {
    data object Default : LoginState()
    data object Success : LoginState()
    data object Error : LoginState()
}