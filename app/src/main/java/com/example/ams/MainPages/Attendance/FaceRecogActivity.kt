package com.example.ams.MainPages.Attendance

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ams.data.ViewModel.FirebaseViewModel
import com.example.ams.faceDetection.FaceDetection
import com.example.ams.faceDetection.FaceNetModel

class FaceRecogActivity: ComponentActivity()  {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val viewModel: FirebaseViewModel = viewModel(factory = FirebaseViewModel.Factory)
            val faceNetModel = FaceNetModel(this)
            val faceDetection = FaceDetection( faceNetModel, this )
            MarkyAtdByFace(viewModel = viewModel, faceDetection = faceDetection )
        }
    }
}