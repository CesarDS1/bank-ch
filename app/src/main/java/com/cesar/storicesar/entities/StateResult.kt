package com.cesar.storicesar.entities

sealed class StateResult<out T:Any?> {

    data class Success<out T: Any?>(val data:T): StateResult<T>()
    data object Error: StateResult<Nothing>()


}