package com.example.ams.MainPages

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ams.R
import com.example.ams.data.ViewModel.FirebaseViewModel
import androidx.navigation.NavHostController
import com.example.ams.MainPages.CustomComposes.customDropDown
import com.example.ams.Navigation.Screen
import com.example.ams.ui.theme.pri

@Composable
fun ViewDetails(
    navHostController: NavHostController,
    courseName: String,
    adminId: String,
    viewModel: FirebaseViewModel
) {
    val newCourseData = viewModel.newCourseData.value
    val totalAtd by viewModel.totalAtdSoFar.observeAsState()
    val totalStd by viewModel.totalStd.observeAsState()
    val totalTchr by viewModel.totalTchr.observeAsState()
    val adminDetail by viewModel.adminInfo.observeAsState()

    val quickSand = FontFamily(Font(R.font.quicksand_medium))
    var isEditEnabled by remember { mutableStateOf(false) }
    val editIcon = painterResource(id = R.drawable.edit_option)
    val backArrowIcon = painterResource(id = R.drawable.arrow_back)
    var isexpanded by remember { mutableStateOf(false) }
    var isexpandedTo by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize()) {

        TopAppBar(backgroundColor = pri) {
            IconButton(onClick = { navHostController.navigateUp() }) {
                Image(painter = backArrowIcon, contentDescription = "")
            }
            Text(
                text = "class details",
                fontFamily = quickSand,
                fontSize = 22.sp,
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
                    .padding(20.dp))
            {
                customFeildModel(
                    field = "Name :",
                    data = newCourseData.name,
                    isEditEnabled = isEditEnabled,
                    onclick = { viewModel.updateData("name", it) }
                )
                Spacer(modifier = Modifier.height(5.dp))
                customFeildModel(
                    field = "Course name :",
                    data = newCourseData.courseName,
                    isEditEnabled = isEditEnabled,
                    onclick = { viewModel.updateData("courseName", it) }
                )
                Spacer(modifier = Modifier.height(10.dp))
                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "Batch :  From", fontFamily = quickSand, fontSize = 17.sp)
                    Spacer(modifier = Modifier.width(10.dp))
                    customDropDown(
                        isEditEnabled = isEditEnabled,
                        expanded = isexpandedTo,
                        batch = newCourseData.batchFrom
                    ) {
                        if (isEditEnabled){
                            isexpandedTo = !isexpandedTo
                        }
                        newCourseData.batchFrom = it
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(text = "To", fontFamily = quickSand, fontSize = 17.sp)
                    Spacer(modifier = Modifier.width(10.dp))
                    customDropDown(
                        isEditEnabled = isEditEnabled,
                        expanded = isexpanded,
                        batch = newCourseData.batchTo,
                    ){
                        if (isEditEnabled){
                            isexpanded = !isexpanded
                        }
                        newCourseData.batchTo = it
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
                customFeildModel(
                    field = "No. of attendace per day :",
                    data = newCourseData.noAttendace,
                    isEditEnabled = isEditEnabled,
                    onclick = { viewModel.updateData("noAttendance", it) }
                )
                Spacer(modifier = Modifier.height(15.dp))
                customDataModel(field = "Total working days :", data = totalAtd.toString())
                Spacer(modifier = Modifier.height(15.dp))
                customDataModel(field = "Total Students :", data = totalStd.toString())
                Spacer(modifier = Modifier.height(15.dp))
                customDataModel(field = "Total No. Teachers :", data = totalTchr.toString())
                Spacer(modifier = Modifier.height(15.dp))
                Text(text = "Admin Info.", fontFamily = quickSand)
                Divider(thickness = 0.5.dp, modifier = Modifier.fillMaxWidth(), color = Color.Black)
                Spacer(modifier = Modifier.height(10.dp))
                customDataModel(field = "Name :", data = adminDetail?.name ?: "---")
                Spacer(modifier = Modifier.height(10.dp))
                customDataModel(field = "Phone :", data = adminDetail?.phone ?: "---")
                Spacer(modifier = Modifier.height(10.dp))
                customDataModel(field = "Email id :", data = adminDetail?.email ?: "---")
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
                        colors = ButtonDefaults.buttonColors(backgroundColor = pri)
                    ) {
                        Text(text = "update details", fontFamily = quickSand, color = Color.White)
                    }
                }else{
                    Button(
                        onClick = { navHostController.navigate(Screen.ViewTeachers.passCourseName(courseName = courseName, adminId = adminId)) },
                        modifier = Modifier
                            .height(40.dp)
                            .fillMaxWidth(),
                        contentPadding = PaddingValues(2.dp),
                        colors = ButtonDefaults.buttonColors(backgroundColor = pri)
                    ) {
                        Text(text = "View all Teachers", fontFamily = quickSand, color = Color.White)
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                    Button(
                        onClick = {
                            viewModel.getAllStduentData(adminId = adminId, courseName = courseName)
                            navHostController.navigate(Screen.ViewStudents.passCourseName(courseName = courseName, adminId = adminId))
                                  },
                        modifier = Modifier
                            .height(40.dp)
                            .fillMaxWidth(),
                        contentPadding = PaddingValues(2.dp),
                        colors = ButtonDefaults.buttonColors(backgroundColor = pri)
                    ) {
                        Text(text = "View all students", fontFamily = quickSand, color = Color.White)
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

@Composable
fun customDataModel(
    field: String,
    data: String
) {
    val quickSand = FontFamily(Font(R.font.quicksand_medium))
    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Text(text = field, fontFamily = quickSand, fontSize = 17.sp)
        Spacer(modifier = Modifier.width(15.dp))
        Text(text = data, fontFamily = quickSand, fontSize = 17.sp )
    }
}

@Composable
fun customFeildModel(
    field: String,
    data: String,
    isEditEnabled: Boolean,
    color: Color = if (isEditEnabled) Color(0x7ECACACA) else Color.Transparent,
    onclick: (String) -> Unit
) {
    val quickSand = FontFamily(Font(R.font.quicksand_medium))
    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Text(text = field, fontFamily = quickSand, fontSize = 17.sp)
        CustomTextFeild(
            value = data,
            onValueChange = { onclick(it) },
            color = color,
            enabled = isEditEnabled
        )
    }
}