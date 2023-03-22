package com.example.ams.data.ViewModel

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.net.Uri
import androidx.compose.runtime.*
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.ams.data.Model.FirebaseRepository
import com.example.ams.data.DataClasses.*
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.launch
import org.json.JSONException
import org.json.JSONObject
import java.time.LocalDate

class FirebaseViewModel(
    private val firebaseRepository: FirebaseRepository,
) : ViewModel() {

    val newCourseData = mutableStateOf(NewCoureModel())
    val teacherDetailsList: MutableLiveData<List<TeachersList>> = MutableLiveData()
    val courseNames: MutableLiveData<List<NewCoureModel>> = MutableLiveData()
    val attendanceDetail: MutableLiveData<List<AttendceDetail>> = MutableLiveData()
    var getuser : FirebaseUser? = null
    private var currentUserUid :String? = null
    var realAtd: MutableLiveData< Map<String, List<Int>>> = MutableLiveData()
    var realAtdDates: MutableLiveData<List<String>> = MutableLiveData()
    val totalAtdSoFar = MutableLiveData<Int>()
    val totalStd = MutableLiveData<Int>()
    val totalTchr = MutableLiveData<Int>()
    val adminInfo = MutableLiveData<TeachersList>()
    val allStudentInfo = MutableLiveData<List<StudentDetail>>()
    val allStudentImg = MutableLiveData<MutableList<Pair<String, Bitmap>>>()
    val stdWithLowAtd = MutableLiveData<MutableList<String>>()

    init {
        viewModelScope.launch {
            getUserDetails()
        }
        if (!currentUserUid.isNullOrEmpty()) fetchClasses()
     }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as FirebaseApplication)
                val firebaseRepository =  application.container.firebaseRepository
                FirebaseViewModel(firebaseRepository)
            }
        }
    }

    private suspend fun getUserDetails() {
        getuser = firebaseRepository?.getUser()
        currentUserUid = getuser?.uid
    }

    fun clearData() {
        newCourseData.value.let {
            it.name = ""
            it.courseName = ""
            it.noAttendace = ""
            it.batchFrom = ""
            it.batchTo = ""
            it.adminId = ""
        }
    }

    fun clearAtdData() {
        attendanceDetail.value = emptyList()
    }

    fun updateData(name: String, value: String) {
        (newCourseData).let {
            when (name) {
                "name" ->  it.value = it.value.copy(name = value)
                "courseName" -> it.value = it.value.copy(courseName = value)
                "batchFrom" -> it.value = it.value.copy(batchFrom = value)
                "batchTo" -> it.value = it.value.copy(batchTo = value)
                "noAttendance" -> it.value = it.value.copy(noAttendace = value)
            }
        }
    }

    private fun fetchClasses() {
        viewModelScope.launch {
            courseNames.value = firebaseRepository.fetchClasses(userId = currentUserUid!!)
        }
    }

    fun getCourseDetails(id: String, name: String) {
        viewModelScope.launch {
            newCourseData.value = firebaseRepository.getCourseDetails(id = id, name = name) ?: NewCoureModel()
        }
        getTotalNoAtd(adminId = id, courseName = name)
        getTotalStd(adminId = id, courseName = name)
        getTotalTeacher(adminId = id, courseName = name)
        getAdminDetail(adminId = id, courseName = name)
    }

    fun getStudentAtdDetails(courseName: String, adminId: String) {
        val atdDetails = mutableListOf<AttendceDetail>()
        viewModelScope.launch {
            val data = firebaseRepository.getStudentAtdDetails(courseName = courseName, adminId = adminId)
            data?.forEach {
                val eachSTd = mutableListOf<Pair<Int, Boolean>>()
                it.data?.forEach { it2->
                    eachSTd.add(Pair(it2.key, it2.value) as Pair<Int, Boolean>)
                       }
                val atd = AttendceDetail(registerNo = it.id, attendance = eachSTd)
                atdDetails.add(atd)
                attendanceDetail.value = atdDetails
            }
            if (data.isNullOrEmpty()) clearAtdData()
        }
    }

    fun updateCourseDetails(name: String) {
        viewModelScope.launch {
            firebaseRepository.updateCourseDetails(name = name, newCourseData = newCourseData.value)
        }
    }

    fun fetchAllTeachersDetails(adminId: String, courseName: String) {
        val listOfTeacherDetails = mutableListOf<TeachersList>()
        viewModelScope.launch {
            firebaseRepository.fetchAllTeachersDetails(adminId = adminId, courseName = courseName)
                .forEach { doc->
                    doc.toObject(TeachersList::class.java)?.let { listOfTeacherDetails.add(it) }
                }
            teacherDetailsList.value = listOfTeacherDetails
        }
    }

    fun getStdRealAtd(adminId: String, courseName: String) {
        viewModelScope.launch {
            val snapShotAtds = firebaseRepository.getStdRealAtd(adminId = adminId, courseName = courseName)
            val snapShotDates = firebaseRepository.getStdRealAtdDates(adminId = adminId, courseName = courseName)
            val rawAtd = mutableListOf<Pair<String, Int>>()
            snapShotAtds.forEach { doc ->
                doc.data?.forEach { regNo, atd ->
                    rawAtd.add(Pair(regNo, atd) as Pair<String, Int>)
                }
            }
            realAtd.value = rawAtd.groupBy { it.first }.mapValues { it.value.map { it.second } }
            realAtdDates.value = snapShotDates
        }
    }

    fun getTotalNoAtd(adminId: String, courseName: String){
        viewModelScope.launch {
            val snapShotDates = firebaseRepository.getStdRealAtdDates(adminId = adminId, courseName = courseName)
            totalAtdSoFar.value = snapShotDates.size
        }
    }

    fun getTotalStd(adminId: String, courseName: String){
        viewModelScope.launch {
            val totalNoStd = firebaseRepository.getTotalNoStd(adminId = adminId, courseName = courseName)
            totalStd.value = totalNoStd
        }
    }

    fun getTotalTeacher(adminId: String, courseName: String){
        viewModelScope.launch {
            val totalNoTchr = firebaseRepository.getTotalNoTeacher(adminId = adminId, courseName = courseName)
            totalTchr.value = totalNoTchr
        }
    }

    fun getAdminDetail(adminId: String, courseName: String){
        viewModelScope.launch {
            val docSnapShot = firebaseRepository.getAdminData(adminId = adminId, courseName = courseName, phone = "admin")
            adminInfo.value = docSnapShot.toObject(TeachersList::class.java)
        }
    }

    fun getAllStduentData(adminId: String, courseName: String) {
        val allstddata = mutableListOf<StudentDetail>()
        viewModelScope.launch {
            firebaseRepository
                .getAllStudentData(adminId = adminId, courseName = courseName)
                .forEach {
                    val item = it.toObject(StudentDetail::class.java)
                    item?.totoalAtd = it.getLong("totalAtd")!!.toInt()
                    allstddata.add(item!!)
                }
            allStudentInfo.value = allstddata
            fetchData(adminId = adminId, courseName = courseName)
        }
    }

    fun fetchData(adminId: String, courseName: String) {
        var imgFromServer =  mutableListOf<Pair<String, Bitmap>>()
        viewModelScope.launch {
                firebaseRepository.getAllImages(
                    adminId = adminId,
                    courseName = courseName,
                    registerNos = allStudentInfo.value?.map { it.registerNo }!!
                ){
                    imgFromServer += it
                    allStudentImg.value = imgFromServer
                }
        }
    }

    fun addStudentImg(courseName: String, adminId: String, regNo: String, name: String, img: Uri) {
        viewModelScope.launch {
            firebaseRepository.addStudentImg(courseName = courseName, adminId = adminId, regNo = regNo, name = name, img = img)
        }
    }

    fun getStudentWithLowAtd(courseName: String, adminId: String) {
        viewModelScope.launch {
            val atdLow = mutableListOf<String>()
            firebaseRepository.getToatlAtd(adminId = adminId, courseName = courseName)
            firebaseRepository.getAllStudentData(adminId = adminId, courseName = courseName).forEach { std->
                val data = std.getLong("totalAtd")
                val name = std.getString("registerNo")
                if (data != null){
                    if(data < totalAtdSoFar.value!! * 0.75){
                        atdLow.add(name.toString())
                    }
                }
            }
            stdWithLowAtd.value = atdLow
        }
    }
}