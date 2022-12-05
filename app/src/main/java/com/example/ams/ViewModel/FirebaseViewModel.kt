package com.example.ams.ViewModel

import androidx.compose.runtime.*
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ams.data.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class FirebaseViewModel: ViewModel() {

    val allNotification: MutableLiveData<MutableList<RequestCourseModel>> = MutableLiveData()
    val newStudent = mutableStateOf(StudentDetail())
    val newCourseData = mutableStateOf(NewCoureModel())
    val requestData = mutableStateOf(RequestCourseModel())
    val courseNames: MutableLiveData<List<Pair<String, String>>> = MutableLiveData()
    val courseData: MutableLiveData<NewCoureModel> = MutableLiveData()
    val attendanceDetail: MutableLiveData<List<AttendceDetail>> = MutableLiveData()
    private val firestore = Firebase.firestore
    private val storageRef = Firebase.storage.reference
    private val getuser = FirebaseAuth.getInstance().currentUser
    private val currentUserUid = FirebaseAuth.getInstance().currentUser!!.uid

    init {
        fetchClasses()
    }

    fun createNewClass() {
        newCourseData.value.adminId = currentUserUid
        firestore
            .document("$currentUserUid/${newCourseData.value.name}")
            .set(newCourseData.value)
    }

    fun addStudent(courseName: String, adminId: String) {
        var i = 0
        val images = newStudent.value.images
        newStudent.value.images.clear()
        firestore.document("$adminId/$courseName/studentDetails/${newStudent.value.registerNo}")
            .set(newStudent.value)
        images.forEach {
            storageRef
                .child("faces/${newStudent.value.registerNo}/${newStudent.value.name+i}")
                .putFile(it!!)
            i++
        }
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

    private fun fetchClasses() {
        val listOfClasses = mutableListOf<Pair<String, String>>()
        firestore.collection(currentUserUid)
            .get()
            .addOnSuccessListener {  result->
                result?.let {  it1 ->
                    it1.documents.forEach {
                        listOfClasses.add(Pair(it.id, it.data!!.get("adminId").toString()))
                    }
                    courseNames.value = listOfClasses
                }
            }
    }

    fun getCourseDetails(id: String, name: String) {
        firestore.document("$id/$name")
            .get()
            .addOnSuccessListener {
                val data = it.toObject(NewCoureModel::class.java)
                if (data != null) { newCourseData.value = data }
            }
    }

    fun getStudentAtdDetails(courseName: String, adminId: String) {
        val atdDetails = mutableListOf<AttendceDetail>()
        firestore.collection("$adminId/$courseName/tempAttendance")
            .get()
            .addOnSuccessListener { snapShot ->
                val data = snapShot.documents
                data?.let { it ->
                    it.forEach { it1->
                        val eachSTd = mutableListOf<Pair<Int, Boolean>>()
                       it1.data?.forEach { it2->
                           eachSTd.add(Pair(it2.key, it2.value) as Pair<Int, Boolean>)
                       }
                        val atd = AttendceDetail(registerNo = it1.id, attendance = eachSTd)
                        atdDetails.add(atd)
                        attendanceDetail.value = atdDetails
                    }
                }
            }
    }

    fun requestAdmin() {
        requestData.let {
            if (it.value.ClassName != "" && it.value.AdminPhone != "") {
                it.value = it.value.copy(
                    TeacherName = getuser?.displayName.toString(),
                    TeacherPhone = getuser?.phoneNumber.toString(),
                    TeacherUid = currentUserUid
                )
                firestore
                    .collection("Requests/${it.value.AdminPhone}/RequestToImport")
                    .add(it.value)
            }
        }

    }

    fun getAllNotifications() {
        val requests = mutableListOf<RequestCourseModel>()
        firestore.collection("Requests/${getuser?.phoneNumber}/RequestToImport")
            .get()
            .addOnSuccessListener { SnapShot ->
                val documents = SnapShot.documents
                documents?.let {
                    documents.forEach { data ->
                        val doc = data.toObject(RequestCourseModel::class.java)
                        doc?.requestId = data.id
                        requests.add(doc!!)
                    }
                    allNotification.value = requests
                }
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
        firestore.document("$currentUserUid/${data.ClassName}/teacherDetails/sushu")
            .set(teacherDetails)
        getCourseDetails(currentUserUid, data.ClassName)
        firestore
            .document("${data.TeacherUid}/${data.ClassName}")
            .set(courseData)
        firestore.document("Requests/8129697750/RequestToImport/${data.requestId}").delete()
    }

    fun ignoreTeacher(id: String) {
        firestore
            .document("Requests/${getuser?.phoneNumber}/RequestToImport/$id")
            .delete()
    }

    fun updateCourseDetails(name: String) {
        (newCourseData.value).let {
            firestore
                .document("${it.adminId}/$name")
                .update(
                    mapOf(
                        "adminId" to it!!.adminId,
                        "name" to it!!.name,
                        "courseName" to it!!.courseName,
                        "batchFrom" to it!!.batchFrom,
                        "batchTo" to it!!.batchTo,
                        "noAttendace" to it!!.noAttendace
                    )
                )
        }
    }
}