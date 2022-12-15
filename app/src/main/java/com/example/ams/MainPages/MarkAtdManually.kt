package com.example.ams.MainPages

import androidx.compose.foundation.Image
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.ams.R
import com.example.ams.ViewModel.FirebaseViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun MarkAtdManually(
    navHostController: NavHostController,
    courseName: String,
    adminId: String,
    size: Int,
    viewModel: FirebaseViewModel = viewModel()
) {
    viewModel.getAllStudents(adminId = adminId, courseName = courseName)
    val studentDetail by viewModel.studentList.observeAsState(initial = emptyList())

    val arrowBackIcon = painterResource(id = R.drawable.arrow_back)
    val checkIconWhite = painterResource(id = R.drawable.check_mark_white)

    Column(Modifier.fillMaxSize()) {
        TopAppBar {
            IconButton(onClick = { navHostController.navigateUp() }) {
                Icon(painter = arrowBackIcon, contentDescription = "")
            }
        }
        LazyColumn {
            items(items = studentDetail){
                StudentAttendanceMarker(name = it){ viewModel.addAtdData(it) }
            }
        }
    }
    Column(
        modifier = Modifier.fillMaxSize().padding(20.dp),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.End
    ) {
        FloatingActionButton(
            onClick = {
                viewModel.markAttendance(adminId = adminId, courseName = courseName, size = size)
                navHostController.navigateUp() },
            backgroundColor = Color.Black
        ) {
            Image(painter = checkIconWhite, contentDescription = "")
        }
    }
}

@Composable
fun StudentAttendanceMarker(name: String, onClick: () -> Unit) {
    var checked by remember { mutableStateOf(false) }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(36.dp)
            .padding(start = 10.dp, top = 0.dp)
            .clickable {
                checked = !checked
                onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = name, fontSize = 20.sp, modifier = Modifier
            .weight(0.7f)
            .padding(top = 10.dp))
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