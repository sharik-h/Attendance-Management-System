package com.example.ams.data.ViewModel

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
import com.example.ams.data.Model.FirebaseRepository
import com.example.ams.data.DataClasses.*
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.launch
import java.time.LocalDate

class FirebaseViewModel(
    private val firebaseRepository: FirebaseRepository,
) : ViewModel() {

    val allNotification: MutableLiveData<MutableList<NotificationModel>> = MutableLiveData()
    val allRequests : MutableLiveData<MutableList<RequestCourseModel>> = MutableLiveData()
    val newStudent = mutableStateOf(StudentDetail())
    val studentImages = MutableLiveData<List<Uri>>()
    val newCourseData = mutableStateOf(NewCoureModel())
    val requestData = mutableStateOf(RequestCourseModel())
    val teacherDetailsList: MutableLiveData<List<TeachersList>> = MutableLiveData()
    val courseNames: MutableLiveData<List<Pair<String, String>>> = MutableLiveData()
    val courseData: MutableLiveData<NewCoureModel> = MutableLiveData()
    val attendanceDetail: MutableLiveData<List<AttendceDetail>> = MutableLiveData()
    val studentList: MutableLiveData<List<String>> = MutableLiveData()
    var getuser : FirebaseUser? = null
    private var currentUserUid :String? = null
    private val studentAtdData = mutableListOf<String>()
    val imageBitmap = MutableLiveData<Bitmap>()
    val notificationData = mutableStateOf(NotificationModel())
    var noAttendance = mutableStateOf(0)
    var periodNo = mutableStateOf(0)
    var realAtd: MutableLiveData< Map<String, List<Int>>> = MutableLiveData()
    var realAtdDates: MutableLiveData<List<String>> = MutableLiveData()

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

    private fun createNewClass() {
        newCourseData.value.adminId = currentUserUid!!
        newCourseData.value.periodNo = "0"
        viewModelScope.launch {
            firebaseRepository.createNewClass(userId = currentUserUid!!, newCourseData = newCourseData.value)
        }
        clearData()
    }

    fun addStudent(courseName: String, adminId: String) {
        viewModelScope.launch {
            firebaseRepository.addNewStudent(
                courseName = courseName,
                adminId = adminId,
                newStudentData = newStudent,
                noAttendance = noAttendance.value
            )
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
        notificationData.value.let {
            it.id = ""
            it.date = ""
            it.heading = ""
            it.discription = ""
            it.notificationId = ""
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
        (notificationData).let {
            when(name) {
                "notificationHeading" -> it.value = it.value.copy(heading = value)
                "notificationDiscrip" -> it.value = it.value.copy(discription = value)
                "notificationDate" -> it.value = it.value.copy(date = value)
                "notificationId" -> it.value = it.value.copy(notificationId = value)
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
            courseNames.value = firebaseRepository.fetchClasses(userId = currentUserUid!!)
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
            TeacherUid = currentUserUid!!
        )
        viewModelScope.launch {
            firebaseRepository.requestAdmin(data = requestData.value)
        }
    }

    fun getAllRequests() {
        val requests = mutableListOf<RequestCourseModel>()
        viewModelScope.launch {
            firebaseRepository.getAllRequests(phone = getuser?.phoneNumber!!)
               .forEach { doc ->
                   doc.toObject(RequestCourseModel::class.java)?.let { requests.add(it) }
               }
            allRequests.value = requests
        }
    }

    fun getAllNotifications(courseName: String){
        val notifications = mutableListOf<NotificationModel>()
        viewModelScope.launch {
            firebaseRepository.getAllNotifications(courseName = courseName, userId = getuser!!.uid)
                .forEach { doc ->
                    doc.toObject(NotificationModel::class.java)?.let {
                        it.notificationId = doc.id
                        notifications.add(it)
                    }
                }
            allNotification.value = notifications
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
                userId = currentUserUid!!,
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

    fun setStudentImage(uri: Uri){
        val currentList: MutableList<Uri> = studentImages.value?.toMutableList() ?: mutableListOf()
        currentList.add(uri)
        studentImages.value = currentList
        newStudent.value.images.add(uri)
    }

    fun saveDetectedStudents(name: String) {
        val availableStd = mutableListOf<String>()
        availableStd.add(name)
        studentList.value = availableStd
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

    fun resizeBitmapByHeight(bitmap: Bitmap, newHeight: Int): Bitmap {
        val aspectRatio = bitmap.width.toFloat() / bitmap.height.toFloat()
        val newWidth = (newHeight * aspectRatio).toInt()
        return resizeBitmap(bitmap, newWidth, newHeight)
    }

    fun createNewNotification(courseName: String) {
        notificationData.value = notificationData.value.copy(id = currentUserUid!!)
        notificationData.value = notificationData.value.copy(date = LocalDate.now().toString())
        viewModelScope.launch {
            firebaseRepository.createNewNotification(notificationData,courseName)
        }
        notificationData.value = notificationData.value.let {
            it.copy(id = "")
            it.copy(heading = "")
            it.copy(discription = "")
            it.copy(date = "")
        }
    }

    // Calls deleteNotification in firebaseRepository to delete the notification
    fun deleteNotification(courseName: String, notificationId: String){
        viewModelScope.launch {
            firebaseRepository.deleteNotification(
                userId = currentUserUid!!,
                courseName = courseName,
                notificationId = notificationId
            )
        }
        getAllNotifications(courseName = courseName)
    }

    fun updateNotificatoin(courseName: String) {
        viewModelScope.launch {
            firebaseRepository.updateNotificatoin(
                userId = currentUserUid!!,
                courseName = courseName,
                data = notificationData
            )
        }
        notificationData.value = notificationData.value.let {
            it.copy(id = "")
            it.copy(heading = "")
            it.copy(discription = "")
            it.copy(date = "")
            it.copy(notificationId = "")
        }
    }

    fun getTotalAtd(courseName: String, adminId: String){
        viewModelScope.launch {
            noAttendance.value = firebaseRepository.getToatlAtd(courseName = courseName, adminId = adminId)
        }
    }

    fun getPeriodNo(adminId: String, courseName: String) {
        viewModelScope.launch {
            periodNo.value = firebaseRepository.getPeriodNo(courseName = courseName, adminId = adminId)
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
}