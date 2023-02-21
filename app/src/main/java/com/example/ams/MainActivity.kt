package com.example.ams

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import android.icu.util.Calendar
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.ams.MainNavigation.MainNavgraph
import com.example.ams.MainPages.Attendance.MarkDailyAtd
import com.example.ams.data.ViewModel.FirebaseViewModel
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

        // Create a new Intent for your function
        val intent = Intent(this, MarkDailyAtd::class.java).apply {
            action = "com.example.ams.ACTION_SCHEDULED_TASK"
        }

        // Create a new PendingIntent for the function
        val pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0)

        // Create a new instance of the AlarmManager class
        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager

        // Set the time for the function to run
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 4)
        calendar.set(Calendar.MINUTE, 30)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.AM_PM, Calendar.PM)

        // Schedule the function to run at the specified time
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            pendingIntent
        )
    }
}
