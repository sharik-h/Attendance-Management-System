package com.example.ams.MainPages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
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
fun CustomTextFeild(
    value: String,
    onValueChange: (String) -> Unit,
    color: Color = Color(0x7ECACACA),
    isError: Boolean = false,
    errorMessage: String? = null,
    enabled: Boolean = true
) {
    val quickSand = FontFamily(Font(R.font.quicksand_medium))
    var color = color
    if (isError) color = Color.Red
    BasicTextField(
        value = value,
        onValueChange = { onValueChange(it) },
        singleLine = true,
        textStyle = TextStyle(fontFamily = quickSand),
        enabled = enabled,
        modifier = Modifier
            .clip(RoundedCornerShape(20))
            .background(color)
            .height(40.dp)
            .fillMaxWidth()
            .padding(start = 10.dp, top = 10.dp)
    )
    if (isError) {
        Text(
            text = errorMessage!!,
            color = MaterialTheme.colors.error,
            style = MaterialTheme.typography.caption,
            modifier = Modifier.padding(start = 16.dp)
        )
    }
}