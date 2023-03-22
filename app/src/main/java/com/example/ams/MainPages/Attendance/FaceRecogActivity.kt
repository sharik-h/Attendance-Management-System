package com.example.ams.MainPages.Attendance

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ams.data.ViewModel.AttendanceViewModel
import com.example.ams.data.ViewModel.FirebaseViewModel
import com.example.ams.faceDetection.FaceDetection
import com.example.ams.faceDetection.FaceNetModel

class FaceRecogActivity: ComponentActivity()  {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val courseName = intent.getStringExtra("courseName")
        val adminId = intent.getStringExtra("adminId")
        val size = intent.getIntExtra("size",1)
        setContent {
            val viewModel: FirebaseViewModel = viewModel(factory = FirebaseViewModel.Factory)
            val attendanceViewModel: AttendanceViewModel = viewModel(factory = AttendanceViewModel.Factory)
            val faceNetModel = FaceNetModel(this)
            val faceDetection = FaceDetection( faceNetModel, this )
            MarkyAtdByFace(
                context = this,
                viewModel = attendanceViewModel,
                faceDetection = faceDetection,
                courseName = courseName,
                adminId = adminId!!,
                size = size
            )
        }
    }
}