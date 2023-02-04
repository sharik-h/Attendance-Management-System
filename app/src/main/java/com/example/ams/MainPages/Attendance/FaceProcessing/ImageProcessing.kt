package com.example.ams.MainPages.Attendance.FaceProcessing

import android.graphics.Bitmap
import android.util.Log
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.FaceDetection

class ImageProcessing {
    companion object {

        val detector = FaceDetection.getClient()
        val normalizeImageList = mutableListOf<Bitmap>()
        val DEFAULT_SIZE = 160

        // detect and collect all the faces in the image
        fun getDetectedImages(image: Bitmap, callback: (List<Bitmap>) -> Unit) {
            processImage(image){ face ->
                println("--------------------------------------------------")
                println(face)
                NormalizeFaces(face){
                    callback(normalizeImageList)
                }
            }
        }

        // Resize the detected image into 160x160
        private fun ResizeDetectedFaces(face: Bitmap): Bitmap {
            println("inside resizedDEtected ========================")
            return Bitmap.createScaledBitmap(face, DEFAULT_SIZE, DEFAULT_SIZE, false)
        }


        // Normalize the Resized images
        private fun NormalizeFaces(scaledFace1: Bitmap, callback: (List<Bitmap>) -> Unit) {
            val scaledFace = ResizeDetectedFaces(scaledFace1)
            val pixels = IntArray(scaledFace.width * scaledFace.height)
            scaledFace.getPixels(pixels, 0, scaledFace.width, 0, 0, scaledFace.width, scaledFace.height)
            for (i in pixels.indices) {
                pixels[i] = pixels[i] / 255
            }
            normalizeImageList.add(
                Bitmap.createBitmap(
                    pixels,
                    scaledFace.width,
                    scaledFace.height,
                    Bitmap.Config.ARGB_8888
                )
            )
            callback(normalizeImageList)
        }

        fun processImage(image: Bitmap, callback: (Bitmap) -> Unit) {
            val faceList = mutableListOf<Bitmap>()
            val image1 = InputImage.fromBitmap(image!!, 0)
            detector.process(image1)
                .addOnSuccessListener { faces ->
                    faces.forEach {
                        try {
                          faceList.add(
                                Bitmap.createBitmap(
                                    image,
                                    it.boundingBox.left,
                                    it.boundingBox.top,
                                    it.boundingBox.width(),
                                    it.boundingBox.height()
                                )
                            )
                            callback(faceList[0])
                        }catch (e: Exception){
                            Log.d("personId", e.message.toString())
                        }
                    }
                }
        }
    }
}