package com.example.ams.MainNavigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.example.ams.Navigation.*
import com.example.ams.ViewModel.FirebaseViewModel

@Composable
fun MainNavgraph(navHostController: NavHostController, viewModel: FirebaseViewModel) {
    NavHost(
        navController = navHostController,
        startDestination = HOME_ROUTE,
        route = MAIN_ROUTE
    ) {
        AuthNavGraph(navHostController = navHostController)
        HomeNavGraph(navHostController = navHostController, viewModel)
        NewCourseNavGraph(navHostController = navHostController, viewModel)
    }
}
