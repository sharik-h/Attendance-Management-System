package com.example.ams.data.ViewModel

import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.ams.data.DataClasses.NewCoureModel
import com.example.ams.data.DataClasses.NotificationModel
import com.example.ams.data.DataClasses.RequestCourseModel
import com.example.ams.data.DataClasses.TeachersList
import com.example.ams.data.Model.FirebaseRepository
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.launch
import org.json.JSONException
import org.json.JSONObject
import java.time.LocalDate

class NotificationRequestViewModel(
    private val firebaseRepository: FirebaseRepository
): ViewModel() {

    var getuser : FirebaseUser? = null
    var currentUserUid: String? = null
    val notificationData = mutableStateOf(NotificationModel())
    val allNotification: MutableLiveData<MutableList<NotificationModel>> = MutableLiveData()
    val allRequests : MutableLiveData<MutableList<RequestCourseModel?>> = MutableLiveData()
    val requestData = mutableStateOf(RequestCourseModel())
    val newCourseData = mutableStateOf(NewCoureModel())
    val totalAtdSoFar = MutableLiveData<Int>()
    val totalStd = MutableLiveData<Int>()
    val totalTchr = MutableLiveData<Int>()
    val adminInfo = MutableLiveData<TeachersList>()

    private val FCM_API = "https://fcm.googleapis.com/fcm/send"
    private val serverKey = "key=" + "AAAAtNSvb_Q:APA91bGJDNa-hrD6NgyIuRqVyWbkDRUwDTabrKVFO8o7GCEsveZCtWyGyN_gPVX3aki1PK7bpvuUb0IVhvxSrNgQ8u3GgrAURCBjbuoGbKaum5nviPJXGJCuBxwOodn8EDQeLnXLREo6"
    private val contentType = "application/json"

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
                NotificationRequestViewModel(firebaseRepository)
            }
        }
    }

    fun updateData(name: String, value: String) {
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

    fun clearData() {
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

    private suspend fun getUserDetails() {
        getuser = firebaseRepository?.getUser()
        currentUserUid = getuser?.uid
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
            Response.ErrorListener { error -> })
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

    fun getCourseDetails(id: String, name: String) {
        viewModelScope.launch {
            newCourseData.value = firebaseRepository.getCourseDetails(id = id, name = name) ?: NewCoureModel()
        }
        getTotalNoAtd(adminId = id, courseName = name)
        getTotalStd(adminId = id, courseName = name)
        getTotalTeacher(adminId = id, courseName = name)
        getAdminDetail(adminId = id, courseName = name)
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
}