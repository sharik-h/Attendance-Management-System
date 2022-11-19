package com.example.ams.data

data class AttendceDetail(
    val registerNo: String,
    val attendance: MutableList<Pair<Int, Boolean>>
)