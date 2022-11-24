package com.example.ams.Login

import android.os.Bundle
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ams.R
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import java.util.concurrent.TimeUnit

class Authenticate: ComponentActivity() {

    private lateinit var method: String
    private lateinit var sentOtp: String
    private lateinit var name: String
    private lateinit var phone: String
    private lateinit var email: String
    private lateinit var password: String
//    private lateinit var image: Uri?
    private val firebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AuthPage()
        }

        method = intent.getStringExtra("method") ?: "error"
        name = intent.getStringExtra("name") ?: ""
        phone = intent.getStringExtra("phone") ?: ""
        password = intent.getStringExtra("password") ?: ""
        email = intent.getStringExtra("email") ?: ""
//        image = intent.getParcelableExtra("image")

        if (method == "phone" || method == "newAccount") {
            val options = PhoneAuthOptions.newBuilder(firebaseAuth)
                .setPhoneNumber("+91$phone")
                .setTimeout(60, TimeUnit.SECONDS)
                .setActivity(this)
                .setCallbacks(
                    object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                        override fun onVerificationCompleted(p0: PhoneAuthCredential) {

                        }

                        override fun onVerificationFailed(p0: FirebaseException) {
                            Toast.makeText(applicationContext, "Verification Failed for some reason", LENGTH_SHORT).show()
                        }

                        override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
                            Toast.makeText(applicationContext, "Verification code sent", LENGTH_SHORT).show()
                            super.onCodeSent(p0, p1)
                            sentOtp = p0
                        }
                    }
                ).build()
            PhoneAuthProvider.verifyPhoneNumber(options)
        }
    }

    fun signInWithEmailAndPassword(password: String) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    finishAffinity()
                } else {
                    Toast.makeText(this, "Please check your email or password", LENGTH_SHORT).show()
                }
            }
    }

    private fun signInWithMobile(otp: String) {
        val credentials = PhoneAuthProvider.getCredential(sentOtp, otp)
        firebaseAuth.signInWithCredential(credentials)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    finish()
                } else {
                    Toast.makeText(applicationContext, "make sure otp is correct", LENGTH_SHORT).show()
                }
            }
    }

     private fun createNewAccount(otp: String) {
         val credentials = PhoneAuthProvider.getCredential(sentOtp, otp)
         firebaseAuth.signInWithCredential(credentials)
             .addOnSuccessListener {
                 val currentUser = FirebaseAuth.getInstance().currentUser!!
                         val credential12 = EmailAuthProvider.getCredential(email, password)
                         currentUser.linkWithCredential(credential12)
                             .addOnSuccessListener {
                                 val profileUpdates = UserProfileChangeRequest.Builder()
                                     .setDisplayName(name).build()
                                 currentUser.updateProfile(profileUpdates)
                                 finishAffinity()
                             }
             }
     }

    @Composable
    fun AuthPage() {
        val bungeeFont = FontFamily(Font(R.font.bungee))
        var otpOrPassword by remember { mutableStateOf("")}
        val message = if(method == "email") { "Enter the password for account $email" }
        else { "Enter the Otp that have been sent to $phone" }

        Column(modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)) {
            Text(
                text = "Complete Your Authentication",
                fontFamily = bungeeFont,
                fontSize = 19.sp
            )
            Text(
                text = message,
                fontFamily = bungeeFont
            )
            Spacer(modifier = Modifier.height(20.dp))
            OutlinedTextField(
                value = otpOrPassword,
                onValueChange = { otpOrPassword = it },
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
                onClick = {
                          when(method) {
                              "phone" -> signInWithMobile(otpOrPassword)
                              "email" -> signInWithEmailAndPassword(otpOrPassword)
                              "newAccount" -> createNewAccount(otpOrPassword)
                          }
                },
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