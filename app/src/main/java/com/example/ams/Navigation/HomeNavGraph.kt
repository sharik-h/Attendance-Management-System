package com.example.ams.Navigation

import androidx.navigation.*
import androidx.navigation.compose.composable
import com.example.ams.MainPages.*
import com.example.ams.MainPages.NewDataPage.ViewStudentDetails
import com.example.ams.MainPages.Notifications.NewNotification
import com.example.ams.SplashScreen.SplashScreen
import com.example.ams.data.ViewModel.FirebaseViewModel

fun NavGraphBuilder.HomeNavGraph(
    navHostController: NavHostController,
    viewModel: FirebaseViewModel
) {
    navigation(
        startDestination = Screen.Splash.route,
        route = HOME_ROUTE
    ){
        composable(route = Screen.Splash.route) {
            SplashScreen(navHostController = navHostController)
        }
        composable(route = Screen.ListOfCoursesPage.route) {
            ListOfCoursePage(navHostController = navHostController, viewModel = viewModel)
        }
        composable(
            route = Screen.Notifications.route,
            arguments = listOf(
                navArgument(name = "courseName"){ type = NavType.StringType}
            )
        ){
            Notifications(
                navHostController = navHostController,
                viewModel = viewModel,
                courseName = it.arguments?.getString("courseName").toString()
            )
        }
        composable(
            route = Screen.NewNotification.route,
            arguments = listOf(
                navArgument(name = "courseName"){ type = NavType.StringType }
            )
        ){
            NewNotification(
                navHostController = navHostController,
                viewModel = viewModel,
                courseName = it.arguments?.getString("courseName").toString(),
            )
        }
        composable(
            route = Screen.ViewCourse.route,
            arguments = listOf(
                navArgument(name = "courseName") { type = NavType.StringType },
                navArgument(name = "adminId") { type = NavType.StringType }
            )
        ) {
            ViewCourse(
                navHostController = navHostController,
                courseName = it.arguments?.getString("courseName").toString(),
                adminId = it.arguments?.getString("adminId").toString(),
                viewModel = viewModel
            )
        }
        composable(
            route = Screen.ViewDetails.route,
            arguments = listOf(
                navArgument(name = "courseName") { type = NavType.StringType },
                navArgument(name = "adminId") { type = NavType.StringType }
            )
        ){
            ViewDetails(
                navHostController = navHostController,
                courseName = it.arguments?.getString("courseName").toString(),
                adminId = it.arguments?.getString("adminId").toString(),
                viewModel = viewModel
            )
        }
        composable(
            route = Screen.ViewTeachers.route,
            arguments = listOf(
                navArgument(name = "courseName") { type = NavType.StringType },
                navArgument(name= "adminId") { type = NavType.StringType }
            )
        ){
            ViewTeachers(
                navHostController = navHostController,
                courseName = it.arguments?.getString("courseName").toString(),
                adminId = it.arguments?.getString("adminId").toString(),
                viewModel = viewModel
            )
        }
        composable(
            route = Screen.ViewStudents.route,
            arguments = listOf(
                navArgument(name = "courseName") { type = NavType.StringType },
                navArgument(name = "adminId"){ type = NavType.StringType }
            )
        ){
            ViewStudents(
                navHostController = navHostController,
                adminId = it.arguments?.getString("adminId").toString(),
                courseName = it.arguments?.getString("courseName").toString(),
                viewModel = viewModel
            )
        }
        composable(
            route = Screen.ViewStudentDetails.route,
            arguments = listOf(
                navArgument(name = "courseName") { type = NavType.StringType },
                navArgument(name = "adminId") { type = NavType.StringType },
                navArgument(name = "registerNo") { type = NavType.StringType }
            )
        ){
            ViewStudentDetails(
                navHostController = navHostController,
                courseName = it.arguments?.getString("courseName").toString(),
                adminId = it.arguments?.getString("adminId").toString(),
                registerNo = it.arguments?.getString("registerNo").toString(),
                viewModel = viewModel
            )
        }
        composable(
            route = Screen.MarkAtdManually.route,
            arguments = listOf(
                navArgument(name = "courseName") { type = NavType.StringType },
                navArgument(name = "adminId") { type = NavType.StringType }
            )
        ) {
            MarkAtdManually(
                navHostController = navHostController,
                courseName = it.arguments?.getString("courseName").toString(),
                adminId = it.arguments?.getString("adminId").toString(),
                viewModel = viewModel
            )
        }
    }
}