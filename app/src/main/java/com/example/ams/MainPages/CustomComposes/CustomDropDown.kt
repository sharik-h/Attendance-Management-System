package com.example.ams.MainPages.CustomComposes

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.DropdownMenu
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.ams.R
import java.time.LocalDate

@Composable
fun customDropDown(
    isEditEnabled: Boolean,
    expanded: Boolean,
    batch: String,
    color: Color = Color(0x7ECACACA),
    onclick: (String) -> Unit
) {
    val years = (LocalDate.now().year-5 ..LocalDate.now().year+5).toList()
    val quickSand = FontFamily(Font(R.font.quicksand_medium))
    var selected by remember { mutableStateOf(batch) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .width(80.dp)
            .height(30.dp)
            .clip(RoundedCornerShape(30))
            .background(if (isEditEnabled) color else Color.Transparent)
            .clickable { onclick(selected) }) {
        Text(text = batch, fontFamily = quickSand)
        if (isEditEnabled){
            Image(painter = painterResource(id = R.drawable.arrow_down_black), contentDescription = "")
        }
        DropdownMenu(
            expanded = if (isEditEnabled) expanded else false,
            onDismissRequest = { onclick(selected) },
            modifier = Modifier
                .height(200.dp)
                .width(100.dp)
                .background(Color.White)
        ) {
            years.forEach {
                TextButton(
                    modifier = Modifier
                        .width(80.dp)
                        .background(Color.White),
                    colors = ButtonDefaults.textButtonColors(backgroundColor = Color.White),
                    onClick = {
                        selected = it.toString()
                        onclick(selected)
                    }
                ) {
                    Text(
                        text = "$it",
                        fontFamily = quickSand,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth(),
                        color = Color.Black
                    )
                }
            }
        }
    }
}