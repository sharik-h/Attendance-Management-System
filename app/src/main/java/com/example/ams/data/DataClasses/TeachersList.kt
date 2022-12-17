package com.example.ams.data.DataClasses

import android.graphics.Bitmap

data class TeachersList (
    val uid: String = "",
    val name: String = "",
    val phone: String = "",
    val email: String = "",
    val image: Bitmap? = null
)