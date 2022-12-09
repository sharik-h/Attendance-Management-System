package com.example.ams.MainPages

import androidx.compose.foundation.Image
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
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.ams.Navigation.Screen
import com.example.ams.R
import com.example.ams.ViewModel.FirebaseViewModel
import com.example.ams.data.AttendceDetail

@Composable
fun ViewCourse(
    navHostController: NavHostController,
    courseName: String,
    adminId: String,
    viewModel: FirebaseViewModel = viewModel()
) {
    viewModel.getStudentAtdDetails(courseName, adminId)
    val attendanceDetail by viewModel.attendanceDetail.observeAsState(initial = emptyList())
    var size = 0
    if (!attendanceDetail.isNullOrEmpty()) { size = attendanceDetail[0].attendance.size }
    val arrowBackIcon = painterResource(id = R.drawable.arrow_back)
    val addIconWhite = painterResource(id = R.drawable.add_icon_white)
    val notificationIcon = painterResource(id = R.drawable.notification_white)
    val infoIcon = painterResource(id = R.drawable.info_white)
    val addPersonIcon = painterResource(id = R.drawable.add_person_white)
    val bungee = FontFamily(Font(R.font.bungee))

 Column(modifier = Modifier.fillMaxSize()) {
     TopAppBar {
         IconButton(onClick = { navHostController.navigateUp() }) {
             Image(painter = arrowBackIcon, contentDescription = "")
         }
         Text(text = courseName, fontFamily = bungee, color = Color.White)
         Spacer(modifier = Modifier.weight(0.5f))
         IconButton(onClick = { navHostController.navigate(Screen.ViewDetails.passCourseName(courseName = courseName, adminId = adminId)) }) {
             Image(painter = infoIcon, contentDescription = "")
         }
         IconButton(onClick = { navHostController.navigate(Screen.Notifications.route) }) {
             Image(painter = notificationIcon, contentDescription = "")
         }
         IconButton(onClick = { navHostController.navigate(Screen.NewStudent.passCourseName(courseName = courseName, adminId = adminId)) }) {
             Image(painter = addPersonIcon, contentDescription = "")
         }
     }
     Row(
         Modifier
             .fillMaxWidth()
             .height(40.dp)
             .padding(start = 10.dp, top = 5.dp)
     ) {
         Text(text = "Name", fontSize = 20.sp, modifier = Modifier.weight(0.7f))
         for (i in 1..size) {
             Row(Modifier.weight(0.1f)) {
                 Spacer(modifier = Modifier.width(9.dp))
                 Text(text = "$i", fontSize = 20.sp)
             }
         }
     }
     Divider(thickness = 1.dp, color = Color.Black)
     LazyColumn {
         items(items = attendanceDetail){
             StudentAttendance(attendance = it)
             Divider(thickness = 0.5.dp, color = Color.Black, modifier = Modifier.padding(top = 10.dp))
         }
     }
 }
    Column(
        Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.End
    ) {
        FloatingActionButton(onClick = { /*TODO*/ }, backgroundColor = Color.Black) {
            Image(painter = addIconWhite, contentDescription = "")
        }
    }
}

@Composable
fun StudentAttendance(attendance: AttendceDetail) {
    val chekMarkImg = painterResource(id = R.drawable.check_mark)
    val closeMarkImg = painterResource(id = R.drawable.close_mark)

    Row(
        modifier = Modifier.fillMaxWidth().height(36.dp)
            .padding(start = 10.dp, top = 0.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = attendance.registerNo, fontSize = 20.sp, modifier = Modifier
            .weight(0.7f)
            .padding(top = 10.dp))
        attendance.attendance!!.forEach {
            Row(Modifier.weight(0.1f)) {
                Spacer(modifier = Modifier.width(5.dp) )
                if (it.second){
                    Image(painter = chekMarkImg, contentDescription = "", modifier = Modifier.padding(top = 10.dp))
                }else {
                    Image(painter = closeMarkImg, contentDescription = "", modifier = Modifier.padding(top = 10.dp))
                }
            }
        }
    }
}