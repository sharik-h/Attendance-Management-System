package com.example.ams.Login

import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ams.R
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import java.util.concurrent.TimeUnit

class Authenticate: ComponentActivity() {

    lateinit var sentOtp: String
    lateinit var name: String
    lateinit var phone: String
    lateinit var password: String
    lateinit var image: Uri
    val firebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AuthPage()
        }

        name = intent.getStringExtra("name") ?: ""
        phone = intent.getStringExtra("phone") ?: ""
        password = intent.getStringExtra("password") ?: ""
        image = intent.getParcelableExtra("image")!!

        val options = PhoneAuthOptions.newBuilder(firebaseAuth)
            .setPhoneNumber("+91$phone")
            .setTimeout(60, TimeUnit.SECONDS)
            .setActivity(this)
            .setCallbacks(
                object: PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    override fun onVerificationCompleted(p0: PhoneAuthCredential) {
                        Toast.makeText(applicationContext, "Verification completed", Toast.LENGTH_SHORT).show()
                    }

                    override fun onVerificationFailed(p0: FirebaseException) {
                        Toast.makeText(applicationContext, "Verification Failed for some reason", Toast.LENGTH_SHORT).show()
                    }

                    override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
                        super.onCodeSent(p0, p1)
                        sentOtp = p0
                    }
                }
            ).build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private fun signInWithCredentials(otp: String) {
        val credentials = PhoneAuthProvider.getCredential(sentOtp, otp)
        firebaseAuth.signInWithCredential(credentials)
            .addOnCompleteListener { task->
                if (task.isSuccessful){
                    val user = FirebaseAuth.getInstance().currentUser
                    val profileUpdates = UserProfileChangeRequest.Builder()
                        .setDisplayName(name)
                        .setPhotoUri(image)
                        .build()
                    user?.updateProfile(profileUpdates)
                    user?.updatePassword(password!!)
                    finish()
                }else{
                    Toast.makeText(applicationContext, "make sure otp is correct", Toast.LENGTH_SHORT).show()
                }
            }
    }

    @Preview(showBackground = true)
    @Composable
    fun AuthPage() {
        val bungeeFont = FontFamily(Font(R.font.bungee))
        var otp by remember { mutableStateOf("")}

        Column(modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)) {
            Text(
                text = "Complete Your Authentication",
                fontFamily = bungeeFont,
                fontSize = 19.sp
            )
            Text(
                text = "Enter the Otp that have been sent to $phone",
                fontFamily = bungeeFont
            )
            Spacer(modifier = Modifier.height(20.dp))
            OutlinedTextField(
                value = otp,
                onValueChange = { otp = it },
                placeholder = { Text(text = "Name")},
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = TextFieldDefaults.textFieldColors(
                    unfocusedIndicatorColor = Color.Black,
                    focusedIndicatorColor = Color.Black,
                    cursorColor = Color.Black,
                    textColor = Color.Black,
                    placeholderColor = Color.Black
                )
            )
            Spacer(modifier = Modifier.height(20.dp))
            Button(
                onClick = { signInWithCredentials(otp) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors( backgroundColor = Color.Black )  ) {
                Text(
                    text = "verify",
                    color = Color.White,
                    fontFamily = bungeeFont,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 8.dp),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}