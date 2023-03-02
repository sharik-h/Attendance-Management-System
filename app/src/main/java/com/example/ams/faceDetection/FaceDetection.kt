package com.example.ams.faceDetection

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.MutableLiveData
import kotlin.math.pow
import kotlin.math.sqrt

class FaceDetection(
    private val model: FaceNetModel,
    context: Context
) {

    private var fileLoader: FileLoader

    init {
        val faceNetModel = FaceNetModel(context)
        fileLoader = FileLoader(faceNetModel)
    }

    var imageData = arrayListOf<Pair<String, FloatArray>>()
    val isImageDataEmpty = MutableLiveData(false)
    private val nameScoreHashmap = HashMap<String,ArrayList<Float>>()

    // Loads the file from the server
    fun load(data: String) {
        fileLoader.run(data, fileLoaderCallback)
    }

    // Invokes the runFaceNetModel function in order to identify the given person
    fun Detect(image: Bitmap, callback: (String) -> Unit){
            runFaceNetModel(image){
                callback(it)
            }
    }


    private val fileLoaderCallback = object : FileLoader.ProcessCallback {
        override fun onProcessComplete(data: List<Pair<String, FloatArray>>) {
            imageData = data as ArrayList<Pair<String, FloatArray>>
            isImageDataEmpty.value = true
        }
    }

    private fun runFaceNetModel(face: Bitmap, callback: (String) -> Unit ) {
        try {
            model.getFaceEmbedding(face) { embedding ->
                //perform clustering
                // for each of the face embedding that we have already stored
                // we perform following operations for the processing image.
                for (i in 0 until imageData.size) {

                    // if the cluster doesn't exist compute and add it to the hashmap
                    if (nameScoreHashmap[imageData[i].first] == null) {
                        val p = ArrayList<Float>()
                        p.add( L2Norm( embedding , imageData[ i ].second ) )
                        nameScoreHashmap[imageData[i].first] = p
                    }
                    // if cluster already has a value then append the new one to it.
                    else {
                        nameScoreHashmap[imageData[i].first]?.add(L2Norm(embedding, imageData[i].second))
                    }
                }

                // find the average of each cluster in hashmap
                val averages = nameScoreHashmap.values.map { scores ->
                    scores.toFloatArray().average()
                }

                // store all the names/keys in the hashmap
                val names = nameScoreHashmap.keys.toTypedArray()
                nameScoreHashmap.clear()

                // calculate the L2 distance from the averages value
                if (averages.minOrNull()!! > 10f) {
                    callback("unknown")
                } else {
                    callback( names[averages.indexOf(averages.minOrNull()!!)])
                }
            }
        }catch (e: Exception){
            Log.d("personId",e.message.toString())
        }
    }

    // Compute the L2 norm of ( x2 - x1 )
    private fun L2Norm( x1 : FloatArray, x2 : FloatArray ) : Float {
        return sqrt( x1.mapIndexed{ i , xi -> (xi - x2[ i ]).pow( 2 ) }.sum() )
    }

}