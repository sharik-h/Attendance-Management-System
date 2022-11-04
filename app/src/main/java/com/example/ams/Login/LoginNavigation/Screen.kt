package com.example.ams.Login.LoginNavigation

sealed class Screen(val route: String) {
    object LoginPage: Screen(route = "LoginPage")
    object NewAccountPage: Screen(route = "NewAccountPage")
}
