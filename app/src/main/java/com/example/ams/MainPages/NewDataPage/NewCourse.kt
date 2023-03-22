package com.example.ams.MainPages

import androidx.compose.foundation.Image
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.ams.MainPages.CustomComposes.customDropDown
import com.example.ams.R
import com.example.ams.data.ViewModel.FirebaseViewModel
import com.example.ams.data.ViewModel.NewOrImportCourseViewModel
import com.example.ams.ui.theme.pri

@Composable
fun NewCourse(
    navHostController: NavHostController,
    viewModel: NewOrImportCourseViewModel
) {
    val quickSand = FontFamily(Font(R.font.quicksand_medium))
    val newCourseData = viewModel.newCourseData.value

    var isExpanded by remember { mutableStateOf(false) }
    var isExpandedTo by remember { mutableStateOf(false) }

    Column(Modifier.fillMaxSize()) {
        TopAppBar(backgroundColor = pri) {
            IconButton(onClick = { navHostController.navigateUp() }) {
                Image(painter = painterResource(id = R.drawable.arrow_back), contentDescription = "")
            }
            Text(
                text = "New class",
                fontFamily = quickSand,
                fontSize = 30.sp,
                color = Color.White
            )
        }
        Column(
            Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState())) {
            Text( text = "Name", fontFamily = quickSand, fontSize = 17.sp)
            CustomTextFeild(
                value = newCourseData.name,
                onValueChange = { viewModel.updateData("name", it) },
                isError = viewModel.checkIfNameIsUsed(),
                errorMessage = "You cannot take this name, It is already taken"
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(text = "Course name", fontFamily = quickSand, fontSize = 17.sp)
            CustomTextFeild(
                value = newCourseData.courseName,
                onValueChange = { viewModel.updateData("courseName", it) })
            Spacer(modifier = Modifier.height(20.dp))
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Text(text = "Batch :  From", fontFamily = quickSand, fontSize = 17.sp)
                Spacer(modifier = Modifier.width(10.dp))
                customDropDown(
                    isEditEnabled = true,
                    expanded = isExpandedTo,
                    batch = newCourseData.batchFrom
                ) {
                    isExpandedTo = !isExpandedTo
                    newCourseData.batchFrom = it
                }
                Spacer(modifier = Modifier.width(10.dp))
                Text(text = "To", fontFamily = quickSand, fontSize = 17.sp)
                Spacer(modifier = Modifier.width(10.dp))
                customDropDown(
                    isEditEnabled = true,
                    expanded = isExpanded,
                    batch = newCourseData.batchTo,
                ){
                    isExpanded = !isExpanded
                    newCourseData.batchTo = it
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Text(text = "No. of attendace per day", fontFamily = quickSand)
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
                colors = ButtonDefaults.buttonColors(backgroundColor = pri)
            ) {
                Text(text = "Save Course", fontFamily = quickSand, color = Color.White)
            }
            Spacer(modifier = Modifier.weight(0.1f))
        }
    }
}