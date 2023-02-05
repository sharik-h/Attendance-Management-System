package com.example.ams.faceDetection

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class FileLoader(private val model: FaceNetModel) {

    val storage = Firebase.storage.reference
    private lateinit var storageRefInitial: StorageReference
    private lateinit var callback: ProcessCallback

    val imgFromServer =  mutableListOf<Pair<String, Bitmap>>()
    val nameAndEmbd = arrayListOf<Pair<String, FloatArray>>()
    var size = 0

    interface ProcessCallback {
        fun onProcessComplete(data: List<Pair<String, FloatArray>>)
    }

    fun run(location: String, callback: ProcessCallback){
        storageRefInitial = storage.child(location)
        fetchData(storageRefInitial)
        this.callback = callback
    }

    // This function collects all the image presented in the given storage reference
    private fun fetchData(storageRef: StorageReference) {
        storageRef
            .listAll()
            .addOnSuccessListener { result ->
                // All the images is this storage reference is saved to ImgFromServer with
                // the format of name and bitmap.
                for (item in result.items){
                    GlobalScope.launch(Dispatchers.Main) {
                        val byteArr = item.getBytes(Long.MAX_VALUE).await()
                        val image = BitmapFactory.decodeByteArray(byteArr , 0, byteArr.size)
                        imgFromServer.add(Pair(storageRef.name, image))
                        size++
                        if (size == result.items.size){
                            bitmapToEmbeding()
                        }
                    }
                }
                // is there are several sub folders then it will take each folder and pass
                // the storage reference to this function itself
                for (prefix in result.prefixes){
                    fetchData(prefix)
                }

            }
    }

    // finds the embedding of bitmap image in imgFromServer and saves it into
    // nameAndEmbd as name and embedding and passes it to callback function
    private fun bitmapToEmbeding() {
        for (data in imgFromServer){
            GlobalScope.launch {
                model.getFaceEmbedding(data.second){ embd ->
                    nameAndEmbd.add( Pair( data.first, embd ) )
                }
            }
        }
        callback.onProcessComplete(nameAndEmbd)
    }
}