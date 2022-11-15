package com.example.ams.MainPages

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ams.R

@Composable
fun ViewCourse() {

    val arrowBackIcon = painterResource(id = R.drawable.arrow_back)
    val moreOptionIcon = painterResource(id = R.drawable.option_icon)
    val bungee = FontFamily(Font(R.font.bungee))
    val noAttendancePerDay = 5
    val data = mutableListOf("", "")

 Column(modifier = Modifier.fillMaxSize()) {
     TopAppBar {
         IconButton(onClick = { /*TODO*/ }) {
             Icon(painter = arrowBackIcon, contentDescription = "")
         }
         Text(text = "Course name", fontFamily = bungee, color = Color.White)
         Spacer(modifier = Modifier.weight(0.5f))
         IconButton(onClick = { /*TODO*/ }) {
             Icon(painter = moreOptionIcon, contentDescription = "")
         }
     }
     Row(
         Modifier
             .fillMaxWidth()
             .padding(horizontal = 10.dp, vertical = 5.dp)
     ) {
         Button(
             onClick = { /*TODO*/ },
             modifier = Modifier.weight(0.5f)
         ) {
             Text(text = "add std", fontFamily = bungee)
         }
         Spacer(modifier = Modifier.width(10.dp))
         Button(
             onClick = { /*TODO*/ },
             modifier = Modifier.weight(0.5f)
         ) {
             Text(text = "Take atd", fontFamily = bungee)
         }
     }
     Row(
         Modifier
             .fillMaxWidth()
             .padding(horizontal = 10.dp, vertical = 5.dp)
     ) {
         Button(
             onClick = { /*TODO*/ },
             modifier = Modifier.weight(0.5f)
         ) {
             Text(text = "view details", fontFamily = bungee)
         }
         Spacer(modifier = Modifier.width(10.dp))
         Button(
             onClick = { /*TODO*/ },
             modifier = Modifier.weight(0.5f)
         ) {
             Text(text = "Edit atd/cls", fontFamily = bungee)
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
             for (i in 1..noAttendancePerDay) {
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
         items(items = data) {
             StudentAttendance(it)
         }
     }
 }
}

@Composable
fun StudentAttendance(name: String) {
    val chekMarkImg = painterResource(id = R.drawable.check_mark)
    val closeMarkImg = painterResource(id = R.drawable.close_mark)
    val present = true
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
                Text(text = name, fontSize = 20.sp)
            }
            for (i in 1..5) {
                Row(Modifier.weight(0.1f)) {
                    Divider(modifier = Modifier
                        .width(3.dp)
                        .fillMaxHeight(), color = Color.Black)
                    Spacer(modifier = Modifier.width(9.dp) )
                    if (present){
                        Icon(painter = chekMarkImg, contentDescription = "")
                    }else {
                        Icon(painter = closeMarkImg, contentDescription = "")
                    }
                }
            }
        }
    }
    Divider(thickness = 1.dp, color = Color.Black)
}