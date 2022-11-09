package com.example.ams

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.example.ams.MainNavigation.MainNavgraph
import com.example.ams.ui.theme.AMSTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navHostController = rememberNavController()
            AMSTheme {
               MainNavgraph(navHostController = navHostController)
            }
        }
    }
}
