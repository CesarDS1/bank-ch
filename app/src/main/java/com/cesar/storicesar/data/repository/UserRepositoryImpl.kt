package com.cesar.storicesar.data.repository

import android.net.Uri
import android.util.Log
import com.cesar.storicesar.entities.StateResult
import com.cesar.storicesar.entities.User
import com.cesar.storicesar.features.login.LoginViewModel
import com.cesar.storicesar.util.encodeEmail
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject


class UserRepositoryImpl @Inject constructor(
    private val firebaseDatabase: FirebaseDatabase,
    private val firebaseStorage: FirebaseStorage
) : UserRepository {

    override suspend fun login(email: String, password: String): Flow<StateResult<Unit>> =
        callbackFlow {

            val reference = firebaseDatabase.getReference(LoginViewModel.USER_DB)


            val listener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val user = snapshot.getValue(User::class.java)
                    if (user != null && user.pass == password) {

                        trySend(StateResult.Success(Unit)).isSuccess
                    } else {
                        trySend(StateResult.Error).isSuccess
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    trySend(StateResult.Error).isSuccess
                }
            }

            reference.child(email.encodeEmail()).addListenerForSingleValueEvent(listener)
            awaitClose {
                reference.removeEventListener(listener)
            }
        }

    override suspend fun register(user: User, image: Uri): Flow<StateResult<Unit>> =
        callbackFlow {
            val encodeEmail = user.email.encodeEmail()

            val riversRef = firebaseStorage.reference.child("images/${encodeEmail}")

            val uploadTask: UploadTask = riversRef.putFile(image)


            uploadTask.addOnFailureListener { exception ->
                Log.e("Upload Photo", exception.printStackTrace().toString())

            }.addOnSuccessListener {
                Log.i("Upload Photo", "success")
            }


            firebaseDatabase.getReference(User.USER).child(encodeEmail).setValue(user)
                .addOnCompleteListener {
                    trySend(StateResult.Success(Unit)).isSuccess

                }.addOnFailureListener {
                    trySend(StateResult.Error).isSuccess

                }

            awaitClose {

            }
        }

}