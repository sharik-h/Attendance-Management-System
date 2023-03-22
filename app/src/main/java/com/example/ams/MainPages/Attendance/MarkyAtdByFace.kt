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
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.sp
import com.example.ams.R
import com.example.ams.data.ViewModel.AttendanceViewModel
import com.example.ams.faceDetection.FaceDetection
import com.example.ams.ui.theme.pri
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun MarkyAtdByFace(
    context: Activity,
    viewModel: AttendanceViewModel,
    faceDetection: FaceDetection,
    courseName: String?,
    adminId: String,
    size: Int
) {

    faceDetection.load("$adminId/$courseName")
    viewModel.getPeriodNo(adminId = adminId, courseName = courseName!!)
    viewModel.getTotalAtd(adminId = adminId, courseName = courseName)
    viewModel.getAllStudents(adminId = adminId, courseName = courseName)
    val detectedStd by viewModel.identifiedStudents.observeAsState(initial = emptyList())
    val unDetectedStd by viewModel.unIdentifiedStudents.observeAsState(initial = emptyList())
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
    val quicksand = FontFamily(Font(R.font.quicksand_medium))
    val systemUiController = rememberSystemUiController()
    systemUiController.setStatusBarColor(color = pri)

    Column(Modifier.fillMaxSize()) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            TopAppBar(backgroundColor = pri) {
                Spacer(modifier = Modifier.width(5.dp))
                IconButton(onClick = { context.finish() }) {
                    Image(
                        painter = painterResource(id = R.drawable.arrow_back),
                        contentDescription = ""
                    )
                }
                Text(
                    text = "Mark Attendance",
                    fontFamily = quicksand,
                    color = Color.White,
                    fontSize = 20.sp
                )
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
                            painter = painterResource(id = R.drawable.camera_icon_grey),
                            contentDescription = "",
                            Modifier.size(40.dp)
                        )
                        Text(text = "Take Picture")
                    }
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 10.dp),
                horizontalArrangement = Arrangement.End
            ) {
                if (!faceDetection.isImageDataEmpty.observeAsState().value!!){
                    Text(
                        text = "Model Training",
                        fontFamily = quicksand,
                        fontSize = 15.sp
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    CircularProgressIndicator(
                        color = Color.Blue,
                        strokeWidth = 2.dp,
                        modifier = Modifier
                            .size(15.dp)
                            .padding(top = 5.dp)
                    )
                }else{
                    Text(
                        text = "Model Trained successfully",
                        fontFamily = quicksand,
                        fontSize = 15.sp
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Image(
                        painter = painterResource(id = R.drawable.check_green),
                        contentDescription = ""
                    )
                }
            }
            Text(
                text = "Identified",
                fontFamily = quicksand,
                modifier = Modifier.padding(top = 10.dp, start = 10.dp)
            )
            LazyColumn {
                items(items = detectedStd) {
                    var even by remember { mutableStateOf(true) }
                    ListView(name = it, selected = true, even = even) {
                        viewModel.addAtdData(it)
                    }
                    even = !even
                }
            }
            Text(
                text = "un-Identified",
                fontFamily = quicksand,
                modifier = Modifier.padding(top = 10.dp, start = 10.dp)
            )
            LazyColumn {
                items(items = unDetectedStd) {
                    var even by remember { mutableStateOf(true) }
                    ListView(name = it, selected = false, even = even) {
                        viewModel.addAtdData(it)
                    }
                    even = !even
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
                viewModel.markAttendance(adminId = adminId, courseName = courseName)
                context.finish()
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(30),
            colors = ButtonDefaults.buttonColors(backgroundColor = pri)
        ) {
            Text(
                text = "Mark Attendance",
                fontSize = 15.sp,
                fontFamily = quicksand,
                color = Color.White
            )
        }
    }
}

@Composable
fun ListView(
    name: String,
    selected: Boolean,
    even: Boolean,
    onClick: () -> Unit
) {
    val quicksand = FontFamily(Font(R.font.quicksand_medium))
    var checked by remember { mutableStateOf(selected) }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(36.dp)
            .background(if (even) Color(0xFFEEEEEE) else Color.White)
            .clickable {
                checked = !checked
                onClick()
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = name,
            fontSize = 18.sp,
            fontFamily = quicksand,
            modifier = Modifier
                .weight(0.7f)
                .padding(start = 10.dp)
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