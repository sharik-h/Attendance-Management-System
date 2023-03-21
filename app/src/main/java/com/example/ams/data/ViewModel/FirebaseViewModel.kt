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

    val allNotification: MutableLiveData<MutableList<NotificationModel>> = MutableLiveData()
    val allRequests : MutableLiveData<MutableList<RequestCourseModel?>> = MutableLiveData()
    val newStudent = mutableStateOf(StudentDetail())
    val studentImages = MutableLiveData<List<Uri>>()
    val newCourseData = mutableStateOf(NewCoureModel())
    val requestData = mutableStateOf(RequestCourseModel())
    val teacherDetailsList: MutableLiveData<List<TeachersList>> = MutableLiveData()
    val courseNames: MutableLiveData<List<Pair<String, String>>> = MutableLiveData()
    val courseData: MutableLiveData<NewCoureModel> = MutableLiveData()
    val attendanceDetail: MutableLiveData<List<AttendceDetail>> = MutableLiveData()
    val studentList: MutableLiveData<List<String>> = MutableLiveData()
    val identifiedStudents: MutableLiveData<List<String>> = MutableLiveData()
    val unIdentifiedStudents: MutableLiveData<List<String>> = MutableLiveData()
    var getuser : FirebaseUser? = null
    private var currentUserUid :String? = null
    private val studentAtdData = mutableListOf<String>()
    val imageBitmap = MutableLiveData<Bitmap>()
    val notificationData = mutableStateOf(NotificationModel())
    var noAttendance = mutableStateOf(0)
    var periodNo = mutableStateOf(0)
    var realAtd: MutableLiveData< Map<String, List<Int>>> = MutableLiveData()
    var realAtdDates: MutableLiveData<List<String>> = MutableLiveData()
    val availableStd = mutableListOf<String>()
    val unAvailableStd = mutableListOf<String>()
    val totalAtdSoFar = MutableLiveData<Int>()
    val totalStd = MutableLiveData<Int>()
    val totalTchr = MutableLiveData<Int>()
    val adminInfo = MutableLiveData<TeachersList>()
    val allStudentInfo = MutableLiveData<List<StudentDetail>>()
    val allStudentImg = MutableLiveData<MutableList<Pair<String, Bitmap>>>()
    val stdWithLowAtd = MutableLiveData<MutableList<String>>()

    private val FCM_API = "https://fcm.googleapis.com/fcm/send"
    private val serverKey = "key=" + "AAAAtNSvb_Q:APA91bHj2U19HsnKa-idCv94hu1fIke1zo5vxiueCkZnn82keigQTJ-fb-WDsM6okSv1OsPDM9G7FrpMmFkhQ_g1aMF5mZH1VeBRp5qtCp3-uV3-UU9qxkW3PnknLoYV4R0hS_BVq9Wa"
    private val contentType = "application/json"

    fun sendPush(courseName: String, title: String,message: String, context: Context) {

        //topic has to match what the receiver subscribed to
        val topic = "/topics/$courseName"
        val notification = JSONObject()
        val notifcationBody = JSONObject()

        try {
            notifcationBody.put("title", title)
            notifcationBody.put("message", message)   //Enter your notification message
            notification.put("to", topic)
            notification.put("data", notifcationBody)
        } catch (e: JSONException) {
        }
        sendNotification(notification, context)
    }

    private fun sendNotification(notification: JSONObject, context: Context) {
        val requestQueue: RequestQueue by lazy { Volley.newRequestQueue(context) }
        val jsonObjectRequest = object : JsonObjectRequest(FCM_API, notification,
            Response.Listener<JSONObject> { response -> },
            Response.ErrorListener {error -> })
        {
            override fun getHeaders(): Map<String, String> {
                val params = HashMap<String, String>()
                params["Authorization"] = serverKey
                params["Content-Type"] = contentType
                return params
            }
        }
        requestQueue.add(jsonObjectRequest)
    }

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
                "importClassName" -> it.value = it.value.copy(className = value)
                "creatorPhoneNo" -> it.value = it.value.copy(adminId = value)
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

    fun checkRequestDetails(){
        if (requestData.value.className != "" && requestData.value.adminId != "") {
            requestAdmin()
        }
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

    fun getAllRequests(courseName: String) {
        val requests = mutableListOf<RequestCourseModel?>()
        viewModelScope.launch {
            firebaseRepository.getAllRequests(adminId = getuser!!.uid, courseName = courseName)
               .forEach { doc ->
                   requests.add(doc.toObject(RequestCourseModel::class.java))
               }
            allRequests.value = requests
        }
    }

    fun getAllNotifications(courseName: String, adminId: String){
        val notifications = mutableListOf<NotificationModel>()
        viewModelScope.launch {
            firebaseRepository.getAllNotifications(courseName = courseName, userId = adminId)
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
            name = data.teacherName,
            phone = data.teacherPhone,
            email = data.teacherEmail,
            uid = data.teacherUid
        )
        getCourseDetails(id = data.adminId, name = data.className)
        viewModelScope.launch {
            firebaseRepository.acceptTeacher(
                data = data,
                adminId = currentUserUid!!,
                phone = getuser?.phoneNumber!!,
                courseData = newCourseData.value!!,
                teacherDetails = teacherDetails
            )
        }
    }

    fun ignoreTeacher(phone: String, courseName: String) {
        viewModelScope.launch {
            firebaseRepository.ignoreTeacher(id = getuser!!.uid, phone = phone, courseName = courseName)
        }
    }

    fun updateCourseDetails(name: String) {
        viewModelScope.launch {
            firebaseRepository.updateCourseDetails(name = name, newCourseData = newCourseData.value)
        }
    }

    fun checkIfNameIsUsed(): Boolean {
        return courseNames.value?.any { it.name == newCourseData.value.name } ?: false
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

    fun createNewNotification(courseName: String, adminId: String, context: Context) {
        notificationData.value = notificationData.value.copy(id = currentUserUid!!)
        notificationData.value = notificationData.value.copy(date = LocalDate.now().toString())
        viewModelScope.launch {
            firebaseRepository.createNewNotification(notificationData,courseName, adminId)
        }
        sendPush(
            courseName = courseName,
            title = notificationData.value.heading,
            message = notificationData.value.discription,
            context = context
        )
        notificationData.value = notificationData.value.let {
            it.copy(id = "")
            it.copy(heading = "")
            it.copy(discription = "")
            it.copy(date = "")
        }
    }

    // Calls deleteNotification in firebaseRepository to delete the notification
    fun deleteNotification(courseName: String, adminId: String, notificationId: String){
        viewModelScope.launch {
            firebaseRepository.deleteNotification(
                adminId = adminId,
                courseName = courseName,
                notificationId = notificationId
            )
        }
        getAllNotifications(courseName = courseName, adminId = adminId)
    }

    fun updateNotificatoin(courseName: String, adminId: String) {
        viewModelScope.launch {
            firebaseRepository.updateNotificatoin(
                adminId = adminId,
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

    fun checkIfRegNoIsUsed(): Boolean {
        return attendanceDetail
            .value?.map { it.registerNo.replace(" ", "") }
            ?.contains(newStudent.value.registerNo.replace(" ", "")) ?: false
    }

    fun getStudentWithLowAtd(courseName: String, adminId: String) {
        viewModelScope.launch {
            val atdLow = mutableListOf<String>()
            firebaseRepository.getToatlAtd(adminId = adminId, courseName = courseName)
            firebaseRepository.getAllStudentData(adminId = adminId, courseName = courseName).forEach { std->
                val data = std.getLong("totalAtd")
                val name = std.getString("registerNo")
                if(data!! < totalAtdSoFar.value!! * 0.75){
                    atdLow.add(name.toString())
                }
            }
            stdWithLowAtd.value = atdLow
        }
    }
}