package com.example.ams.MainPages

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.ams.R
import com.example.ams.ViewModel.FirebaseViewModel
import com.example.ams.Navigation.Screen

@Composable
fun ViewStudents(
    navHostController: NavHostController,
    adminId: String,
    courseName: String,
    viewModel: FirebaseViewModel
) {
    viewModel.getAllStudents(adminId = adminId, courseName = courseName)
    val data by viewModel.studentList.observeAsState(initial = emptyList())

    val backArrowIcon = painterResource(id = R.drawable.arrow_back)

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar {
            IconButton(onClick = { navHostController.navigateUp() }) {
                Icon(painter = backArrowIcon, contentDescription = "")
            }
        }
        LazyColumn {
            items(items = data) {
                StudentItem(name = it) {
                    navHostController.navigate(
                        Screen.ViewStudentDetails
                            .passCourseName(courseName = courseName, adminId = adminId, registerNo = it)
                    )
                }
                Divider(thickness = 0.5.dp, color = Color.Black)
            }
        }
    }
}

@Composable
fun StudentItem(name: String, onClick: () -> Unit) {
    val defaultUserImage = painterResource(id = R.drawable.account_img)
    val arrowRight = painterResource(id = R.drawable.arrow_right_black)
    Row(
        modifier = Modifier
            .padding(5.dp)
            .fillMaxWidth()
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(painter = defaultUserImage, contentDescription = "", modifier = Modifier.size(60.dp))
        Spacer(modifier = Modifier.width(20.dp))
        Text(text = name, fontSize = 18.sp)
        Spacer(modifier = Modifier.weight(0.5f))
        Icon(painter = arrowRight, contentDescription = "")
    }
}