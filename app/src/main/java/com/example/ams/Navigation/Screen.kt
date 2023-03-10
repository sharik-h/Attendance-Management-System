package com.example.ams.Navigation


const val HOME_ROUTE = "home_route"
const val LOGIN_ROUTE = "login_route"
const val MAIN_ROUTE = "main_route"
const val NEW_COURSE = "new_course"

sealed class Screen(val route: String) {
    object LoginPage: Screen(route = "LoginPage")
    object NewAccountPage: Screen(route = "NewAccountPage")
    object Splash: Screen(route = "SplashScree")
    object ListOfCoursesPage: Screen(route = "ListOfCoursesPage")
    object NewCourse: Screen(route = "NewCourse")
    object NewStudent: Screen(route = "NewStudent/{courseName}/{adminId}"){
        fun passCourseName(courseName: String, adminId: String) = "NewStudent/$courseName/$adminId"
    }
    object ViewCourse: Screen(route = "ViewCourse/{courseName}/{adminId}"){
        fun passCourseName(courseName: String, adminId: String) = "ViewCourse/$courseName/$adminId"
    }
    object ImportCourse: Screen(route = "ImportCourse")
    object Notifications: Screen(route = "Notifications/{courseName}/{adminId}"){
        fun PassCourseName(courseName: String, adminId: String) = "Notifications/$courseName/$adminId"
    }
    object ViewDetails: Screen(route = "ViewDetails/{courseName}/{adminId}"){
        fun passCourseName(courseName: String, adminId: String) = "ViewDetails/$courseName/$adminId"
    }
    object ViewTeachers: Screen(route = "ViewTeachers/{courseName}/{adminId}"){
        fun passCourseName(courseName: String, adminId: String) = "ViewTeachers/$courseName/$adminId"
    }
    object ViewStudents: Screen(route = "ViewStudents/{courseName}/{adminId}") {
        fun passCourseName(courseName: String, adminId: String) = "ViewStudents/$courseName/$adminId"
    }
    object MarkAtdManually: Screen(route = "MarkAtdManually/{courseName}/{adminId}"){
        fun passCourseName(courseName: String, adminId: String) = "MarkAtdManually/$courseName/$adminId"
    }
    object NewNotification: Screen(route = "NewNotification/{courseName}/{adminId}"){
        fun passCourseName(courseName: String, adminId: String) = "NewNotification/$courseName/$adminId"
    }
    object ViewAttendance: Screen(route = "Attendance/{courseName}/{adminId}"){
        fun passCourseName(courseName: String, adminId: String) = "Attendance/$courseName/$adminId"
    }
}
