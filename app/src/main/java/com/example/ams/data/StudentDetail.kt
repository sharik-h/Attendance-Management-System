package com.example.ams.data

import android.net.Uri

data class StudentDetail (
        var name: String = "",
        var phone: String = "",
        var registerNo: String = "",
        var images: MutableList<Uri?> = mutableListOf()
)