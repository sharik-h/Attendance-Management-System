package com.example.ams.MainPages

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.ams.Navigation.Screen
import com.example.ams.R

@Composable
fun NewCourse(navHostController: NavHostController) {
    val bungeeStyle = FontFamily(Font(R.font.bungee))
    var className by remember { mutableStateOf("") }
    var deptName by remember{ mutableStateOf("")}
    var yearFrom by remember { mutableStateOf("2022") }
    var yearTo by remember { mutableStateOf("2020")}
    var attdPerDay by remember { mutableStateOf("")}
    val tchrData  = mutableListOf("shairkh", "sharikh")
    val stdData = mutableListOf("sharikh", "sharikh")

    Column(
        Modifier
            .fillMaxSize()
            .padding(20.dp)) {
        Text(
            text = "Create New course",
            fontFamily = bungeeStyle,
            fontSize = 30.sp,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
        OutlinedTextField(
            value = className,
            onValueChange = { className = it },
            textStyle = TextStyle(fontFamily = bungeeStyle, fontSize = 30.sp),
            singleLine = true,
            colors = TextFieldDefaults.textFieldColors(
                unfocusedIndicatorColor = Color.Black,
                focusedIndicatorColor = Color.Black,
                cursorColor = Color.Black,
                textColor = Color.Black,
                placeholderColor = Color.Black
            )
        )
        Divider(Modifier.width(300.dp))
        Text(text = "Department name", fontFamily = bungeeStyle, fontSize = 15.sp)
        OutlinedTextField(
            value = deptName,
            onValueChange = { deptName = it },
            modifier = Modifier
                .fillMaxWidth()
                .height(30.dp),
            textStyle = TextStyle(fontFamily = bungeeStyle),
            colors = TextFieldDefaults.textFieldColors(
                unfocusedIndicatorColor = Color.Black,
                focusedIndicatorColor = Color.Black,
                cursorColor = Color.Black,
                textColor = Color.Black,
                placeholderColor = Color.Black
            )
        )
        Text(text = "Batch year", fontFamily = bungeeStyle, fontSize = 15.sp)
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(
                value = yearFrom,
                onValueChange = { yearFrom = it },
                modifier = Modifier.width(100.dp),
                colors = TextFieldDefaults.textFieldColors(
                    unfocusedIndicatorColor = Color.Black,
                    focusedIndicatorColor = Color.Black,
                    cursorColor = Color.Black,
                    textColor = Color.Black,
                    placeholderColor = Color.Black
                )
            )
            Spacer(modifier = Modifier.width(20.dp))
            Text(text = "TO", fontFamily = bungeeStyle)
            Spacer(modifier = Modifier.width(20.dp))
            OutlinedTextField(
                value = yearTo,
                onValueChange = { yearTo = it },
                modifier = Modifier.width(100.dp),
                colors = TextFieldDefaults.textFieldColors(
                    unfocusedIndicatorColor = Color.Black,
                    focusedIndicatorColor = Color.Black,
                    cursorColor = Color.Black,
                    textColor = Color.Black,
                    placeholderColor = Color.Black
                )
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Text(text = "No. of attendace per day", fontFamily = bungeeStyle)
            Spacer(modifier = Modifier.width(20.dp))
            OutlinedTextField(
                value = attdPerDay,
                onValueChange = { attdPerDay = it },
                modifier = Modifier.width(50.dp),
                colors = TextFieldDefaults.textFieldColors(
                    unfocusedIndicatorColor = Color.Black,
                    focusedIndicatorColor = Color.Black,
                    cursorColor = Color.Black,
                    textColor = Color.Black,
                    placeholderColor = Color.Black
                )
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        Button(
            onClick = { /*TODO*/ },
            modifier = Modifier
                .height(40.dp)
                .width(155.dp),
            contentPadding = PaddingValues(0.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Black)
        ) {
            Text(text = "Select poster", fontFamily = bungeeStyle, fontSize = 15.sp, color = Color.White)
        }
        Spacer(modifier = Modifier.height(10.dp))

        Text(text = "Teachers", fontFamily = bungeeStyle, fontSize = 15.sp)
        Divider(thickness = 1.dp)
        LazyColumn() {
            items(items = tchrData) {
                Text(text = it, fontFamily = bungeeStyle, fontSize = 15.sp)
                Divider()
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
        Button(
            onClick = { /*TODO*/ },
            modifier = Modifier
                .height(40.dp)
                .width(140.dp),
            contentPadding = PaddingValues(2.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Black)
        ) {
            Text(text = "Add Teacher", fontFamily = bungeeStyle, color = Color.White)
        }
        Text(text = "Students", fontFamily = bungeeStyle, fontSize = 15.sp)
        Divider(thickness = 1.dp)
        LazyColumn() {
            items(items = stdData) {
                Text(text = it, fontFamily = bungeeStyle, fontSize = 15.sp)

                Divider()
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
        Button(
            onClick = { navHostController.navigate(Screen.NewStudent.route) },
            modifier = Modifier
                .height(40.dp)
                .width(140.dp),
            contentPadding = PaddingValues(2.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Black)
        ) {
            Text(text = "Add student", fontFamily = bungeeStyle, color = Color.White)
        }
    }
}