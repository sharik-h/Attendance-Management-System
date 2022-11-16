package com.example.ams.ViewModel

import androidx.compose.runtime.*
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ams.data.NewCoureModel
import com.example.ams.data.StudentDetail
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class FirebaseViewModel: ViewModel() {

    val newStudent = mutableStateOf(StudentDetail())
    val newCourseData = mutableStateOf(NewCoureModel())
    val course: MutableLiveData<List<NewCoureModel>> = MutableLiveData<List<NewCoureModel>>()

    private val firestore = Firebase.firestore
    private val storageRef = Firebase.storage.reference
    private val currentUser = FirebaseAuth.getInstance().currentUser!!

    init {
        fetchClasses()
    }

    fun createNewClass() {
        firestore
            .document("${currentUser.uid}/${newCourseData.value.name}")
            .set(newCourseData.value)
    }

    fun addStudent() {
        var i = 0
        val studentDetails = hashMapOf(
            "name" to newStudent.value.name,
            "phone" to newStudent.value.phone,
            "registerNo" to newStudent.value.registerNo
        )
        firestore.document("${currentUser.uid}/${newStudent.value.name}")
            .update("studentsList", studentDetails)
        newStudent.value.images.forEach {
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
                "batchFrom" -> it.value = it.value.copy(bactchFrom = value)
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

    fun fetchClasses() {
        firestore.collection(currentUser.uid)
            .get()
            .addOnSuccessListener {  it->
                it?.let {  it1 ->
                    val documents = it1.documents
                    val listOfClasses = mutableListOf<NewCoureModel>()
                    documents.forEach {
                        val data = it.toObject(NewCoureModel::class.java)
                        if (data!!.courseName != "") {
                            listOfClasses.add(data)
                        }
                    }
                    course.value = listOfClasses
                }
            }
    }
}