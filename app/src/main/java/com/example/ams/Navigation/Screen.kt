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
    object NewStudent: Screen(route = "NewStudent/{courseName}"){
        fun passCourseName(courseName: String) = "NewStudent/$courseName"
    }
    object ViewCourse: Screen(route = "ViewCourse/{courseName}/{adminId}"){
        fun passCourseName(courseName: String, adminId: String) = "ViewCourse/$courseName/$adminId"
    }
    object ImportCourse: Screen(route = "ImportCourse")
}
