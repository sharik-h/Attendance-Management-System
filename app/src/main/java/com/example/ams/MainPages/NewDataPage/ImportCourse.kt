package com.example.ams.MainPages.NewDataPage

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ams.data.ViewModel.FirebaseViewModel
import androidx.navigation.NavHostController
import com.example.ams.MainPages.CustomTextFeild
import com.example.ams.R
import com.example.ams.ui.theme.pri

@Composable
fun ImportCourse(navHostController: NavHostController  ,viewModel: FirebaseViewModel) {

    val requestData = viewModel.requestData.value
    val quickSand = FontFamily(Font(R.font.quicksand_medium))

    Column(Modifier.fillMaxSize()) {
        TopAppBar(backgroundColor = pri) {
            IconButton(onClick = { navHostController.navigateUp() }) {
                Image(painter = painterResource(id = R.drawable.arrow_back), contentDescription = "")
            }
            Text(
                text = "Import a class",
                fontFamily = quickSand,
                fontSize = 30.sp,
                color = Color.White
            )
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
        ) {
            Text(text = "Name of class", fontFamily = quickSand, fontSize = 17.sp)
            CustomTextFeild(
                value = requestData.ClassName,
                onValueChange = { viewModel.updateData("importClassName", it) }
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(text = "Admin phone", fontFamily = quickSand, fontSize = 17.sp)
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
                colors = ButtonDefaults.buttonColors(backgroundColor = pri),
            ) {
                Text(text = "Save New student", fontFamily = quickSand, color = Color.White)
            }
        }
    }
}