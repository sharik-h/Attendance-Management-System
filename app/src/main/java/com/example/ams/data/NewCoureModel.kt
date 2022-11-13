package com.example.ams.data

data class NewCoureModel (
    var name: String = "",
    var courseName: String = "",
    var bactchFrom: String= "",
    var batchTo: String = "",
    var noAttendace: String = "",
    var teachersList: List<TeachersList> = emptyList(),
    var studentsList: List<StudentDetail> = emptyList()
)