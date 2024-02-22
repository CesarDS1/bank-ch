package com.cesar.storicesar.features.onboarding

import androidx.lifecycle.ViewModel
import com.cesar.storicesar.entities.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class OnboardingDataViewModel @Inject constructor() : ViewModel() {

    private val _uiStateFlow: MutableStateFlow<RegisterState> =
        MutableStateFlow(RegisterState.Default)
    internal val uiStateFlow: StateFlow<RegisterState> = _uiStateFlow

    val uiState = RegisterUIState()

    fun saveData() {

        if(!uiState.validateFields()){
            return
        }

        val user = User(
            name = uiState.name,
            surName = uiState.surname,
            email = uiState.email,
            pass = uiState.password
        )
        _uiStateFlow.value = RegisterState.Success(user)

    }

}

sealed class RegisterState {
    data object Default : RegisterState()
    data class Success(var user:User) : RegisterState()
}