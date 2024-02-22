package com.cesar.storicesar.util

fun String.encodeEmail(): String{
    return this.replace("@", "-at-").replace('.','_')
}
const val PHOTO_NAME = "yyyyMMdd_HHmmss"
const val TYPE_JPEG = "image/jpeg"
const val CAMERAX_IMAGE= "Pictures/CameraX-Image"