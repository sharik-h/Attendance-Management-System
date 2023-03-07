package com.example.ams.MainPages

import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.ams.R
import com.example.ams.data.ViewModel.FirebaseViewModel
import com.example.ams.data.DataClasses.StudentDetail
import com.example.ams.ui.theme.pri
import java.io.ByteArrayOutputStream
import java.util.regex.Pattern

@Composable
fun ViewStudents(
    navHostController: NavHostController,
    adminId: String,
    courseName: String,
    viewModel: FirebaseViewModel
) {
    val stddata by viewModel.allStudentInfo.observeAsState(initial = emptyList())
    val imgs by viewModel.allStudentImg.observeAsState(initial = viewModel.allStudentImg.value)
    val pattern = Pattern.compile("([a-zA-Z]+)\\d*_?")
    val data2 = imgs?.groupBy { it ->
        val matcher = pattern.matcher(it.toString())
        if (matcher.find()) {
            matcher.group(1)
        } else {
            ""
        }
    }
    val context = LocalContext.current
    var selectedReg by remember { mutableStateOf("") }
    var selectedName: String by remember { mutableStateOf("") }
    val cameraLauncher = rememberLauncherForActivityResult(contract = ActivityResultContracts.TakePicturePreview()) {
        if (it != null) {
            val bytes = ByteArrayOutputStream()
            it.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
            val path = MediaStore.Images.Media.insertImage(context.contentResolver, it, selectedName, null)
            viewModel.addStudentImg(
              courseName = courseName,
              adminId = adminId,
              regNo = selectedReg,
              name = selectedName,
              img  = Uri.parse(path.toString())
          )
        }
    }
    val quickSand = FontFamily(Font(R.font.quicksand_medium))

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(backgroundColor = pri) {
            IconButton(onClick = { navHostController.navigateUp() }) {
                Image(painter = painterResource(id = R.drawable.arrow_back), contentDescription = "")
            }
            Text(text = "All Students", fontFamily = quickSand, color = Color.White, fontSize = 20.sp)
        }
        LazyColumn {
            items(items = stddata) {
                println(data2?.get(it.name))
                StudentItem(
                    data = it,
                    images = data2?.get(it.name) ?: emptyList()
                ) { regNo, name ->
                    selectedReg = regNo
                    selectedName = name
                    cameraLauncher.launch()
                }
                Divider(thickness = 0.5.dp, color = Color.Black)
            }
        }
    }
}

@Composable
fun StudentItem(
    data: StudentDetail,
    images: List<Pair<String, Bitmap>>,
    onClick: (regNo: String, name: String) -> Unit
) {
    var editable by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .padding(top = 5.dp, start = 10.dp, bottom = 5.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Center
    ) {
        customDataModel1(field = "Name            :", data = data.name)
        customDataModel1(field = "Register no.  :", data = data.registerNo)
        customDataModel1(field = "Phone no.      :", data = data.phone)
        Spacer(modifier = Modifier.height(5.dp))
        Spacer(modifier = Modifier.height(5.dp))
        LazyRow{
            items(items = images){
                Image(
                    painter = rememberAsyncImagePainter(it.second),
                    contentDescription = "",
                    modifier = Modifier
                        .size(50.dp)
                        .clip(RoundedCornerShape(30))
                )
                Spacer(modifier = Modifier.width(5.dp))
            }
            item {
                Button(
                    onClick = {
                        onClick(data.registerNo, images.last().first)
                              },
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFE2E2E2)),
                    modifier = Modifier
                        .size(50.dp)
                        .clip(RoundedCornerShape(30))
                ) {
                    Icon(painter = painterResource(id = R.drawable.add_icon_white), contentDescription = "")
                }
            }
        }
    }
}

@Composable
fun customDataModel1(
    field: String,
    data: String
) {
    val quickSand = FontFamily(Font(R.font.quicksand_medium))
    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Text(text = field, fontFamily = quickSand, fontSize = 17.sp)
        Spacer(modifier = Modifier.width(15.dp))
        Text(text = data, fontFamily = quickSand, fontSize = 17.sp )
    }
}