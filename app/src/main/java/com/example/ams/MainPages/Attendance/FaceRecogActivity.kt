package com.example.ams.MainPages.Attendance

import android.graphics.Bitmap
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ams.data.ViewModel.FirebaseViewModel

class FaceRecogActivity: ComponentActivity()  {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val image = intent.getParcelableExtra<Bitmap>("image")!!
        setContent {
            val viewModel: FirebaseViewModel = viewModel(factory = FirebaseViewModel.Factory)
            MarkyAtdByFace(viewModel = viewModel, image = image)
        }
    }
}