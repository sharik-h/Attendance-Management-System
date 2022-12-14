package com.example.ams.MainPages

import androidx.compose.foundation.clickable
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.ams.R
import com.example.ams.data.ViewModel.FirebaseViewModel
import com.example.ams.data.DataClasses.RequestCourseModel

@Composable
fun Notifications(navHostController: NavHostController, viewModel: FirebaseViewModel) {

    viewModel.getAllNotifications()
    val allNotifications by viewModel.allNotification.observeAsState()
    val arrowBackIcon = painterResource(id = R.drawable.arrow_back)
    val bungee = FontFamily(Font(R.font.bungee))

    Column(modifier = Modifier.fillMaxSize())
    {
        TopAppBar {
            IconButton(onClick = { navHostController.navigateUp() }) {
                Icon(painter = arrowBackIcon, contentDescription = "")
            }
            Text(text = "Notifications", fontFamily = bungee, color = Color.White)
            Spacer(modifier = Modifier.weight(0.5f))
        }
        allNotifications?.let {
            LazyColumn {
                items(items = it) {
                    NotificationModel(
                        data = it,
                        onAccept = { viewModel.acceptTeacher(it) },
                        onIgnore = { viewModel.ignoreTeacher(it.requestId) }
                    )
                    Divider(thickness = 1.dp, color = Color.Black)
                }
            }
        }
    }
}

@Composable
fun NotificationModel(data: RequestCourseModel, onAccept : () -> Unit, onIgnore : () -> Unit  ) {
    val bungeeStyle = FontFamily(Font(R.font.bungee))

    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start,
        modifier = Modifier
            .fillMaxWidth()
            .size(190.dp)
            .clickable { }
    ) {
        Text(
            text = "Request to join the class.",
            fontFamily = bungeeStyle,
            color = Color.Black,
            modifier = Modifier.padding(start = 20.dp)
        )
        Text(
            text = "${data.TeacherName} is asking to join the ${data.ClassName}",
            fontFamily = bungeeStyle,
            color = Color.Black,
            modifier = Modifier.padding(start = 20.dp)
        )
        Text(
            text = "Phone: ${data.TeacherPhone}",
            fontFamily = bungeeStyle,
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
                colors =  ButtonDefaults.buttonColors( backgroundColor = Color.Black )
            ) {
                Text(text = "Accept", color = Color.White)
            }
            Spacer(modifier = Modifier.width(20.dp))
            Button(
                onClick = { onIgnore() },
                modifier = Modifier
                    .weight(0.5f)
                    .clip(RoundedCornerShape(30)),
                colors =  ButtonDefaults.buttonColors( backgroundColor = Color.Black )
            ) {
                Text(text = "Ignore", color = Color.White)
            }
        }
    }
}