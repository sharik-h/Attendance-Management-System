package com.example.ams.ViewModel

import androidx.compose.runtime.*
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ams.data.NewCoureModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class FirebaseViewModel: ViewModel() {

    val newCourseData = mutableStateOf(NewCoureModel())
    val course: MutableLiveData<List<NewCoureModel>> = MutableLiveData<List<NewCoureModel>>()

    private val firestore = Firebase.firestore
    private val currentUser = FirebaseAuth.getInstance().currentUser!!

    init {
        fetchClasses()
    }

    fun createNewClass() {
        firestore
            .document("${currentUser.uid}/${newCourseData.value.name}")
            .set(newCourseData.value)
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