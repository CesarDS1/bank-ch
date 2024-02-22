package com.cesar.storicesar.features.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cesar.storicesar.data.repository.MovementRepository
import com.cesar.storicesar.entities.BankAccount
import com.cesar.storicesar.entities.BankMovement
import com.cesar.storicesar.entities.StateResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val movementRepository: MovementRepository
) : ViewModel() {

    var emptyMovements by mutableStateOf(false)

    internal val movements = mutableListOf<BankMovement>()


    fun getBankAccount(): BankAccount {
        //Mock bank account
        return BankAccount("123456789", "$1500.00")
    }

    internal fun getMovements() = viewModelScope.launch {
        emptyMovements = false
        movementRepository.getMovements().collect { result ->
            when (result) {
                is StateResult.Success -> {
                    movements.addAll(result.data)
                }

                is StateResult.Error -> {
                    emptyMovements = true
                }

            }
        }

    }
}