package com.example.ams.MainPages.NewDataPage

import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.background
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.ams.R
import com.example.ams.data.ViewModel.FirebaseViewModel
import com.example.ams.MainPages.CustomTextFeild
import com.example.ams.data.ViewModel.NewOrImportCourseViewModel
import com.example.ams.ui.theme.pri
import java.io.ByteArrayOutputStream

@Composable
fun AddNewStudent(
    navHostController: NavHostController,
    courseName: String,
    adminId: String,
    viewModel: NewOrImportCourseViewModel
) {
    var i = 0
    viewModel.getTotalAtd(courseName, adminId)
    val quickSand = FontFamily(Font(R.font.quicksand_medium))
    val context = LocalContext.current
    val studentDetail = viewModel.newStudent.value
    val studentImage = viewModel.studentImages.observeAsState(initial = emptyList())
    val cameraLauncher = rememberLauncherForActivityResult(contract = ActivityResultContracts.TakePicturePreview()) {
        if (it != null) {
            val bytes = ByteArrayOutputStream()
            it.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
            val path = MediaStore.Images.Media.insertImage(context.contentResolver, it, studentDetail.name+(i++), null)
            viewModel.setStudentImage(Uri.parse(path.toString()))
        }
    }

    Column(Modifier.fillMaxSize()) {
        TopAppBar(backgroundColor = pri) {
            IconButton(onClick = { navHostController.navigateUp() }) {
                Image(
                    painter = painterResource(id = R.drawable.arrow_back),
                    contentDescription = ""
                )
            }
            Text(
                text = "Student details",
                fontFamily = quickSand,
                fontSize = 25.sp,
                color = Color.White,
                modifier = Modifier.fillMaxWidth()
            )
        }
        Column(
            Modifier
                .fillMaxSize()
                .padding(start = 20.dp, end = 20.dp, bottom = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(5.dp))
            Text(text = "Name", fontFamily = quickSand, fontSize = 17.sp)
            CustomTextFeild(
                value = studentDetail.name,
                onValueChange = { viewModel.updateData("studentName", it) }
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(text = "Register no.", fontFamily = quickSand, fontSize = 17.sp)
            CustomTextFeild(
                value = studentDetail.registerNo,
                onValueChange = { viewModel.updateData("studentRegisterNo", it) },
                isError = viewModel.checkIfRegNoIsUsed(),
                errorMessage = "Register no already in use"
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(text = "Phone no.", fontFamily = quickSand, fontSize = 17.sp)
            CustomTextFeild(
                value = studentDetail.phone,
                onValueChange = { viewModel.updateData("studentPhone", it) }
            )
            Spacer(modifier = Modifier.height(20.dp))
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Text(text = "Add images of student", fontFamily = quickSand, fontSize = 17.sp)
                Spacer(modifier = Modifier.weight(1f))
                IconButton(
                    onClick = { cameraLauncher.launch() },
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))
                        .background(pri)
                        .size(40.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.add_icon_white),
                        contentDescription = "",
                        modifier = Modifier.size(25.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 110.dp)
            ) {
                items(items = studentImage.value) {
                    Spacer(modifier = Modifier.width(10.dp))
                    Image(
                        painter = rememberAsyncImagePainter(it),
                        contentDescription = "",
                        modifier = Modifier
                            .size(110.dp)
                            .clip(RoundedCornerShape(20))
                            .padding(vertical = 5.dp)
                    )
                }
            }
        }
    }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
        ) {
            Spacer(modifier = Modifier.weight(0.5f))
            Button(
                onClick = {
                    viewModel.addStudent(courseName = courseName, adminId =  adminId)
                    navHostController.navigateUp()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(30)),
                colors = ButtonDefaults.buttonColors(backgroundColor = pri),
            ) {
                Text(text = "Save New student", fontFamily = quickSand, color = Color.White)
            }
        }
}