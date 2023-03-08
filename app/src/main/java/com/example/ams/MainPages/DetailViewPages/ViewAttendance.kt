package com.example.ams.MainPages.DetailViewPages

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.ams.MainPages.CustomComposes.cornerBorder
import com.example.ams.R
import com.example.ams.data.ViewModel.FirebaseViewModel
import com.example.ams.ui.theme.pri

@Composable
fun ViewAttendance(
    navController: NavController,
    viewModel: FirebaseViewModel,
    adminId: String,
    courseName: String
) {

    val studentAtds by viewModel.realAtd.observeAsState(initial = emptyMap())
    val studentAtdDates by viewModel.realAtdDates.observeAsState(initial = emptyList())
    val quickSand = FontFamily(Font(R.font.quicksand_medium))
    var scroll = rememberScrollState()

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(backgroundColor = pri) {
            IconButton(onClick = { navController.navigateUp() }) {
                Image(
                    painter = painterResource(id = R.drawable.arrow_back),
                    contentDescription = ""
                )
            }
            Text(
                text = "Attendance",
                fontFamily = quickSand,
                color = Color.White,
                fontSize = 20.sp
            )
        }
        Row(modifier = Modifier.fillMaxSize()) {
            Column {
                var even by remember { mutableStateOf(false) }
                Spacer(modifier = Modifier
                    .width(90.dp)
                    .height(22.dp))
                for (i in studentAtds){
                    even = !even
                    Text(
                        text = i.key,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .width(90.dp)
                            .background(if (even) Color(0xFFEEEEEE) else Color.White)
                    )
                }
            }
            Row(modifier = Modifier.horizontalScroll(scroll)){
                var even by remember { mutableStateOf(false) }
                var j = 0
                studentAtdDates.forEach{
                    Column {
                        Text(
                            text = it,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .width(100.dp)
                                .cornerBorder(strokeWidth = 1.dp, color = Color.Black)
                        )
                        for( i in studentAtds.toList() ){
                            even = !even
                            val isEven = even
                            Text(
                                text = i.second[j].toString(),
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .width(100.dp)
                                    .cornerBorder(strokeWidth = 1.dp, color = Color.Black)
                                    .background(if (isEven) Color(0xFFEEEEEE) else Color.White)
                            )
                        }
                    }
                    if (j< studentAtdDates.size-1){
                        j++
                    }
                }
            }
        }
    }
}

