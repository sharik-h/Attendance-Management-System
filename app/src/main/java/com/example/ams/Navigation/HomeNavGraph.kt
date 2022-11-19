package com.example.ams.Navigation

import androidx.navigation.*
import androidx.navigation.compose.composable
import com.example.ams.MainPages.ListOfCoursePage
import com.example.ams.MainPages.ViewCourse
import com.example.ams.SplashScreen.SplashScreen

fun NavGraphBuilder.HomeNavGraph(
    navHostController: NavHostController
) {
    navigation(
        startDestination = Screen.Splash.route,
        route = HOME_ROUTE
    ){
        composable(route = Screen.Splash.route) {
            SplashScreen(navHostController = navHostController)
        }
        composable(route = Screen.ListOfCoursesPage.route) {
            ListOfCoursePage(navHostController = navHostController)
        }
        composable(
            route = Screen.ViewCourse.route,
            arguments = listOf( navArgument(name = "courseName"){type = NavType.StringType} )
        ) {
            ViewCourse(navHostController = navHostController, courseName = it.arguments?.getString("courseName").toString())
        }
    }
}