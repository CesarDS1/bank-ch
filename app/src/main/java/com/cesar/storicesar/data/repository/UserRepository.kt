package com.cesar.storicesar.data.repository

import android.graphics.Bitmap
import android.media.Image
import android.net.Uri
import com.cesar.storicesar.entities.StateResult
import com.cesar.storicesar.entities.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    suspend fun login(email:String, password:String): Flow<StateResult<Unit>>

    suspend fun register(user: User, image: Uri): Flow<StateResult<Unit>>
}