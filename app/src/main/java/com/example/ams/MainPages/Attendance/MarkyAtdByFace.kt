package com.example.ams.MainPages.Attendance

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.ams.data.ViewModel.FirebaseViewModel
import androidx.activity.result.launch
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.sp
import com.example.ams.R
import com.example.ams.faceDetection.FaceDetection

@Composable
fun MarkyAtdByFace(
    context: Activity,
    viewModel: FirebaseViewModel,
    faceDetection: FaceDetection,
    courseName: String?,
    adminId: String,
    size: Int
) {

    faceDetection.load(courseName!!)
    val detectedStd by viewModel.studentList.observeAsState(initial = emptyList())
    val bitimg  by viewModel.imageBitmap.observeAsState(initial = null)
    val cLauncher = rememberLauncherForActivityResult(contract = ActivityResultContracts.TakePicturePreview()) { bitmap ->
        if (bitmap != null) {
            viewModel.setbitimgValue(viewModel.resizeBitmapByHeight(bitmap, 360))
            if (faceDetection.imageData != null)
                faceDetection.Detect(bitmap){
                    viewModel.saveDetectedStudents(it)
                    viewModel.addAtdData(it)
                }
        }
    }
    val cameraIcon = painterResource(id = R.drawable.camera_icon_grey)
    val backIcon = painterResource(id = R.drawable.arrow_back)


    Column(Modifier.fillMaxSize()) {

        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            TopAppBar {
                Spacer(modifier = Modifier.width(5.dp))
                IconButton(onClick = { }) {
                    Image(painter = backIcon, contentDescription = "")
                }
                Text(text = "Mark Attendance")
            }
            Column(modifier = Modifier
                .fillMaxWidth()
                .height(190.dp)
                .background(Color(0xC49B9B9B))
            ) {
                if (bitimg != null){
                    Image(
                        painter = rememberAsyncImagePainter(bitimg),
                        contentDescription = "",
                        modifier = Modifier.fillMaxWidth()
                    )
                }else{
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .clickable {
                                cLauncher.launch()
                            },
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = cameraIcon,
                            contentDescription = "",
                            Modifier.size(40.dp)
                        )
                        Text(text = "Take Picture")
                    }
                }
            }
            LazyColumn {
                items(items = detectedStd) {
                    ListView(name = it) {
                    }
                }
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
            onClick = {
                viewModel.markAttendance(adminId = adminId, courseName = courseName, size = size?.toInt() ?: 0)
                context.finish()
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(50)
        ) {
            Text(text = "Mark Attendance")
        }
    }
}

@Composable
fun ListView(name: String, onClick: () -> Unit) {
    var checked by remember { mutableStateOf(true) }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(36.dp)
            .padding(top = 0.dp)
            .clickable {
                checked = !checked
                onClick()
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(modifier = Modifier.width(5.dp))
        Text(
            text = name,
            fontSize = 20.sp,
            modifier = Modifier
                .weight(0.7f)
                .padding(top = 10.dp)
        )
        Spacer(modifier = Modifier.width(5.dp) )
        Checkbox(
            checked = checked,
            colors = CheckboxDefaults.colors(uncheckedColor = Color.Black),
            onCheckedChange = {
                checked = !checked
                onClick()
            }
        )
    }
}