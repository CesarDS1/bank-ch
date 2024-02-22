package com.cesar.storicesar.data.repository

import com.cesar.storicesar.entities.BankMovement
import com.cesar.storicesar.entities.StateResult
import kotlinx.coroutines.flow.Flow

interface MovementRepository {
    suspend fun getMovements(): Flow<StateResult<List<BankMovement>>>
}