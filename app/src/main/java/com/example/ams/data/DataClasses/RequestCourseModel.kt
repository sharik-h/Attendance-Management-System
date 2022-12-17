package com.example.ams.data.DataClasses

data class RequestCourseModel (
    var requestId: String = "",
    val ClassName: String = "",
    val AdminPhone: String = "",
    val TeacherUid: String = "",
    val TeacherName: String = "",
    val TeacherPhone: String = "",
    val TeacherEmail: String = ""
)