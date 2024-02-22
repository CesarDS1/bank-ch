package com.cesar.storicesar.features.onboarding

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cesar.storicesar.data.repository.UserRepository
import com.cesar.storicesar.entities.StateResult
import com.cesar.storicesar.entities.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnboardingTakePhotoViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private lateinit var user: User

    private val _registerState: MutableStateFlow<TakePhotoState> =
        MutableStateFlow(TakePhotoState.Default)
    val registerState: StateFlow<TakePhotoState> = _registerState

    fun initUser(user: User) {
        this.user = user
    }

    fun saveUser(image: Uri) = viewModelScope.launch {
        userRepository.register(user, image).collect {
            when (it) {
                is StateResult.Success ->
                    _registerState.value = TakePhotoState.Success
                is StateResult.Error ->
                    _registerState.value = TakePhotoState.Error
            }

        }
    }
}

sealed class TakePhotoState {
    data object Default : TakePhotoState()
    data object Success : TakePhotoState()
    data object Error : TakePhotoState()
}