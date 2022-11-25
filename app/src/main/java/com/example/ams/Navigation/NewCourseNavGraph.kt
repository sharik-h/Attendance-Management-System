package com.example.ams.Navigation

import androidx.navigation.*
import androidx.navigation.compose.composable
import com.example.ams.MainPages.NewCourse
import com.example.ams.MainPages.NewDataPage.AddNewStudent
import com.example.ams.MainPages.NewDataPage.ImportCourse

fun NavGraphBuilder.NewCourseNavGraph(
    navHostController: NavHostController
) {
    navigation(
        startDestination = Screen.NewCourse.route,
        route = NEW_COURSE
    ){
        composable(route = Screen.NewCourse.route) {
            NewCourse(navHostController = navHostController)
        }
        composable(
            route = Screen.NewStudent.route,
            arguments = listOf(
                navArgument(name = "courseName"){type = NavType.StringType},
                navArgument(name = "adminId"){type = NavType.StringType}
            )
        ) {
            AddNewStudent(
                navHostController = navHostController,
                courseName = it.arguments?.getString("courseName").toString(),
                adminId = it.arguments?.getString("adminId").toString()
            )
        }
        composable(route =  Screen.ImportCourse.route){
            ImportCourse(navHostController = navHostController)
        }
    }
}