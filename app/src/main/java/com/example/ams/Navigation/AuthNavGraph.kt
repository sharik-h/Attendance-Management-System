package com.example.ams.Navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.ams.Login.Pages.LoginPage
import com.example.ams.Login.Pages.NewAccountPage

fun NavGraphBuilder.AuthNavGraph(navHostController: NavHostController) {
    navigation(
        startDestination = Screen.LoginPage.route,
        route = LOGIN_ROUTE
    ){
        composable(route = Screen.LoginPage.route) {
            LoginPage(navHostController = navHostController)
        }
        composable(route = Screen.NewAccountPage.route) {
            NewAccountPage(navHostController = navHostController)
        }
    }
}