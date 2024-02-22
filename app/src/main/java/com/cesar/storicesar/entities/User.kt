package com.cesar.storicesar.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(var name: String, var surName: String, var email: String, var pass: String) :
    Parcelable {
    constructor() : this("", "", "", "")

    companion object {
        const val USER = "User"
    }
}
