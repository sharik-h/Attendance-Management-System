package com.example.ams.MainPages

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.ams.R
import com.example.ams.ViewModel.FirebaseViewModel

@Composable
fun NewCourse(
    navHostController: NavHostController,
    viewModel: FirebaseViewModel
) {
    val bungeeStyle = FontFamily(Font(R.font.bungee))
    val newCourseData = viewModel.newCourseData.value

    Column(
        Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp)
            .verticalScroll(rememberScrollState())) {
        Text(
            text = "Create New course",
            fontFamily = bungeeStyle,
            fontSize = 30.sp,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
        Text( text = "Name", fontFamily = bungeeStyle, fontSize = 15.sp)
        CustomTextFeild(
            value = newCourseData.name,
            onValueChange = { viewModel.updateData("name", it) },
            isError = viewModel.checkIfNameIsUsed(),
            errorMessage = "You cannot take this name, It is already taken"
        )
        Text(text = "Course name", fontFamily = bungeeStyle, fontSize = 15.sp)
        CustomTextFeild(
            value = newCourseData.courseName,
            onValueChange = { viewModel.updateData("courseName", it) })
        Text(text = "Batch year", fontFamily = bungeeStyle, fontSize = 15.sp)
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
           CustomTextFeild(
               value = newCourseData.batchFrom,
               onValueChange = { viewModel.updateData("batchFrom", it) })
            Text(text = "TO", fontFamily = bungeeStyle)
            CustomTextFeild(
                value = newCourseData.batchTo,
                onValueChange = { viewModel.updateData("batchTo", it) })
        }
        Spacer(modifier = Modifier.height(10.dp))
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Text(text = "No. of attendace per day", fontFamily = bungeeStyle)
            Spacer(modifier = Modifier.width(20.dp))
            CustomTextFeild(
                value = newCourseData.noAttendace,
                onValueChange = { viewModel.updateData("noAttendance", it) })
        }
        Spacer(modifier = Modifier.weight(0.9f))
        Button(
            onClick = { if(viewModel.checkAndCreateClass()) navHostController.popBackStack() },
            modifier = Modifier
                .height(40.dp)
                .fillMaxWidth(),
            contentPadding = PaddingValues(2.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Black)
        ) {
            Text(text = "Save Course", fontFamily = bungeeStyle, color = Color.White)
        }
        Spacer(modifier = Modifier.weight(0.1f))
    }
}