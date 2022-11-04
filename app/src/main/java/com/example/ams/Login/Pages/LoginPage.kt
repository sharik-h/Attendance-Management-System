package com.example.ams.Login.Pages

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.ams.Login.LoginNavigation.Screen
import com.example.ams.R



@Composable
fun LoginPage(navHostController: NavHostController) {

    var name by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val bungeeFont = FontFamily(Font(R.font.bungee))

    Column(
        modifier = Modifier.fillMaxSize().padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally) {

        Text(text = "AMS", fontSize = 35.sp, fontFamily = bungeeFont)
        Text(text = "HELLO THERE, WELCOME BACK", fontSize = 35.sp, fontFamily = bungeeFont)
    }
    Column(
        modifier = Modifier.fillMaxSize().padding(20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Sign up to continue", fontSize = 18.sp, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(20.dp))
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            placeholder = { Text(text = "Name")},
            modifier = Modifier.fillMaxWidth().height(50.dp),
            colors = TextFieldDefaults.textFieldColors(
                unfocusedIndicatorColor = Color.Black,
                focusedIndicatorColor = Color.Black,
                cursorColor = Color.Black,
                textColor = Color.Black,
                placeholderColor = Color.Black
            )
        )
        Spacer(modifier = Modifier.height(10.dp))
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            placeholder = { Text(text = "Password") },
            modifier = Modifier.fillMaxWidth().height(50.dp),
            colors = TextFieldDefaults.textFieldColors(
                unfocusedIndicatorColor = Color.Black,
                focusedIndicatorColor = Color.Black,
                cursorColor = Color.Black,
                textColor = Color.Black,
                placeholderColor = Color.Black
            )
        )
        Spacer(modifier = Modifier.height(5.dp))
        TextButton(
            onClick = { /*TODO*/ },
            modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "Forgot password?",
                color = Color.Black,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.End
            )
        }
        Spacer(modifier = Modifier.height(5.dp))
        Button(
            onClick = { navHostController.navigate("mainPage") },
            modifier = Modifier.fillMaxWidth().height(50.dp),
            colors = ButtonDefaults.buttonColors( backgroundColor = Color.Black )  ) {
            Text(
                text = "GO",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxSize().padding(top = 8.dp),
                textAlign = TextAlign.Center
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        Button(
            onClick = { navHostController.navigate(Screen.NewAccountPage.route) },
            modifier = Modifier.fillMaxWidth().height(50.dp),
            colors = ButtonDefaults.buttonColors( backgroundColor = Color.White ),
        ) {
            Text(
                text = "NEW USER? SIGN UP",
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxSize().padding(top = 8.dp),
                textAlign = TextAlign.Center,
            )
        }
    }
}
@Preview(showBackground = true)
@Composable
fun Preview() {
    val navHostController = rememberNavController()
    LoginPage(navHostController = navHostController)
}