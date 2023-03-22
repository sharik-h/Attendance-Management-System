package com.example.ams.data.ViewModel

import android.net.Uri
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.ams.data.DataClasses.*
import com.example.ams.data.Model.FirebaseRepository
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.launch

class NewOrImportCourseViewModel(
    private val firebaseRepository: FirebaseRepository
): ViewModel() {

    private var currentUserUid :String? = null
    var getuser : FirebaseUser? = null
    val newStudent = mutableStateOf(StudentDetail())
    val studentImages = MutableLiveData<List<Uri>>()
    var noAttendance = mutableStateOf(0)
    val newCourseData = mutableStateOf(NewCoureModel())
    val requestData = mutableStateOf(RequestCourseModel())
    val courseNames: MutableLiveData<List<NewCoureModel>> = MutableLiveData()
    val attendanceDetail: MutableLiveData<List<AttendceDetail>> = MutableLiveData()

    init {
        viewModelScope.launch {
            getUserDetails()
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as FirebaseApplication)
                val firebaseRepository =  application.container.firebaseRepository
                NewOrImportCourseViewModel(firebaseRepository)
            }
        }
    }

    private suspend fun getUserDetails() {
        getuser = firebaseRepository?.getUser()
        currentUserUid = getuser?.uid
    }

    fun checkIfNameIsUsed(): Boolean {
        return courseNames.value?.any { it.name == newCourseData.value.name } ?: false
    }

    fun checkRequestDetails(){
        if (requestData.value.className != "" && requestData.value.adminId != "") {
            requestAdmin()
        }
    }

    fun checkAndCreateClass():Boolean {
        return if (!checkIfNameIsUsed()){
            createNewClass()
            true
        }else{
            false
        }
    }

    private fun createNewClass() {
        newCourseData.value.adminId = currentUserUid!!
        newCourseData.value.periodNo = "0"
        viewModelScope.launch {
            firebaseRepository.createNewClass(userId = currentUserUid!!, newCourseData = newCourseData.value)
            val adminInfo = TeachersList(
                uid = currentUserUid!!,
                phone = getuser?.phoneNumber ?: "",
                email = getuser?.email ?: "",
                name = getuser?.displayName ?: ""
            )
            firebaseRepository.addAdminAsTeacher(
                adminId = currentUserUid!!,
                courseName = newCourseData.value.name,
                adminInfo = adminInfo
            )
        }
        clearData()
    }

    private fun requestAdmin() {
        requestData.value = requestData.value.copy(
            teacherName = getuser?.displayName.toString(),
            teacherPhone = getuser?.phoneNumber.toString(),
            teacherEmail = getuser?.email.toString(),
            teacherUid = currentUserUid!!
        )
        viewModelScope.launch {
            firebaseRepository.requestAdmin(data = requestData.value)
        }
    }

    fun getTotalAtd(courseName: String, adminId: String){
        viewModelScope.launch {
            noAttendance.value = firebaseRepository.getToatlAtd(courseName = courseName, adminId = adminId)
        }
    }

    fun setStudentImage(uri: Uri){
        val currentList: MutableList<Uri> = studentImages.value?.toMutableList() ?: mutableListOf()
        currentList.add(uri)
        studentImages.value = currentList
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
        (newStudent).let {
            when(name) {
                "studentName" -> it.value = it.value.copy(name = value)
                "studentRegisterNo" -> it.value = it.value.copy(registerNo = value)
                "studentPhone" -> it.value = it.value.copy(phone = value)
            }
        }
        (requestData).let {
            when(name) {
                "importClassName" -> it.value = it.value.copy(className = value)
                "creatorPhoneNo" -> it.value = it.value.copy(adminId = value)
            }
        }
    }

    fun checkIfRegNoIsUsed(): Boolean {
        return attendanceDetail
            .value?.map { it.registerNo.replace(" ", "") }
            ?.contains(newStudent.value.registerNo.replace(" ", "")) ?: false
    }

    fun addStudent(courseName: String, adminId: String) {
        viewModelScope.launch {
            firebaseRepository.addNewStudent(
                courseName = courseName,
                adminId = adminId,
                newStudentData = newStudent,
                noAttendance = noAttendance.value,
                studentImages = studentImages.value!!
            )
        }
        clearData()
        studentImages.value = emptyList()
    }

    fun clearData() {
        newStudent.value.let {
            it.name = ""
            it.registerNo = ""
            it.phone = ""
        }
        newCourseData.value.let {
            it.name = ""
            it.courseName = ""
            it.noAttendace = ""
            it.batchFrom = ""
            it.batchTo = ""
            it.adminId = ""
        }
    }
}