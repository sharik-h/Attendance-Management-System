package com.example.ams.data.ViewModel

import androidx.compose.runtime.*
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.ams.data.Model.FirebaseRepository
import com.example.ams.data.DataClasses.*
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.launch

class FirebaseViewModel(private val firebaseRepository: FirebaseRepository) : ViewModel() {

    val allNotification: MutableLiveData<MutableList<RequestCourseModel>> = MutableLiveData()
    val newStudent = mutableStateOf(StudentDetail())
    val newCourseData = mutableStateOf(NewCoureModel())
    val requestData = mutableStateOf(RequestCourseModel())
    val teacherDetailsList: MutableLiveData<List<TeachersList>> = MutableLiveData()
    val courseNames: MutableLiveData<List<Pair<String, String>>> = MutableLiveData()
    val courseData: MutableLiveData<NewCoureModel> = MutableLiveData()
    val attendanceDetail: MutableLiveData<List<AttendceDetail>> = MutableLiveData()
    val studentList: MutableLiveData<List<String>> = MutableLiveData()
    lateinit var getuser : FirebaseUser
    private lateinit var currentUserUid :String
    private val studentAtdData = mutableListOf<String>()

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
        getuser = firebaseRepository.getUser()!!
        currentUserUid = getuser.uid
    }

    private fun createNewClass() {
        newCourseData.value.adminId = currentUserUid
        viewModelScope.launch {
            firebaseRepository.createNewClass(userId = currentUserUid, newCourseData = newCourseData.value)
        }
        clearData()
    }

    fun addStudent(courseName: String, adminId: String) {
        viewModelScope.launch {
            firebaseRepository.addNewStudent(courseName = courseName, adminId = adminId, newStudentData = newStudent)
        }
        clearData()
    }

    fun clearData() {
        newStudent.value.let {
            it.name = ""
            it.registerNo = ""
            it.phone = ""
            it.images.clear()
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
        (newStudent).let {
            when(name) {
                "studentName" -> it.value = it.value.copy(name = value)
                "studentRegisterNo" -> it.value = it.value.copy(registerNo = value)
                "studentPhone" -> it.value = it.value.copy(phone = value)
            }
        }
        (requestData).let {
            when(name) {
                "importClassName" -> it.value = it.value.copy(ClassName = value)
                "creatorPhoneNo" -> it.value = it.value.copy(AdminPhone = value)
            }
        }
    }

    fun addAtdData(registerNo: String) {
        if (studentAtdData.contains(registerNo)) {
            studentAtdData.remove(registerNo)
        }else{
            studentAtdData.add(registerNo)
        }
    }

    private fun fetchClasses() {
        viewModelScope.launch {
            courseNames.value = firebaseRepository.fetchClasses(userId = currentUserUid)
        }
    }

    fun getCourseDetails(id: String, name: String) {
        viewModelScope.launch {
            newCourseData.value = firebaseRepository.getCourseDetails(id = id, name = name) ?: NewCoureModel()
        }
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

    fun checkRequestDetails(){
        if (requestData.value.ClassName != "" && requestData.value.AdminPhone != "") {
            requestAdmin()
        }
    }

    private fun requestAdmin() {
        requestData.value = requestData.value.copy(
            TeacherName = getuser?.displayName.toString(),
            TeacherPhone = getuser?.phoneNumber.toString(),
            TeacherUid = currentUserUid
        )
        viewModelScope.launch {
            firebaseRepository.requestAdmin(data = requestData.value)
        }
    }

    fun getAllNotifications() {
        val requests = mutableListOf<RequestCourseModel>()
        viewModelScope.launch {
            firebaseRepository.getAllNotifications(phone = getuser?.phoneNumber!!)
               .forEach { doc ->
                   doc.toObject(RequestCourseModel::class.java)?.let { requests.add(it) }
               }
            allNotification.value = requests
        }
    }

    fun acceptTeacher(data: RequestCourseModel) {
        val teacherDetails = TeachersList(
            name = data.TeacherName,
            phone = data.TeacherPhone,
            email = data.TeacherEmail,
            uid = data.TeacherUid
        )
        newStudent.value.images.clear()
        viewModelScope.launch {
            firebaseRepository.acceptTeacher(
                data = data,
                userId = currentUserUid,
                phone = getuser?.phoneNumber!!,
                courseData = courseData.value!!,
                teacherDetails = teacherDetails
            )
        }
    }

    fun ignoreTeacher(id: String) {
        viewModelScope.launch {
            firebaseRepository.ignoreTeacher(id = id, phone = getuser?.phoneNumber!!)
        }
    }

    fun updateCourseDetails(name: String) {
        viewModelScope.launch {
            firebaseRepository.updateCourseDetails(name = name, newCourseData = newCourseData.value)
        }
    }

    fun checkIfNameIsUsed(): Boolean {
        return courseNames.value?.contains(Pair(newCourseData.value.name, currentUserUid)) ?: false
    }

    fun checkAndCreateClass():Boolean {
        return if (!checkIfNameIsUsed()){
            createNewClass()
            true
        }else{
            false
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

    fun getStudentDetails(courseName: String, adminId: String, registerNo: String) {
        viewModelScope.launch {
            newStudent.value = firebaseRepository
                .getStudentDetails(courseName = courseName, adminId = adminId, registerNo = registerNo) ?: StudentDetail()
        }
    }

    fun markAttendance(adminId: String, courseName: String, size: Int) {
        studentList.value?.forEach { registerNo ->
            val present = studentAtdData.contains(registerNo)
            viewModelScope.launch {
                firebaseRepository
                    .markAttendance(adminId = adminId, courseName = courseName, registerNo = registerNo, size = size, present = present)
            }
        }
    }
}