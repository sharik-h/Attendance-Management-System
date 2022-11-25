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

    val arrowBackIcon = painterResource(id = R.drawable.arrow_back)
    val moreOptionIcon = painterResource(id = R.drawable.option_icon)
    val bungee = FontFamily(Font(R.font.bungee))

 Column(modifier = Modifier.fillMaxSize()) {
     TopAppBar {
         IconButton(onClick = { navHostController.navigateUp() }) {
             Icon(painter = arrowBackIcon, contentDescription = "")
         }
         Text(text = courseName, fontFamily = bungee, color = Color.White)
         Spacer(modifier = Modifier.weight(0.5f))
         IconButton(onClick = { navHostController.navigate(Screen.Notifications.route) }) {
             Icon(painter = moreOptionIcon, contentDescription = "")
         }
     }
     Row(
         Modifier
             .fillMaxWidth()
             .padding(horizontal = 10.dp, vertical = 5.dp)
     ) {
         Button(
             onClick = { navHostController.navigate(Screen.NewStudent.passCourseName(courseName = courseName, adminId = adminId)) },
             modifier = Modifier.weight(0.5f),
             colors = ButtonDefaults.buttonColors(backgroundColor = Color.Black),
         ) {
             Text(text = "add std", fontFamily = bungee, color = Color.White)
         }
         Spacer(modifier = Modifier.width(10.dp))
         Button(
             onClick = { /*TODO*/ },
             modifier = Modifier.weight(0.5f),
             colors = ButtonDefaults.buttonColors(backgroundColor = Color.Black),
         ) {
             Text(text = "Take atd", fontFamily = bungee, color = Color.White)
         }
     }
     Row(
         Modifier
             .fillMaxWidth()
             .padding(horizontal = 10.dp, vertical = 5.dp)
     ) {
         Button(
             onClick = { /*TODO*/ },
             modifier = Modifier.weight(0.5f),
             colors = ButtonDefaults.buttonColors(backgroundColor = Color.Black),
         ) {
             Text(text = "view details", fontFamily = bungee, color = Color.White)
         }
         Spacer(modifier = Modifier.width(10.dp))
         Button(
             onClick = { /*TODO*/ },
             modifier = Modifier.weight(0.5f),
             colors = ButtonDefaults.buttonColors(backgroundColor = Color.Black),
         ) {
             Text(text = "Edit atd/cls", fontFamily = bungee, color = Color.White)
         }
     }
     Spacer(modifier = Modifier.height(10.dp))
     Divider(thickness = 1.dp, color = Color.Black)
     Column(
         Modifier
             .fillMaxWidth()
             .height(30.dp)
     ) {
         Row(Modifier.fillMaxWidth()
         ) {
             Row(
                 Modifier
                     .weight(0.7f)
                     .padding(start = 10.dp)
             ) {
                 Text(text = "Name", fontSize = 20.sp)
             }
             for (i in 1..attendanceDetail.size){
                 Row(Modifier.weight(0.1f)) {
                     Divider(modifier = Modifier
                         .width(3.dp)
                         .fillMaxHeight(), color = Color.Black)
                     Spacer(modifier = Modifier.width(9.dp) )
                     Text(text = "$i", fontSize = 20.sp)
                 }
             }
         }
     }
     Divider(thickness = 1.dp, color = Color.Black)
     LazyColumn {
         items(items = attendanceDetail){
             StudentAttendance(attendance = it)
         }
     }
 }
}

@Composable
fun StudentAttendance(attendance: AttendceDetail) {
    val chekMarkImg = painterResource(id = R.drawable.check_mark)
    val closeMarkImg = painterResource(id = R.drawable.close_mark)

    Column(
       modifier = Modifier.fillMaxWidth().height(36.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                Modifier
                    .weight(0.7f)
                    .padding(start = 10.dp)
            ) {
                Text(text = attendance.registerNo, fontSize = 20.sp)
            }
            attendance.attendance!!.forEach {
                Row(Modifier.weight(0.1f)) {
                    Divider(modifier = Modifier
                        .width(3.dp)
                        .fillMaxHeight(), color = Color.Black)
                    Spacer(modifier = Modifier.width(9.dp) )
                    if (it.second){
                        Image(painter = chekMarkImg, contentDescription = "")
                    }else {
                        Image(painter = closeMarkImg, contentDescription = "")
                    }
                }
            }
        }
    }
    Divider(thickness = 1.dp, color = Color.Black)
}