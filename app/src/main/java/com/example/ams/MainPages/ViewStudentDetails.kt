package com.example.ams.MainPages.NewDataPage

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.ams.R
import com.example.ams.ViewModel.FirebaseViewModel

@Composable
fun ViewStudentDetails(
    navHostController: NavHostController,
    courseName: String,
    adminId: String,
    registerNo: String,
    viewModel: FirebaseViewModel
) {
    viewModel.getStudentDetails(courseName = courseName, adminId = adminId, registerNo = registerNo)
    val studentData = viewModel.newStudent.value

    val backArrowIcon = painterResource(id = R.drawable.arrow_back)
    val defaultUserImage = painterResource(id = R.drawable.account_img)

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar {
            IconButton(onClick = { navHostController.navigateUp() }) {
                Icon(painter = backArrowIcon, contentDescription = "")
            }
        }
        Column(
            Modifier
                .fillMaxSize()
                .padding(15.dp)) {
            Image(
                painter = defaultUserImage,
                contentDescription = "",
                modifier = Modifier.size(120.dp),
            )
            Text(text = "name")
            Text(text = studentData.name, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Text(text = "registerNo.")
            Text(text = studentData.registerNo, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Text(text = "mobiele")
            Text(text = studentData.phone, fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }
    }
}