package com.example.ams.MainPages.NewDataPage

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ams.ViewModel.FirebaseViewModel
import androidx.navigation.NavHostController
import com.example.ams.MainPages.CustomTextFeild
import com.example.ams.R

@Composable
fun ImportCourse(navHostController: NavHostController  ,viewModel: FirebaseViewModel) {

    val requestData = viewModel.requestData.value
    val bungeeStyle = FontFamily(Font(R.font.bungee))

    Column(modifier = Modifier.fillMaxSize().padding(start = 20.dp, end = 20.dp, bottom = 20.dp)) {
        Text(text = "Import a class", fontFamily = bungeeStyle, fontSize = 25.sp)
        Text(text = "Name of class", fontFamily = bungeeStyle, fontSize = 15.sp)
        CustomTextFeild(
            value = requestData.ClassName,
            onValueChange = { viewModel.updateData("importClassName", it) }
        )
        Text(text = "creater Phone", fontFamily = bungeeStyle, fontSize = 15.sp)
        CustomTextFeild(
            value = requestData.AdminPhone,
            onValueChange = { viewModel.updateData("creatorPhoneNo", it) }
        )
        Spacer(modifier = Modifier.weight(0.5f))
        Button(
            onClick = {
                viewModel.checkRequestDetails()
                navHostController.navigateUp()
            },
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20)),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Black),
        ) {
            Text(text = "Save New student", fontFamily = bungeeStyle, color = Color.White)
        }
    }

}