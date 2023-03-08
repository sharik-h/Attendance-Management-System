package com.example.ams.MainPages.Notifications

import androidx.compose.foundation.Image
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.ams.R
import com.example.ams.data.ViewModel.FirebaseViewModel
import java.time.LocalDate
import com.example.ams.ui.theme.pri

@Composable
fun NewNotification(
    navHostController: NavHostController,
    courseName: String,
    viewModel: FirebaseViewModel
){
    val quickSand = FontFamily(Font(R.font.quicksand_medium))
    val notificationData = viewModel.notificationData.value

    Column(Modifier.fillMaxSize()) {
        TopAppBar(backgroundColor = pri) {
            IconButton(onClick = { navHostController.navigateUp() }) {
                Image(painter = painterResource(id = R.drawable.arrow_back), contentDescription = "")
            }
            Text(
                text = "New Notification",
                color = Color.White,
                fontSize = 20.sp,
                fontFamily = quickSand
            )
            Spacer(modifier = Modifier.weight(0.1f))
            TextButton(onClick = {
                if (notificationData.notificationId == "") {
                    viewModel.createNewNotification(courseName)
                }else{
                    viewModel.updateNotificatoin(courseName)
                }
                navHostController.navigateUp()
            }) {
                Text(
                    text = "Save",
                    color = Color.White,
                    fontFamily = quickSand,
                    fontSize = 20.sp
                )
            }
        }

        TextField(
            value = notificationData.heading,
            onValueChange = { viewModel.updateData(name = "notificationHeading", value = it) },
            maxLines = 3,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            placeholder = { Text(text = "Title goes here", color = Color.Gray, fontFamily = quickSand) },
            textStyle = TextStyle(fontFamily = quickSand),
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = Color.Transparent,
                backgroundColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            )
        )
        Text(
            text = LocalDate.now().toString(),
            modifier = Modifier.padding(start = 15.dp),
            fontFamily = quickSand,
            fontSize = 17.sp
        )
        TextField(
            value = notificationData.discription,
            onValueChange = { viewModel.updateData(name = "notificationDiscrip", value = it) },
            placeholder = { Text(text = "Give some details about it", color = Color.Gray, fontFamily = quickSand) },
            modifier = Modifier.fillMaxSize(),
            textStyle = TextStyle(fontFamily = quickSand),
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = Color.Transparent,
                backgroundColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            )
        )
    }
}