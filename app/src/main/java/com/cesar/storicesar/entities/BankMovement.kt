package com.cesar.storicesar.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class BankMovement(val title: String, val description: String, val amount: String) : Parcelable{
    constructor() : this("", "", "")
}