package com.example.ams.ViewModel

import androidx.compose.runtime.*
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ams.data.AttendceDetail
import com.example.ams.data.NewCoureModel
import com.example.ams.data.StudentDetail
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class FirebaseViewModel: ViewModel() {

    val newStudent = mutableStateOf(StudentDetail())
    val requestData = mutableStateOf(RequestCourseModel())
    val newCourseData = mutableStateOf(NewCoureModel())
    val courseNames: MutableLiveData<List<String>> = MutableLiveData()
    val attendanceDetail: MutableLiveData<List<AttendceDetail>> = MutableLiveData()
    private val firestore = Firebase.firestore
    private val storageRef = Firebase.storage.reference
    private val currentUserUid = FirebaseAuth.getInstance().currentUser!!.uid

    init {
        fetchClasses()
    }

    fun createNewClass() {
        firestore
            .document("$currentUserUid/${newCourseData.value.name}")
            .set(newCourseData.value)
    }

    fun addStudent(courseName: String) {
        var i = 0
        val images = newStudent.value.images
        newStudent.value.images.clear()
        firestore.document("$currentUserUid/$courseName/studentDetails/${newStudent.value.registerNo}")
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

    }

    private fun fetchClasses() {
        val listOfClasses = mutableListOf<String>()
        firestore.collection(currentUserUid)
            .get()
            .addOnSuccessListener {  result->
                result?.let {  it1 ->
                    it1.documents.forEach {
                        listOfClasses.add(it.id)
                    }
                    courseNames.value = listOfClasses
                }
            }
    }

    fun getStudentAtdDetails(courseName: String) {
        val atdDetails = mutableListOf<AttendceDetail>()
        firestore.collection("$currentUserUid/$courseName/tempAttendance")
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
}