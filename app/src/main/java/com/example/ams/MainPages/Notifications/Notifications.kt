package com.example.ams.MainPages

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.ams.Navigation.Screen
import com.example.ams.R
import com.example.ams.data.DataClasses.NotificationModel
import com.example.ams.data.ViewModel.NotificationRequestViewModel
import com.example.ams.data.DataClasses.RequestCourseModel
import com.example.ams.data.DataClasses.StudentRequestModel
import com.example.ams.ui.theme.pri
import me.saket.swipe.SwipeAction
import me.saket.swipe.SwipeableActionsBox

@Composable
fun Notifications(
    navHostController: NavHostController,
    viewModel: NotificationRequestViewModel,
    courseName: String,
    adminId: String
) {

    viewModel.getAllRequests(courseName = courseName)
    viewModel.getAllNotifications(courseName = courseName, adminId = adminId)
    viewModel.getStudentRequests(courseName = courseName, adminId = adminId)
    val allRequests by viewModel.allRequests.observeAsState()
    val allNotifications by viewModel.allNotification.observeAsState()
    val allStudentRequest by viewModel.studentRequest.observeAsState()
    val quickSand = FontFamily(Font(R.font.quicksand_medium))

    Column(modifier = Modifier.fillMaxSize())
    {
        TopAppBar(backgroundColor = pri) {
            IconButton(onClick = { navHostController.navigateUp() }) {
                Image(painter = painterResource(id = R.drawable.arrow_back), contentDescription = "")
            }
            Text(text = "Notifications", fontFamily = quickSand, color = Color.White, fontSize = 20.sp)
            Spacer(modifier = Modifier.weight(0.5f))
        }
        LazyColumn{
            allNotifications?.let { it->
                items(items = it){ it1->
                    NotificationItem(data = it1){
                        if (it == "edit"){
                            viewModel.updateData("notificationHeading", it1.heading)
                            viewModel.updateData("notificationDiscrip", it1.discription)
                            viewModel.updateData("notificationDate", it1.date)
                            viewModel.updateData("notificationId", it1.notificationId!!)
                            navHostController.navigate(Screen.NewNotification.passCourseName(courseName = courseName, adminId = adminId))
                        }else{
                            viewModel.deleteNotification(courseName = courseName,adminId = adminId, notificationId = it)
                        }
                    }
                    Divider(thickness = 0.5.dp, color = Color.Black)
                }
            }
            allRequests?.let {
                items(items = it) {
                    RequestModel(
                        data = it!!,
                        onAccept = { viewModel.acceptTeacher(it) },
                        onIgnore = { viewModel.ignoreTeacher(phone  = it.teacherPhone!!, courseName = courseName) }
                    )
                    Divider(thickness = 0.5.dp, color = Color.Black)
                }
            }
            allStudentRequest?.let {
                items(items = it){
                    StudentRequest(it)
                }
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
            backgroundColor = pri,
            onClick = {
                viewModel.clearData()
                navHostController.navigate(Screen.NewNotification.passCourseName(courseName = courseName, adminId = adminId))
            }
        ) {
            Image(painter = painterResource(id = R.drawable.add_icon_white), contentDescription = "")
        }
    }
}

@Composable
fun NotificationItem(data: NotificationModel, onClick: (String) -> Unit) {
    val quickSand = FontFamily(Font(R.font.quicksand_medium))
    val edit = SwipeAction(
        onSwipe = { onClick("edit") },
        icon = {Icon(
            modifier = Modifier.padding(16.dp),
            painter = painterResource(id = R.drawable.edit_option),
            contentDescription = "",
            tint = Color.White
        )},
        background = Color(0xFF03B8FF)
    )
    val delete = SwipeAction(
        onSwipe = { onClick(data.notificationId!!) },
        icon = { Icon(
            modifier = Modifier.padding(16.dp),
            painter = painterResource(id = R.drawable.delete_white),
            contentDescription = " ",
            tint = Color.White
        ) },
        background = Color.Red
    )
    SwipeableActionsBox(
        startActions = listOf(edit),
        endActions = listOf(delete),
        swipeThreshold = 200.dp
    ) {
        Column(modifier = Modifier
            .fillMaxWidth()
            .background(color = Color.White))
        {
            Column(modifier = Modifier.padding(15.dp)) {
                Text(
                    text = data.heading,
                    modifier = Modifier.fillMaxWidth(),
                    fontSize = 17.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = quickSand
                )
                Text(text = data.discription, modifier = Modifier.fillMaxWidth(), fontFamily = quickSand)
                Text(
                    text = data.date,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Right,
                    color = Color.Gray,
                    fontFamily = quickSand
                )
            }
            Divider(thickness = 1.dp, modifier = Modifier.fillMaxWidth())
        }
    }
}

@Composable
fun RequestModel(data: RequestCourseModel, onAccept : () -> Unit, onIgnore : () -> Unit  ) {
    val quickSand = FontFamily(Font(R.font.quicksand_medium))

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .size(140.dp)
    ) {
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = "Requesting to join",
            fontFamily = quickSand,
            color = Color.Black,
            modifier = Modifier.padding(start = 20.dp)
        )
        Text(
            text = "${data.teacherName} is asking to join the ${data.className}",
            fontFamily = quickSand,
            color = Color.Black,
            modifier = Modifier.padding(start = 20.dp)
        )
        Text(
            text = "Phone: ${data.teacherPhone}",
            fontFamily = quickSand,
            color = Color.Black,
            modifier = Modifier.padding(start = 20.dp)
        )

        Row(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)) {
            Button(
                onClick = { onAccept() },
                modifier = Modifier
                    .weight(0.5f)
                    .clip(RoundedCornerShape(30)),
                colors =  ButtonDefaults.buttonColors( backgroundColor = Color(0xF800CC3A))
            ) {
                Text(text = "Accept", color = Color.White)
            }
            Spacer(modifier = Modifier.width(20.dp))
            Button(
                onClick = { onIgnore() },
                modifier = Modifier
                    .weight(0.5f)
                    .clip(RoundedCornerShape(30)),
                colors =  ButtonDefaults.buttonColors( backgroundColor = Color(0xFFFF5C5C))
            ) {
                Text(text = "Ignore", color = Color.White)
            }
        }
    }
}

@Composable
fun StudentRequest(data: StudentRequestModel) {
    val quickSand = FontFamily(Font(R.font.quicksand_medium))
    Column(modifier = Modifier
        .fillMaxWidth()
        .background(color = Color.White))
    {
        Column(modifier = Modifier.padding(15.dp)) {
            Text(
                text = "Request From: ${data.name},  ${data.regNo}",
                modifier = Modifier.fillMaxWidth(),
                fontSize = 17.sp,
                fontWeight = FontWeight.SemiBold,
                fontFamily = quickSand
            )
            Text(text = data.matter, modifier = Modifier.fillMaxWidth(), fontFamily = quickSand)
            Text(
                text = data.date,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Right,
                color = Color.Gray,
                fontFamily = quickSand
            )
        }
        Divider(thickness = 0.5.dp, modifier = Modifier.fillMaxWidth(), color = Black)
    }
}