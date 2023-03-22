package com.example.ams.data.ViewModel

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.ams.data.Model.FirebaseRepository
import kotlinx.coroutines.launch

class AttendanceViewModel(
    private val firebaseRepository: FirebaseRepository
): ViewModel() {

    val studentList: MutableLiveData<List<String>> = MutableLiveData()
    var periodNo = mutableStateOf(0)
    var noAttendance = mutableStateOf(0)
    val imageBitmap = MutableLiveData<Bitmap>()
    val identifiedStudents: MutableLiveData<List<String>> = MutableLiveData()
    val unIdentifiedStudents: MutableLiveData<List<String>> = MutableLiveData()
    private val availableStd = mutableListOf<String>()
    private val unAvailableStd = mutableListOf<String>()
    private val studentAtdData = mutableListOf<String>()

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as FirebaseApplication)
                val firebaseRepository =  application.container.firebaseRepository
                AttendanceViewModel(firebaseRepository)
            }
        }
    }

    fun saveDetectedStudents(name: String) {
        availableStd.add(name)
        identifiedStudents.value = availableStd
        studentList.value?.forEach {
            if (!(identifiedStudents.value as MutableList<String>).contains(it)){
                unAvailableStd.add(it)
                unIdentifiedStudents.value = unAvailableStd
            }else{
                unAvailableStd.remove(it)
                unIdentifiedStudents.value = unAvailableStd
            }
        }
    }

    fun getPeriodNo(adminId: String, courseName: String) {
        viewModelScope.launch {
            periodNo.value = firebaseRepository.getPeriodNo(courseName = courseName, adminId = adminId)
        }
    }

    fun getTotalAtd(courseName: String, adminId: String){
        viewModelScope.launch {
            noAttendance.value = firebaseRepository.getToatlAtd(courseName = courseName, adminId = adminId)
        }
    }

    fun getAllStudents(adminId: String, courseName: String) {
        val docids = mutableListOf<String>()
        viewModelScope.launch {
            firebaseRepository.getAllStudents(adminId = adminId, courseName = courseName)
                .forEach { doc->
                    docids.add(doc.id)
                }
            studentList.value = docids
        }
    }

    fun addAtdData(registerNo: String) {
        if (studentAtdData.contains(registerNo)) {
            studentAtdData.remove(registerNo)
        }else{
            studentAtdData.add(registerNo)
        }
    }

    fun markAttendance(adminId: String, courseName: String) {
        if (periodNo.value +1 <= noAttendance.value) {
            studentList.value?.forEach { registerNo ->
                val present = studentAtdData.contains(registerNo)
                viewModelScope.launch {
                    firebaseRepository
                        .markAttendance(
                            adminId = adminId,
                            courseName = courseName,
                            registerNo = registerNo,
                            size = periodNo.value+1,
                            present = present
                        )
                }
            }
            viewModelScope.launch {
                firebaseRepository
                    .updatePeriod(
                        adminId = adminId,
                        courseName = courseName,
                        periodNo = periodNo.value + 1
                    )
            }
        }
        studentAtdData.removeAll(studentAtdData)
    }

    fun setbitimgValue(bitmap: Bitmap){
        imageBitmap.postValue(bitmap)
    }

    fun resizeBitmapByHeight(bitmap: Bitmap, newHeight: Int): Bitmap {
        val aspectRatio = bitmap.width.toFloat() / bitmap.height.toFloat()
        val newWidth = (newHeight * aspectRatio).toInt()
        return resizeBitmap(bitmap, newWidth, newHeight)
    }

    private fun resizeBitmap(bitmap: Bitmap, newWidth: Int, newHeight: Int): Bitmap {
        val scaledBitmap = Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, false)
        val resizedBitmap = Bitmap.createBitmap(newWidth, newHeight, bitmap.config)
        val canvas = Canvas(resizedBitmap)
        val paint = Paint()
        paint.isFilterBitmap = true
        canvas.drawBitmap(scaledBitmap, 0f, 0f, paint)
        scaledBitmap.recycle()
        return resizedBitmap
    }
}