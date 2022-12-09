package com.example.ams.MainPages.NewDataPage

import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
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
import com.example.ams.ViewModel.FirebaseViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ams.MainPages.CustomTextFeild
import java.io.ByteArrayOutputStream

@Composable
fun AddNewStudent(
    navHostController: NavHostController,
    courseName: String,
    adminId: String,
    viewModel: FirebaseViewModel = viewModel()
) {
    var i = 0
    val bungeeStyle = FontFamily(Font(R.font.bungee))
    val addIcon = painterResource(id = R.drawable.add_icon_black)
    val context = LocalContext.current
    val studentDetail = viewModel.newStudent.value
    val cameraLauncher = rememberLauncherForActivityResult(contract = ActivityResultContracts.TakePicturePreview()) {
        if (it != null) {
            val bytes = ByteArrayOutputStream()
            it.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
            val path = MediaStore.Images.Media.insertImage(context.contentResolver, it, studentDetail.name+(i++), null)
            studentDetail.images.add(Uri.parse(path.toString()))
        }
    }

    Column(
        Modifier
            .fillMaxSize()
            .padding(start = 20.dp, end = 20.dp, bottom = 20.dp)
            .verticalScroll(rememberScrollState()) ) {
        Text(
            text = "Student details",
            fontFamily = bungeeStyle,
            fontSize = 25.sp,
            modifier = Modifier.fillMaxWidth()
        )
        Text(text = "Name", fontFamily = bungeeStyle, fontSize = 15.sp )
        CustomTextFeild(
            value = studentDetail.name,
            onValueChange = { viewModel.updateData("studentName", it) }
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(text = "Register no.", fontFamily = bungeeStyle, fontSize = 15.sp)
        CustomTextFeild(
            value = studentDetail.registerNo,
            onValueChange = { viewModel.updateData("studentRegisterNo", it) }
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(text = "Phone no.", fontFamily = bungeeStyle, fontSize = 15.sp)
        CustomTextFeild(
            value = studentDetail.phone,
            onValueChange = { viewModel.updateData("studentPhone", it) }
        )
        Spacer(modifier = Modifier.height(20.dp))
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Text(text = "Add images of student", fontFamily = bungeeStyle, fontSize = 15.sp)
            Spacer(modifier = Modifier.weight(1f))
            IconButton(onClick = { cameraLauncher.launch() }) {
                Icon(painter = addIcon, contentDescription = "", Modifier.size(40.dp))
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
        LazyRow(reverseLayout = true) {
            items(items = studentDetail.images) {
                Spacer(modifier = Modifier.width(10.dp))
                Image(
                    painter = rememberAsyncImagePainter(it),
                    contentDescription = "",
                    modifier = Modifier
                        .size(110.dp)
                        .clip(RoundedCornerShape(20))
                )
            }
        }
        Spacer(modifier = Modifier.weight(0.5f))
        Button(
            onClick = {
                viewModel.addStudent(courseName = courseName, adminId =  adminId)
                navHostController.navigateUp()
                      },
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20)),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Black),
        ) {
            Text(text = "Save New student", fontFamily = bungeeStyle, color = Color.White)
        }
    }
}