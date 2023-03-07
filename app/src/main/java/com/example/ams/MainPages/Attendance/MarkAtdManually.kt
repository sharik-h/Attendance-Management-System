package com.example.ams.MainPages

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.ams.R
import com.example.ams.data.ViewModel.FirebaseViewModel
import com.example.ams.ui.theme.pri

@Composable
fun MarkAtdManually(
    navHostController: NavHostController,
    courseName: String,
    adminId: String,
    viewModel: FirebaseViewModel
) {
    viewModel.getPeriodNo(adminId = adminId, courseName = courseName)
    viewModel.getTotalAtd(adminId = adminId, courseName = courseName)
    viewModel.getAllStudents(adminId = adminId, courseName = courseName)
    val studentDetail by viewModel.studentList.observeAsState(initial = emptyList())
    val quickSand = FontFamily(Font(R.font.quicksand_medium))
    var even by remember { mutableStateOf(false) }

    Column(Modifier.fillMaxSize()) {
        TopAppBar(backgroundColor = pri) {
            IconButton(onClick = { navHostController.navigateUp() }) {
                Image(painter = painterResource(id = R.drawable.arrow_back), contentDescription = "")
            }
            Text(
                text = "Mark Attendance",
                fontFamily = quickSand,
                color = White,
                fontSize = 20.sp
            )
        }
        LazyColumn {
            items(items = studentDetail){
                StudentAttendanceMarker(name = it, even = even){ viewModel.addAtdData(it) }
                even = !even
            }
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.End
    ) {
        FloatingActionButton(
            onClick = {
                viewModel.markAttendance(adminId = adminId, courseName = courseName)
                navHostController.navigateUp() },
            backgroundColor = pri
        ) {
            Image(painter = painterResource(id = R.drawable.check_mark_white), contentDescription = "")
        }
    }
}

@Composable
fun StudentAttendanceMarker(name: String, even: Boolean, onClick: () -> Unit) {
    var checked by remember { mutableStateOf(false) }
    val quickSand = FontFamily(Font(R.font.quicksand_medium))
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(36.dp)
            .background(if (even) Color(0xFFEEEEEE) else White)
            .padding(start = 10.dp)
            .clickable {
                checked = !checked
                onClick()
            },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = name,
            fontSize = 20.sp,
            fontFamily = quickSand,
            modifier = Modifier.weight(0.7f)
        )
        Spacer(modifier = Modifier.width(5.dp) )
        Checkbox(
            checked = checked,
            onCheckedChange = {
                checked = !checked
                onClick() },
            colors = CheckboxDefaults.colors(uncheckedColor = Black)
        )
    }
}