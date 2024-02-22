package com.cesar.storicesar.data.repository

import com.cesar.storicesar.entities.BankMovement
import com.cesar.storicesar.entities.StateResult
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class MovementRepositoryImpl @Inject constructor(
    private val firebaseDatabase: FirebaseDatabase
) : MovementRepository {

    override suspend fun getMovements(): Flow<StateResult<List<BankMovement>>> = callbackFlow {

        val reference = firebaseDatabase.getReference(MOVEMENTS)

        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val listResult = mutableListOf<BankMovement>()

                for (childSnapshot in dataSnapshot.children) {
                    val data = childSnapshot.getValue(BankMovement::class.java)
                    data?.let { listResult.add(it) }
                }
                trySend(StateResult.Success(listResult)).isSuccess

            }

            override fun onCancelled(databaseError: DatabaseError) {
                trySend(StateResult.Error).isSuccess
            }
        }
        reference.addValueEventListener(valueEventListener)
        awaitClose {
            reference.removeEventListener(valueEventListener)
        }
    }


    companion object {
        const val MOVEMENTS = "Movements"
    }
}