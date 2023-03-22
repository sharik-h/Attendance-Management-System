package com.example.ams.MainNavigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.example.ams.Navigation.*
import com.example.ams.data.ViewModel.AttendanceViewModel
import com.example.ams.data.ViewModel.FirebaseViewModel
import com.example.ams.data.ViewModel.NewOrImportCourseViewModel
import com.example.ams.data.ViewModel.NotificationRequestViewModel

@Composable
fun MainNavgraph(
    navHostController: NavHostController,
    viewModel: FirebaseViewModel,
    notificationRequestViewModel: NotificationRequestViewModel,
    attendanceViewModel: AttendanceViewModel,
    newOrImportCourseViewModel: NewOrImportCourseViewModel
) {
    NavHost(
        navController = navHostController,
        startDestination = HOME_ROUTE,
        route = MAIN_ROUTE
    ) {
        AuthNavGraph(navHostController = navHostController)
        HomeNavGraph(navHostController = navHostController, viewModel, notificationRequestViewModel, attendanceViewModel)
        NewCourseNavGraph(navHostController = navHostController, viewModel, newOrImportCourseViewModel)
    }
}
