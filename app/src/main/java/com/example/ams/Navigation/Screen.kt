package com.example.ams.Navigation


const val HOME_ROUTE = "home_route"
const val LOGIN_ROUTE = "login_route"
const val MAIN_ROUTE = "main_route"

sealed class Screen(val route: String) {
    object LoginPage: Screen(route = "LoginPage")
    object NewAccountPage: Screen(route = "NewAccountPage")
    object Splash: Screen(route = "SplashScree")
    object ListOfCoursesPage: Screen(route = "ListOfCoursesPage")
}
