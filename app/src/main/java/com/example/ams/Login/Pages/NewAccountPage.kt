package com.example.ams.Login.Pages

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
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
            .padding(20.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Sign up",
            modifier = Modifier.fillMaxWidth(),
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Blue
        )
        Spacer(modifier = Modifier.height(5.dp))
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
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Left
        )
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20)),
            colors = TextFieldDefaults.textFieldColors(
                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Blue,
                cursorColor = Color.Blue,
                backgroundColor = Color.LightGray,
                textColor = Color.Black),
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            leadingIcon = {
                Image(painter = painterResource(id = R.drawable.person_white), contentDescription ="" )
            }
        )
        Text(
            text = "Phone",
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Left
        )
        OutlinedTextField(
            value = phone,
            onValueChange = { phone = it },
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20)),
            colors = TextFieldDefaults.textFieldColors(
                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Blue,
                cursorColor = Color.Blue,
                textColor = Color.Black,
            backgroundColor = Color.LightGray),
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Number
            ),
            leadingIcon = {
                Image(painter = painterResource(id = R.drawable.phone_white), contentDescription ="" )
            }
        )
        Text(
            text = "Email",
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Left
        )
        OutlinedTextField(
            value = password1,
            onValueChange = { password1 = it },
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20)),
            colors = TextFieldDefaults.textFieldColors(
                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Blue,
                cursorColor = Color.Blue,
                textColor = Color.Black,
            backgroundColor = Color.LightGray),
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Email
            ),
            leadingIcon = {
                Image(painter = painterResource(id = R.drawable.mail_white), contentDescription = "")
            }
        )
        Text(
            text = "Password",
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Left
        )
        OutlinedTextField(
            value = password2,
            onValueChange = { password2 = it },
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20)),
            colors = TextFieldDefaults.textFieldColors(
                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Blue,
                cursorColor = Color.Blue,
                textColor = Color.Black,
            backgroundColor = Color.LightGray),
            singleLine = true,
            keyboardOptions = KeyboardOptions(
              keyboardType = KeyboardType.Password
            ),
            leadingIcon = {
                Image(painter = painterResource(id = R.drawable.lock_white), contentDescription ="" )
            }
        )
        Spacer(modifier = Modifier.height(20.dp))
        Button(
            onClick = {
                if (name.isNotEmpty() && phone.isNotEmpty() && password1.isNotEmpty() && password2.isNotEmpty() && password1.isNotEmpty() ) {
                    context.startActivity(
                        Intent(context, Authenticate::class.java)
                            .putExtra("method", "newAccount")
                            .putExtra("name", name)
                            .putExtra("phone", phone)
                            .putExtra("email", password1)
                            .putExtra("password", password2)
                            .putExtra("image", image)
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20))
                .height(50.dp),
            colors = ButtonDefaults.buttonColors( backgroundColor = Color.Blue ),
        ) {
            Text(
                text = "Sign up",
                color = Color.White,
            )
        }
    }
}