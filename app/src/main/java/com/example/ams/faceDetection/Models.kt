package com.example.ams.faceDetection

import com.example.ams.data.DataClasses.ModelInfo

class Models {
    companion object{
        val FACENET = ModelInfo(
            "FaceNet" ,
            "facenet.tflite" ,
            0.4f ,
            10f ,
            128 ,
            160
        )
    }
}