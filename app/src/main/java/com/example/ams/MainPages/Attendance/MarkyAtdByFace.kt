package com.example.ams.MainPages.Attendance

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.ams.data.ViewModel.FirebaseViewModel

@Composable
fun MarkyAtdByFace(viewModel: FirebaseViewModel, image: Bitmap) {

    val imageUri by  viewModel.imageUri.observeAsState(initial = null)

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        TopAppBar {
            Spacer(modifier = Modifier.width(5.dp))
            Text(text = "Mark Attendance")
        }
        Image(
            painter = rememberAsyncImagePainter(image),
            contentDescription = "",
            modifier = Modifier.fillMaxWidth()
        )
        imageUri?.let {
            for (i in it) {
                Image(
                    painter = rememberAsyncImagePainter(i),
                    contentDescription = "",
                    modifier = Modifier.size(50.dp)
                )
            }
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(15.dp),
        verticalArrangement = Arrangement.Bottom,
    ) {
        Button(
            onClick = { viewModel.ImageFaceDetection(image) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(50)
        ) {
            Text(text = "Detect faces")
        }
    }
}