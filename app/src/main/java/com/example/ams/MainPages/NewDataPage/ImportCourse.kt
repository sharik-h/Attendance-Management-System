package com.example.ams.MainPages.NewDataPage

import android.graphics.Bitmap
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ams.data.ViewModel.FirebaseViewModel
import androidx.navigation.NavHostController
import com.example.ams.MainPages.CustomTextFeild
import com.example.ams.R
import com.example.ams.ui.theme.pri
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage

@Composable
fun ImportCourse(navHostController: NavHostController  ,viewModel: FirebaseViewModel) {

    val requestData = viewModel.requestData.value
    val quickSand = FontFamily(Font(R.font.quicksand_medium))
    val cameraLauncher = rememberLauncherForActivityResult(contract = ActivityResultContracts.TakePicturePreview()) {
        if (it != null){
            qrReader(it){ adminId ->
                viewModel.updateData("creatorPhoneNo", adminId)
            }
        }
    }

    Column(Modifier.fillMaxSize()) {
        TopAppBar(backgroundColor = pri) {
            IconButton(onClick = { navHostController.navigateUp() }) {
                Image(painter = painterResource(id = R.drawable.arrow_back), contentDescription = "")
            }
            Text(
                text = "Import a class",
                fontFamily = quickSand,
                fontSize = 30.sp,
                color = Color.White
            )
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
        ) {
            Text(text = "Name of class", fontFamily = quickSand, fontSize = 17.sp)
            CustomTextFeild(
                value = requestData.className,
                onValueChange = { viewModel.updateData("importClassName", it) }
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(text = "Admin Id", fontFamily = quickSand, fontSize = 17.sp)
            Row(Modifier.fillMaxWidth()) {
                Row(Modifier.weight(0.9f)) {
                    CustomTextFeild(
                        value = requestData.adminId,
                        onValueChange = { viewModel.updateData("creatorPhoneNo", it) }
                    )
                }
                Row(Modifier.weight(0.1f), verticalAlignment = Alignment.Top) {
                    IconButton(onClick = { cameraLauncher.launch()}) {
                        Image(painter = painterResource(id = R.drawable.qr_code_scanner_black), contentDescription = "")
                    }
                }
            }
            Spacer(modifier = Modifier.weight(0.5f))
            Button(
                onClick = {
                    viewModel.checkRequestDetails()
                    navHostController.navigateUp()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20)),
                colors = ButtonDefaults.buttonColors(backgroundColor = pri),
            ) {
                Text(text = "Request", fontFamily = quickSand, color = Color.White)
            }
        }
    }
}

fun qrReader(img: Bitmap, callback: (String) -> Unit ) {
    val image = InputImage.fromBitmap(img, 0)
    val scanner = BarcodeScanning.getClient()

    scanner.process(image)
        .addOnSuccessListener { barcodes ->
            for (barcode in barcodes) {
                if(barcode.valueType == Barcode.TYPE_TEXT ) {
                    barcode.displayValue?.let { callback(it)
                    }
                }
            }
        }
}