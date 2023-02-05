package com.example.ams.faceDetection

import android.content.Context
import android.graphics.Bitmap
import com.example.ams.MainPages.Attendance.FaceProcessing.ImageProcessing
import org.tensorflow.lite.DataType
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.support.common.FileUtil
import java.nio.ByteBuffer
import org.tensorflow.lite.support.common.TensorOperator
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import org.tensorflow.lite.support.tensorbuffer.TensorBufferFloat
import kotlin.math.max
import kotlin.math.pow
import kotlin.math.sqrt

class FaceNetModel(
    context: Context,
) {

    private val interpreter: Interpreter
    private val imageTensorProcessor = ImageProcessor.Builder()
        .add( ResizeOp( 160 , 160 , ResizeOp.ResizeMethod.BILINEAR ) )
        .add( StandardizeOp() )
        .build()

    init {
        // Initializing TensorFlow Interpreter
        val interpreterOptions = Interpreter
            .Options()
            .setNumThreads(4)
            .setUseNNAPI(true)
            .setUseXNNPACK(true)
        val model = Models.FACENET
        interpreter = Interpreter(FileUtil.loadMappedFile(context, model.assetsFilename ), interpreterOptions)
    }

    // Detect the face embeddings
    fun getFaceEmbedding(image: Bitmap, onResult: (result: FloatArray) -> Unit) {
        ImageProcessing.getDetectedImages(image){ face ->
            onResult(runFaceNet(convertToByteBuffer(face))[0])
        }
    }


    // Actual running of faceNet Model takes place here.
    private fun runFaceNet(image: Any): Array<FloatArray> {
        val faceNetModelOutputs = Array(1){ FloatArray(128)}
        interpreter.run(image, faceNetModelOutputs)
        return faceNetModelOutputs
    }

    // converting the image from Bitmap to ByteBuffer.
    // Bitmap format is not suitable for deep learning purpose
    // as it is designed to represent images in android framework.
    private fun convertToByteBuffer(image: Bitmap): ByteBuffer {
        return imageTensorProcessor.process( TensorImage.fromBitmap( image ) ).buffer
    }

    // still figuring out how it works.
    // The main benefit of standardizing the pixel values is that
    // it helps the model to converge faster and perform better.
    class StandardizeOp : TensorOperator {

        override fun apply(p0: TensorBuffer?): TensorBuffer {
            val pixels = p0!!.floatArray
            val mean = pixels.average().toFloat()
            var std = sqrt( pixels.map{ pi -> ( pi - mean ).pow( 2 ) }.sum() / pixels.size.toFloat() )
            std = max( std , 1f / sqrt( pixels.size.toFloat() ))
            for ( i in pixels.indices ) {
                pixels[ i ] = ( pixels[ i ] - mean ) / std
            }
            val output = TensorBufferFloat.createFixedSize( p0.shape , DataType.FLOAT32 )
            output.loadArray( pixels )
            return output
        }

    }
}