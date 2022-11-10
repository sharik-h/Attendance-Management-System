package com.example.ams.Navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.ams.MainPages.NewCourse
import com.example.ams.MainPages.NewDataPage.AddNewStudent

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
        composable(route = Screen.NewStudent.route) {
            AddNewStudent(navHostController = navHostController)
        }
    }
}