package com.example.ams.Login.Pages

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.isDigitsOnly
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImagePainter.State.Empty.painter
import com.example.ams.Login.Authenticate
import com.example.ams.Navigation.Screen
import com.example.ams.R



@Composable
fun LoginPage(navHostController: NavHostController) {

    var name by remember { mutableStateOf("") }
    val context = LocalContext.current
    var method = "error"
    var rmbrMe by remember { mutableStateOf(false) }
    var password by remember { mutableStateOf("") }

    Column(Modifier.fillMaxSize()) {
        Spacer(modifier = Modifier.height(30.dp))
        Text(
            text = "Login",
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Blue
        )
        Spacer(modifier = Modifier.height(30.dp))
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 30.dp))
        {
            Text(text = "Email", color = Color.Gray)
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = { 
                    Image(painter = painterResource(id = R.drawable.mail_white), contentDescription = "")
                },
                colors = TextFieldDefaults.textFieldColors(
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Blue,
                    backgroundColor = Color.LightGray,
                    cursorColor = Color.Blue
                )
            )
            Spacer(modifier = Modifier.height(30.dp))
            Text(text = "Password", color = Color.Gray)
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = {
                    Image(painter = painterResource(id = R.drawable.lock_white), contentDescription = "")
                },
                colors = TextFieldDefaults.textFieldColors(
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Blue,
                    backgroundColor = Color.LightGray,
                    cursorColor = Color.Blue
                )
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = rmbrMe,
                    onCheckedChange = { rmbrMe = !rmbrMe },
                    colors = CheckboxDefaults.colors(
                        checkedColor = Color.Transparent,
                        uncheckedColor = Color.Gray
                    )
                )
                Text(text = "Remember me", color = Color.Blue)
            }
            Spacer(modifier = Modifier.height(30.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 30.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Button(
                    onClick = {
                        if(name.length == 10 && name.isDigitsOnly()) { method = "phone" }
                        else if( name.split("@").contains("gmail.com")){ method = "email" }
                        if (method != "error") {
                            context.startActivity(
                                Intent(context, Authenticate::class.java)
                                    .putExtra("method", method)
                                    .putExtra("email", name)
                                    .putExtra("phone", name))
                        }
                              },
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Blue),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Login", color = Color.White)
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 30.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Don't have an account?", color = Color.DarkGray)
                TextButton(onClick = { navHostController.navigate(Screen.NewAccountPage.route)  }) {
                    Text(text = "Sign up", color = Color.Blue)
                }

            }
        }

    }

}
@Preview(showBackground = true)
@Composable
fun Preview() {
    val navHostController = rememberNavController()
    LoginPage(navHostController = navHostController)
}