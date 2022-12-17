package com.example.ams.MainPages

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.ams.data.ViewModel.FirebaseViewModel
import com.example.ams.R
import com.example.ams.data.DataClasses.TeachersList

@Composable
fun ViewTeachers(
    navHostController: NavHostController,
    courseName: String,
    adminId: String,
    viewModel: FirebaseViewModel
) {
    viewModel.fetchAllTeachersDetails(courseName = courseName, adminId = adminId)
    val teachersList by viewModel.teacherDetailsList.observeAsState(initial = emptyList())

    val backArrowImg = painterResource(id = R.drawable.arrow_back)
    val bungee = FontFamily(Font(R.font.bungee))

    Column(Modifier.fillMaxSize()) {
        TopAppBar {
            IconButton(onClick = { navHostController.navigateUp() }) {
                Image(painter = backArrowImg, contentDescription = "")
            }
            Text(text = "Teachers", fontSize = 15.sp, fontFamily = bungee, color = Color.White)
        }
        LazyColumn {
           items(items = teachersList) {
               TeacherItem(teacherDetail = it)
               Divider(thickness = 0.5.dp, color = Color.Black)
           }
        }
    }
}

@Composable
fun TeacherItem(
    teacherDetail: TeachersList,
) {
    val defaultUserImage = painterResource(id = R.drawable.account_img)
    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Image(painter = defaultUserImage, contentDescription = "", modifier = Modifier.size(80.dp))
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "name : ${teacherDetail.name}",
                fontSize = 15.sp,
                fontFamily = FontFamily.Serif,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp)
            )
            Text(
                text = "phone: ${teacherDetail.phone}",
                fontSize = 15.sp,
                fontFamily = FontFamily.Serif,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp)
            )
            Text(
                text = "email: ${teacherDetail.email}",
                fontSize = 15.sp,
                fontFamily = FontFamily.Serif,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp)
            )
        }
    }
}