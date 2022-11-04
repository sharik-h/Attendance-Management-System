package com.example.ams.Login.LoginNavigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.ams.Login.Pages.LoginPage
import com.example.ams.Login.Pages.NewAccountPage

@Composable
fun NavGraph(navHostController: NavHostController) {
    NavHost(
        navController = navHostController,
        startDestination = Screen.LoginPage.route
    ){
        composable(route = Screen.LoginPage.route) {
            LoginPage(navHostController = navHostController)
        }
    }
}