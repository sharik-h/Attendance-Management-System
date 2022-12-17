package com.example.ams.data.DataClasses

data class AttendceDetail(
    val registerNo: String,
    val attendance: MutableList<Pair<Int, Boolean>>
)