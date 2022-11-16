package com.example.ams.MainPages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import com.example.ams.R

@Composable
fun CustomTextFeild(value: String, onValueChange: (String) -> Unit) {
    val bungeeStyle = FontFamily(Font(R.font.bungee))
    BasicTextField(
        value = value,
        onValueChange = { onValueChange(it) },
        singleLine = true,
        textStyle = TextStyle(fontFamily = bungeeStyle),
        modifier = Modifier
            .clip(RoundedCornerShape(20))
            .background(Color(0x7ECACACA))
            .height(40.dp)
            .fillMaxWidth()
            .padding(start = 10.dp, top = 2.dp)
    )
}