package com.example.ams.MainPages.NewDataPage

import android.graphics.Bitmap
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.ams.R

@Composable
fun AddNewStudent(navHostController: NavHostController) {
    val bungeeStyle = FontFamily(Font(R.font.bungee))
    val addIcon = painterResource(id = R.drawable.add_icon)
    var name by remember { mutableStateOf("") }
    var registerNo by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf<String>("") }
    var images = mutableListOf<Bitmap>()
    val cameraLauncher = rememberLauncherForActivityResult(contract = ActivityResultContracts.TakePicturePreview()) {
        if (it != null) {
            images.add(it)
        }
    }

    Column(
        Modifier
            .fillMaxSize()
            .padding(20.dp)) {
        Text(
            text = "Add New student",
            fontFamily = bungeeStyle,
            fontSize = 25.sp,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
        Text(text = "Name", fontFamily = bungeeStyle, fontSize = 15.sp)
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.textFieldColors(
                unfocusedIndicatorColor = Color.Black,
                focusedIndicatorColor = Color.Black,
                cursorColor = Color.Black,
                textColor = Color.Black,
                placeholderColor = Color.Black
            )
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(text = "Register", fontFamily = bungeeStyle, fontSize = 15.sp)
        OutlinedTextField(
            value = registerNo,
            onValueChange = { registerNo = it },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.textFieldColors(
                unfocusedIndicatorColor = Color.Black,
                focusedIndicatorColor = Color.Black,
                cursorColor = Color.Black,
                textColor = Color.Black,
                placeholderColor = Color.Black
            )
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(text = "Phone", fontFamily = bungeeStyle, fontSize = 15.sp)
        OutlinedTextField(
            value = phone,
            onValueChange = { phone = it },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.textFieldColors(
                unfocusedIndicatorColor = Color.Black,
                focusedIndicatorColor = Color.Black,
                cursorColor = Color.Black,
                textColor = Color.Black,
                placeholderColor = Color.Black
            )
        )
        Spacer(modifier = Modifier.height(20.dp))
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Text(text = "Add images of student", fontFamily = bungeeStyle, fontSize = 15.sp)
            Spacer(modifier = Modifier.weight(1f))
            IconButton(onClick = { cameraLauncher.launch() }) {
                Image(painter = addIcon, contentDescription = "", Modifier.size(40.dp))
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
        LazyColumn() {
            items(items = images) {
                Spacer(modifier = Modifier.width(10.dp))
                IconButton(onClick = { /*TODO*/ }) {
                    Image(painter = rememberAsyncImagePainter(it), contentDescription = "", Modifier.size(110.dp))
                }
            }
        }
        Spacer(modifier = Modifier.weight(0.5f))
        Button(
            onClick = { /*TODO*/ },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Black),
        ) {
            Text(text = "Save New student", fontFamily = bungeeStyle, color = Color.White)
        }
    }
}