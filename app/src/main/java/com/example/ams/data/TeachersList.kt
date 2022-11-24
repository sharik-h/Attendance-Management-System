package com.example.ams.data

import android.graphics.Bitmap

data class TeachersList (
    val uid: String = "",
    val name: String = "",
    val phone: String = "",
    val email: String = "",
    val image: Bitmap? = null
)