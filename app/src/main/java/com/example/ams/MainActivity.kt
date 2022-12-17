package com.example.ams

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.ams.MainNavigation.MainNavgraph
import com.example.ams.ViewModel.FirebaseViewModel
import com.example.ams.ui.theme.AMSTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        setContent {
            val viewModel: FirebaseViewModel = viewModel(factory = FirebaseViewModel.Factory)
            val navHostController = rememberNavController()
            AMSTheme {
               MainNavgraph(navHostController = navHostController, viewModel)
            }
        }
    }
}
