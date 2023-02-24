package com.example.ams.MainPages.DetailViewPages

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.rememberNestedScrollInteropConnection
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.ams.R
import com.example.ams.data.ViewModel.FirebaseViewModel

@Composable
fun ViewAttendance(viewModel: FirebaseViewModel, adminId: String, courseName: String) {

    viewModel.getStdRealAtd(adminId = adminId, courseName = courseName)
    val studentAtds by viewModel.realAtd.observeAsState(initial = emptyMap())
    val studentAtdDates by viewModel.realAtdDates.observeAsState(initial = emptyList())
    val quickSandFont = FontFamily(Font(R.font.quicksand_medium))

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(modifier = Modifier.fillMaxWidth()) {
            Text(text = "Attendance", fontFamily = quickSandFont)
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 10.dp, top = 10.dp)
        ) {
            LazyColumn {
                item {
                    Row(modifier = Modifier.fillMaxWidth()) {
                        Spacer(modifier = Modifier.width(100.dp))
                        LazyRow {
                            items(items = studentAtdDates) {
                                Text(
                                    text = it,
                                    modifier = Modifier.width(100.dp),
                                    textAlign = TextAlign.Center,
                                    fontFamily = quickSandFont
                                )
                            }
                        }
                    }
                }
                items(items = studentAtds.toList()) {
                    Row(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = it.first,
                            modifier = Modifier.width(100.dp),
                            fontFamily = quickSandFont
                        )
                            LazyRow {
                                items(items = it.second) {
                                    Text(
                                        text = it.toString(),
                                        modifier = Modifier.width(100.dp),
                                        textAlign = TextAlign.Center,
                                        fontFamily = quickSandFont
                                    )
                                }
                            }
                        }
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }
        }
    }
}

