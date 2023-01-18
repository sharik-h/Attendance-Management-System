package com.example.ams.MainPages.Attendance.FaceProcessing

import android.graphics.Bitmap
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.FaceDetection
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ImageProcessing {

    val detector = FaceDetection.getClient()
    var bitmap = mutableListOf<Bitmap>()

    suspend fun processImage(image: Bitmap): List<Bitmap> {
        val faceList = mutableListOf<Bitmap>()
        val image1 = InputImage.fromBitmap(image!!, 0)
        return withContext(Dispatchers.IO) {
            detector.process(image1)
                .addOnSuccessListener { faces ->
                    faces.forEach {
                       faceList.add( Bitmap.createBitmap(
                                image,
                                it.boundingBox.left,
                                it.boundingBox.top,
                                it.boundingBox.width(),
                                it.boundingBox.height()
                            )
                       )
                    }
                    bitmap = faceList
                }
            bitmap
        }
    }
}