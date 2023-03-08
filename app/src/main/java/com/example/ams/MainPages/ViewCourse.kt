package com.example.ams.MainPages

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.ams.MainPages.Attendance.FaceRecogActivity
import com.example.ams.MainPages.CustomComposes.cornerBorder
import com.example.ams.Navigation.Screen
import com.example.ams.R
import com.example.ams.data.ViewModel.FirebaseViewModel
import com.example.ams.data.DataClasses.AttendceDetail
import com.example.ams.ui.theme.pri

@Composable
fun ViewCourse(
    navHostController: NavHostController,
    courseName: String,
    adminId: String,
    viewModel: FirebaseViewModel
) {
    viewModel.getStudentAtdDetails(courseName, adminId)
    val attendanceDetail by viewModel.attendanceDetail.observeAsState(initial = emptyList())
    var selected by remember{ mutableStateOf(false) }
    var size = 0
    val context = LocalContext.current

    if (!attendanceDetail.isNullOrEmpty()) { size = attendanceDetail[0].attendance.size }
    val arrowBackIcon = painterResource(id = R.drawable.arrow_back)
    val addIconWhite = painterResource(id = R.drawable.add_icon_white)
    val notificationIcon = painterResource(id = R.drawable.notification_white)
    val infoIcon = painterResource(id = R.drawable.info_white)
    val addPersonIcon = painterResource(id = R.drawable.add_person_white)
    val checkItemsIcon = painterResource(id = R.drawable.check_item_icon_white)
    val faceRecoIcon = painterResource(id = R.drawable.face_reco_icon_white)
    val quickSand = FontFamily(Font(R.font.quicksand_medium))

 Column(modifier = Modifier.fillMaxSize()) {
     TopAppBar(backgroundColor = pri) {
         IconButton(onClick = { navHostController.navigateUp() }) {
             Image(painter = arrowBackIcon, contentDescription = "")
         }
         Text(text = courseName, fontFamily = quickSand, color = Color.White)
         Spacer(modifier = Modifier.weight(0.5f))
         IconButton(onClick = {
             viewModel.getStdRealAtd(adminId = adminId, courseName = courseName)
             navHostController.navigate(Screen.ViewAttendance.passCourseName(courseName = courseName, adminId = adminId))
         }) {
             Image(painter = checkItemsIcon, contentDescription = "")
         }
         IconButton(onClick = {
             viewModel.getCourseDetails(id = adminId, name = courseName)
             navHostController.navigate(Screen.ViewDetails.passCourseName(courseName = courseName, adminId = adminId))
         }) {
             Image(painter = infoIcon, contentDescription = "")
         }
         IconButton(onClick = { navHostController.navigate(Screen.Notifications.PassCourseName(courseName = courseName)) }) {
             Image(painter = notificationIcon, contentDescription = "")
         }
         IconButton(onClick = {
             viewModel.clearData()
             navHostController.navigate(Screen.NewStudent.passCourseName(courseName = courseName, adminId = adminId))
         }) {
             Image(painter = addPersonIcon, contentDescription = "")
         }
     }
     Row(
         Modifier
             .fillMaxWidth()
             .height(30.dp)
             .padding(start = 10.dp, top = 0.dp)
     ) {
         Text(
             text = "Reg No",
             fontSize = 20.sp,
             modifier = Modifier.weight(0.7f),
             fontFamily = quickSand
         )
         for (i in 1..size) {
//             Row(Modifier.weight(0.1f)) {
                 Box(
                     contentAlignment = Alignment.Center,
                     modifier = Modifier.width(45.dp).height(36.dp)
                 ){
                     Text(text = "$i", fontSize = 20.sp, fontFamily = quickSand)
                 }
//            }
         }
     }
     Divider(thickness = 0.9.dp, color = Color.Black)
     var even by remember{ mutableStateOf(false)}
     LazyColumn {
         items(items = attendanceDetail){
             StudentAttendance(attendance = it , even = even)
             even = !even
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
        if (selected){
            FloatingActionButton(
                onClick = { navHostController.navigate(Screen.MarkAtdManually
                    .passCourseName(courseName = courseName, adminId = adminId)) },
                backgroundColor = Color.Black,
                modifier = Modifier
                    .padding(end = 8.dp)
                    .size(45.dp)
            ) {
                Image(painter = checkItemsIcon, contentDescription = "")
            }
            Spacer(modifier = Modifier.height(10.dp))
            FloatingActionButton(onClick = {
                context.startActivity(Intent(context, FaceRecogActivity::class.java)
                    .putExtra("courseName", courseName)
                    .putExtra("adminId", adminId)
                    .putExtra("size",size)
                )
            },
                backgroundColor = Color.Black, modifier = Modifier
                    .padding(end = 8.dp)
                    .size(45.dp)
            ) {
                Image(painter = faceRecoIcon, contentDescription = "")
            }
            Spacer(modifier = Modifier.height(10.dp))
        }
        FloatingActionButton(onClick = { selected = !selected  }, backgroundColor = pri) {
            Image(painter = addIconWhite, contentDescription = "")
        }
    }
}

@Composable
fun StudentAttendance(attendance: AttendceDetail , even : Boolean) {
    val chekMarkImg = painterResource(id = R.drawable.check_green)
    val closeMarkImg = painterResource(id = R.drawable.cancel_47)
    val quickSand = FontFamily(Font(R.font.quicksand_medium))

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(36.dp)
            .background(if (even) Color(0xFFEEEEEE) else Color.White)
            .padding(start = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = attendance.registerNo,
            fontSize = 20.sp,
            fontFamily = quickSand,
            modifier = Modifier
                .weight(0.7f)
                .fillMaxHeight()
        )
        attendance.attendance!!.forEach {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .cornerBorder(1.dp, Color.Black)
                    .height(36.dp)
                    .width(45.dp)
            ) {
                if (it.second){
                    Image(painter = chekMarkImg, contentDescription = "")
                }else {
                    Image(painter = closeMarkImg, contentDescription = "")
                }
            }
        }
    }
}