package com.example.ams.MainPages

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ams.R
import com.example.ams.ViewModel.FirebaseViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.ams.Navigation.Screen

@Composable
fun ViewDetails(
    navHostController: NavHostController,
    courseName: String,
    adminId: String,
    viewModel: FirebaseViewModel = viewModel()
) {
    val newCourseData = viewModel.newCourseData.value
    if (newCourseData.name.isNullOrBlank()){
        viewModel.getCourseDetails(id = adminId, name = courseName)
    }

    val bungee = FontFamily(Font(R.font.bungee))
    var isEditEnabled by remember { mutableStateOf(false) }
    val editIcon = painterResource(id = R.drawable.edit_option)
    val backArrowIcon = painterResource(id = R.drawable.arrow_back)
    val color = if (isEditEnabled) Color(0x7ECACACA)
    else Color.Transparent



    Column(modifier = Modifier.fillMaxSize()) {

        TopAppBar(backgroundColor = Color.Black) {
            IconButton(onClick = { navHostController.navigateUp() }) {
                Image(painter = backArrowIcon, contentDescription = "")
            }
            Text(
                text = "class details",
                fontFamily = bungee,
                fontSize = 15.sp,
                color = Color.White
            )
            Spacer(modifier = Modifier.weight(0.25f))
            IconButton(onClick = { isEditEnabled = !isEditEnabled }) {
                Image(painter = editIcon, contentDescription = "")
            }
        }
        if (newCourseData?.name != null) {
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(20.dp)
            ) {

                Text(text = "Name", fontFamily = bungee, fontSize = 15.sp)
                CustomTextFeild(
                    value = newCourseData.name,
                    onValueChange = { viewModel.updateData("name", it) },
                    color = color,
                    enabled = isEditEnabled
                )
                Text(text = "Course name", fontFamily = bungee, fontSize = 15.sp)
                CustomTextFeild(
                    value = newCourseData.courseName,
                    onValueChange = { viewModel.updateData("courseName", it) },
                    color = color,
                    enabled = isEditEnabled
                )
                Text(text = "Batch year", fontFamily = bungee, fontSize = 15.sp)
                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    CustomTextFeild(
                        value = newCourseData.batchFrom,
                        onValueChange = { viewModel.updateData("batchFrom", it) },
                        color = color,
                        enabled = isEditEnabled
                    )
                    Text(text = "TO", fontFamily = bungee)
                    CustomTextFeild(
                        value = newCourseData.batchTo,
                        onValueChange = { viewModel.updateData("batchTo", it) },
                        color = color,
                        enabled = isEditEnabled
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "No. of attendace per day", fontFamily = bungee)
                    Spacer(modifier = Modifier.width(20.dp))
                    CustomTextFeild(
                        value = newCourseData.noAttendace,
                        onValueChange = { viewModel.updateData("noAttendance", it) },
                        color = color,
                        enabled = isEditEnabled
                    )
                }
                Spacer(modifier = Modifier.weight(0.9f))
                if (isEditEnabled) {
                    Button(
                        onClick = {
                            viewModel.updateCourseDetails(courseName)
                            navHostController.popBackStack()
                        },
                        modifier = Modifier
                            .height(40.dp)
                            .fillMaxWidth(),
                        contentPadding = PaddingValues(2.dp),
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color.Black)
                    ) {
                        Text(text = "update details", fontFamily = bungee, color = Color.White)
                    }
                }else{
                    Button(
                        onClick = { navHostController.navigate(Screen.ViewTeachers.passCourseName(courseName = courseName, adminId = adminId)) },
                        modifier = Modifier
                            .height(40.dp)
                            .fillMaxWidth(),
                        contentPadding = PaddingValues(2.dp),
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color.Black)
                    ) {
                        Text(text = "View all Teachers", fontFamily = bungee, color = Color.White)
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                    Button(
                        onClick = {  },
                        modifier = Modifier
                            .height(40.dp)
                            .fillMaxWidth(),
                        contentPadding = PaddingValues(2.dp),
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color.Black)
                    ) {
                        Text(text = "View all students", fontFamily = bungee, color = Color.White)
                    }
                }
            }
        } else {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .height(26.dp)
                        .width(26.dp),
                    strokeWidth = 2.dp,
                    color = Color.Black
                )
            }
        }
    }
}

