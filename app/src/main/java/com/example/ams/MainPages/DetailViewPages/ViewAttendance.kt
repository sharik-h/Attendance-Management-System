package com.example.ams.MainPages.DetailViewPages

import android.content.Context
import android.graphics.*
import android.graphics.pdf.PdfDocument
import android.os.Environment
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
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
import java.io.*
import java.time.LocalDate

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
    val context = LocalContext.current

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
            Spacer(modifier = Modifier.weight(0.5f))
            IconButton(onClick = { generatePDF(context = context, studentAtdDates, studentAtds) }) {
                Image(painter = painterResource(id = R.drawable.download_white), contentDescription = "")
            }
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

fun generatePDF(
    context: Context,
    studentAtdDates: List<String>,
    studentAtds: Map<String, List<Int>>
) {
    val atdData = studentAtds.toList()
    val pdfDocument = PdfDocument()
    val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create()
    var k = 0
    studentAtdDates.groupBy { it.substring(0,7) }.forEach { month, dates ->

        val page = pdfDocument.startPage(pageInfo)
        val canvas = page.canvas
        val tableLeft = 50F
        val columnWidth = 150F

        canvas.drawText("${month}", tableLeft + 5F, 10F, Paint())
        canvas.drawLine(tableLeft, 25F, 520f, 25F, Paint())
        canvas.drawText("Reg No.", tableLeft + 5F, 40F, Paint())
        canvas.drawLine(tableLeft, 42f , tableLeft + 20 * 20 + columnWidth - 80, 42f, Paint())
        canvas.drawLine(tableLeft, 25F, tableLeft, 42f, Paint())
        for (j in dates.indices) {
//            canvas.drawLine(((j + 1) * 20) + 100f, 5F, ((j + 1) * 20) + 100f, 22f, Paint())
            canvas.drawText(dates[j].substring(8), tableLeft + ((j + 1) * 20) + 53f, 40F, Paint())
        }
//        canvas.drawLine(520f, 5F, 520f, 22f, Paint())

        var l = 3
        val temp = k
        atdData.forEach {
            canvas.drawText(it.first, 55F, l * 20F, Paint())
            canvas.drawLine(tableLeft, (l * 20f) + 2,520f , (l * 20f) + 2, Paint())
            canvas.drawLine(tableLeft, ((l-1) * 20F) + 23F ,  tableLeft,l * 20f, Paint())
            k = temp
            for(j in dates.indices){
                canvas.drawLine( ( (j+1) * 20 )+ 100f, (l-1 * 20F) + 43F , ( (j+1) * 20 ) + 100f, (l * 20f )+2, Paint())
                canvas.drawText(it.second[k].toString(),((j+1) * 20) + 103f,l * 20F, Paint())
                k++
            }
            canvas.drawLine(520f,(l-1 * 20F) + 43F ,520f,(l * 20f)+2, Paint())
            l++
        }
        pdfDocument.finishPage(page)

    }
    val localDate = LocalDate.now()
    val file = File(Environment.getExternalStorageDirectory(), "Attendance($localDate).pdf")
    try {
        pdfDocument.writeTo(FileOutputStream(file))
        Toast.makeText(context, "PDF file generated..", Toast.LENGTH_SHORT).show()
    } catch (e: Exception) {
        e.printStackTrace()
        Toast.makeText(context, "Fail to generate PDF file..", Toast.LENGTH_SHORT).show()
    }
    pdfDocument.close()
}
