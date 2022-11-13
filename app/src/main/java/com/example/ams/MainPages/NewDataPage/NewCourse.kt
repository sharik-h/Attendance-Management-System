package com.example.ams.MainPages

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.ams.R
import com.example.ams.ViewModel.FirebaseViewModel

@Composable
fun NewCourse(
    navHostController: NavHostController,
    viewModel: FirebaseViewModel = viewModel()
) {
    val bungeeStyle = FontFamily(Font(R.font.bungee))
    val newCourseData = viewModel.newCourseData.value

    Column(
        Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp)) {
        Text(
            text = "Create New course",
            fontFamily = bungeeStyle,
            fontSize = 30.sp,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
        Text( text = "Name", fontFamily = bungeeStyle, fontSize = 15.sp)
        OutlinedTextField(
            value = newCourseData.name,
            onValueChange = { viewModel.updateData("name", it) },
            textStyle = TextStyle(fontFamily = bungeeStyle, fontSize = 10.sp),
            singleLine = true,
            colors = TextFieldDefaults.textFieldColors(
                unfocusedIndicatorColor = Color.Black,
                focusedIndicatorColor = Color.Black,
                cursorColor = Color.Black,
                textColor = Color.Black,
                placeholderColor = Color.Black
            )
        )
        Text(text = "Course name", fontFamily = bungeeStyle, fontSize = 15.sp)
        OutlinedTextField(
            value = newCourseData.courseName,
            onValueChange = { viewModel.updateData("courseName", it) },
            modifier = Modifier.fillMaxWidth(),
            textStyle = TextStyle(fontFamily = bungeeStyle, fontSize = 10.sp),
            colors = TextFieldDefaults.textFieldColors(
                unfocusedIndicatorColor = Color.Black,
                focusedIndicatorColor = Color.Black,
                cursorColor = Color.Black,
                textColor = Color.Black,
                placeholderColor = Color.Black
            )
        )
        Text(text = "Batch year", fontFamily = bungeeStyle, fontSize = 15.sp)
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(
                value = newCourseData.bactchFrom,
                onValueChange = { viewModel.updateData("batchFrom", it)},
                modifier = Modifier.width(100.dp).height(60.dp),
                colors = TextFieldDefaults.textFieldColors(
                    unfocusedIndicatorColor = Color.Black,
                    focusedIndicatorColor = Color.Black,
                    cursorColor = Color.Black,
                    textColor = Color.Black,
                    placeholderColor = Color.Black
                )
            )
            Spacer(modifier = Modifier.width(20.dp))
            Text(text = "TO", fontFamily = bungeeStyle)
            Spacer(modifier = Modifier.width(20.dp))
            OutlinedTextField(
                value = newCourseData.batchTo,
                onValueChange = { viewModel.updateData("batchTo", it) },
                modifier = Modifier.width(100.dp).height(60.dp),
                colors = TextFieldDefaults.textFieldColors(
                    unfocusedIndicatorColor = Color.Black,
                    focusedIndicatorColor = Color.Black,
                    cursorColor = Color.Black,
                    textColor = Color.Black,
                    placeholderColor = Color.Black
                )
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Text(text = "No. of attendace per day", fontFamily = bungeeStyle)
            Spacer(modifier = Modifier.width(20.dp))
            OutlinedTextField(
                value = newCourseData.noAttendace,
                onValueChange = { viewModel.updateData("noAttendance", it) },
                modifier = Modifier.width(50.dp).height(60.dp),
                colors = TextFieldDefaults.textFieldColors(
                    unfocusedIndicatorColor = Color.Black,
                    focusedIndicatorColor = Color.Black,
                    cursorColor = Color.Black,
                    textColor = Color.Black,
                    placeholderColor = Color.Black
                )
            )
        }
        Spacer(modifier = Modifier.weight(0.9f))
        Button(
            onClick = {
                viewModel.createNewClass()
                navHostController.popBackStack()
                      },
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