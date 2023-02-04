package com.example.ams.MainPages.Attendance

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.activity.result.launch
import androidx.compose.ui.unit.sp
import com.example.ams.faceDetection.FaceDetection

@Composable
fun MarkyAtdByFace(viewModel: FirebaseViewModel, faceDetection: FaceDetection) {

    faceDetection.load("cs3")
    var bitmapImag by remember { mutableStateOf<String?>(null) }
    val bitimg  by viewModel.imageBitmap.observeAsState(initial = null)
    val cLauncher = rememberLauncherForActivityResult(contract = ActivityResultContracts.TakePicturePreview()) { bitmap ->
        if (bitmap != null) {
            viewModel.setbitimgValue(bitmap)
        }
    }
    val imageUri by  viewModel.imageUri.observeAsState(initial = null)

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        TopAppBar {
            Spacer(modifier = Modifier.width(5.dp))
            Text(text = "Mark Attendance")
        }
        if (bitimg != null){
            Image(
                painter = rememberAsyncImagePainter(bitimg),
                contentDescription = "",
                modifier = Modifier.fillMaxWidth()
            )
        }
        imageUri?.let {
                Image(
                    painter = rememberAsyncImagePainter(imageUri),
                    contentDescription = "",
                    modifier = Modifier.size(50.dp)
                )
        }
        bitmapImag?.let { Text(text = it, fontSize = 30.sp) }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(15.dp),
        verticalArrangement = Arrangement.Bottom,
    ) {
        Button(onClick = { cLauncher.launch() }) {
            Text(text = "Open Camera", fontSize = 15.sp)
        }
        Button(
            onClick = {
                faceDetection.Detect(bitimg!!){
                 bitmapImag = it
                }
                      },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(50)
        ) {
            Text(text = "Detect Image")
        }
        Button(
            onClick = {  viewModel.ImageFaceDetection(bitimg) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(50)
        ) {
            Text(text = "Detect faces")
        }
    }
}