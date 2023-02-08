package com.example.ams.MainPages.Notifications

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.ams.R
import com.example.ams.data.ViewModel.FirebaseViewModel
import java.time.LocalDate
import java.time.LocalTime

@Composable
fun NewNotification(
    navHostController: NavHostController,
    courseName: String,
    viewModel: FirebaseViewModel
){

    val backArrow = painterResource(id = R.drawable.arrow_back)
    val notificationData = viewModel.notificationData.value

    Column(Modifier.fillMaxSize()) {
        TopAppBar {
            IconButton(onClick = { navHostController.navigateUp() }) {
                Image(painter = backArrow, contentDescription = "")
            }
            Text(text = "New Notification", color = Color.White)
            Spacer(modifier = Modifier.weight(0.1f))
            TextButton(onClick = {
                viewModel.createNewNotification(courseName)
                navHostController.navigateUp()
            }) {
                Text(text = "Save", color = Color.White)
            }
        }

        TextField(
            value = notificationData.heading,
            onValueChange = { viewModel.updateData(name = "notificationHeading", value = it) },
            maxLines = 3,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            placeholder = { Text(text = "Title goes here") },
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = Color.Transparent,
                backgroundColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            )
        )
        Text(text = LocalDate.now().toString(), modifier = Modifier.padding(start = 15.dp))
        TextField(
            value = notificationData.discription,
            onValueChange = { viewModel.updateData(name = "notificationDiscrip", value = it) },
            placeholder = { Text(text = "Give some details about it") },
            modifier = Modifier.fillMaxSize(),
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = Color.Transparent,
                backgroundColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            )
        )
    }

}