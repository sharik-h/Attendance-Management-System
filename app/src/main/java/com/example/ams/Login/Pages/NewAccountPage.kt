package com.example.ams.Login.Pages

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.ams.Login.Authenticate
import com.example.ams.R

@Composable
fun NewAccountPage(navHostController: NavHostController) {

    val context = LocalContext.current
    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var password1 by remember { mutableStateOf("") }
    var password2 by remember { mutableStateOf("") }
    var image by remember { mutableStateOf<Uri?>(null) }
    val bungeeFont = FontFamily(Font(R.font.bungee))
    val accountImg = painterResource(id = R.drawable.account_img)
    val glauncher = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
        image = uri
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Creating a new account?",
            fontFamily = bungeeFont,
            fontSize = 25.sp
        )
        Text(
            text = "It may cost you some personal information",
            fontFamily = bungeeFont,
            fontSize = 13.sp,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Left
        )
        Spacer(modifier = Modifier.height(20.dp))
        Box(
            modifier = Modifier
                .size(150.dp)
                .clip(RoundedCornerShape(50))
                .clickable { glauncher.launch("image/*") },
        ){
            if (image == null) { 
                Image(painter = accountImg, contentDescription = "", modifier = Modifier.fillMaxSize())
            }else {
                Image(painter = rememberAsyncImagePainter(image), contentDescription = "", modifier = Modifier.fillMaxSize())
            }
        }
        Text(
            text = "Your name",
            fontFamily = bungeeFont,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Left
        )
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            placeholder = { Text(text = "Name") },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = TextFieldDefaults.textFieldColors(
                unfocusedIndicatorColor = Color.Black,
                focusedIndicatorColor = Color.Black,
                cursorColor = Color.Black,
                textColor = Color.Black,
                placeholderColor = Color.Black),
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
        )
        Text(
            text = "Phone",
            fontFamily = bungeeFont,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Left
        )
        OutlinedTextField(
            value = phone,
            onValueChange = { phone = it },
            placeholder = { Text(text = "Phone") },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = TextFieldDefaults.textFieldColors(
                unfocusedIndicatorColor = Color.Black,
                focusedIndicatorColor = Color.Black,
                cursorColor = Color.Black,
                textColor = Color.Black,
                placeholderColor = Color.Black),
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
        )
        Text(
            text = "Create a Password",
            fontFamily = bungeeFont,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Left
        )
        OutlinedTextField(
            value = password1,
            onValueChange = { password1 = it },
            placeholder = { Text(text = "Password") },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = TextFieldDefaults.textFieldColors(
                unfocusedIndicatorColor = Color.Black,
                focusedIndicatorColor = Color.Black,
                cursorColor = Color.Black,
                textColor = Color.Black,
                placeholderColor = Color.Black),
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
        )
        Text(
            text = "Confirm your password",
            fontFamily = bungeeFont,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Left
        )
        OutlinedTextField(
            value = password2,
            onValueChange = { password2 = it },
            placeholder = { Text(text = "Password") },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = TextFieldDefaults.textFieldColors(
                unfocusedIndicatorColor = Color.Black,
                focusedIndicatorColor = Color.Black,
                cursorColor = Color.Black,
                textColor = Color.Black,
                placeholderColor = Color.Black),
            singleLine = true
        )
        Spacer(modifier = Modifier.weight(1f))
        Button(
            onClick = {
                if (name.isNotEmpty() && phone.isNotEmpty() && password1.isNotEmpty() && password1 == password2 ) {
                    context.startActivity(
                        Intent(context, Authenticate::class.java)
                            .putExtra("name", name)
                            .putExtra("phone", phone)
                            .putExtra("password", password2)
                            .putExtra("image", image)
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors( backgroundColor = Color.Black ),
        ) {
            Text(
                text = "Create account",
                color = Color.White,
                fontFamily = bungeeFont
            )
        }
    }
}