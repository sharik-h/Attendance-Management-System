package com.example.ams.data.Model

import androidx.compose.runtime.MutableState
import com.example.ams.data.DataClasses.NewCoureModel
import com.example.ams.data.DataClasses.RequestCourseModel
import com.example.ams.data.DataClasses.StudentDetail
import com.example.ams.data.DataClasses.TeachersList
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.tasks.await

interface FirebaseRepository {
    suspend fun getUser(): FirebaseUser?
    suspend fun createNewClass(userId: String, newCourseData: NewCoureModel)
    suspend fun addNewStudent(courseName: String, adminId: String, newStudentData: MutableState<StudentDetail>)
    suspend fun markAttendance(adminId: String, courseName: String, size: Int, registerNo: String, present: Boolean)
    suspend fun updateCourseDetails(name: String, newCourseData: NewCoureModel)
    suspend fun fetchClasses(userId: String):  MutableList<Pair<String, String>>
    suspend fun getCourseDetails(id: String, name: String): NewCoureModel?
    suspend fun ignoreTeacher(id: String, phone: String)
    suspend fun getStudentDetails(courseName: String, adminId: String, registerNo: String): StudentDetail?
    suspend fun getStudentAtdDetails(courseName: String, adminId: String): MutableList<DocumentSnapshot>?
    suspend fun getAllStudents(adminId: String, courseName: String): MutableList<DocumentSnapshot>
    suspend fun fetchAllTeachersDetails(adminId: String, courseName: String):  MutableList<DocumentSnapshot>
    suspend fun requestAdmin(data: RequestCourseModel)
    suspend fun acceptTeacher(data: RequestCourseModel, userId: String, phone: String, courseData: NewCoureModel, teacherDetails: TeachersList)
    suspend fun getAllNotifications(phone: String): MutableList<DocumentSnapshot>
}

class DefaultFirebaseRepository(
    private val firestore: FirebaseFirestore,
    private val storageRef: StorageReference
    ): FirebaseRepository {

    override suspend fun getUser(): FirebaseUser? {
       return FirebaseAuth.getInstance().currentUser
    }

    override suspend fun createNewClass(userId: String, newCourseData: NewCoureModel) {
        firestore
            .document("$userId/${newCourseData.name}")
            .set(newCourseData)
    }


    override suspend fun addNewStudent(courseName: String, adminId: String, newStudentData: MutableState<StudentDetail>) {
        var i = 0
        val images = newStudentData.value.images
        newStudentData.value.images.clear()
        firestore
            .document("$adminId/$courseName/studentDetails/${newStudentData.value.registerNo}")
            .set(newStudentData.value)
        images.forEach {
            storageRef
                .child("faces/${newStudentData.value.registerNo}/${newStudentData.value.name+i}")
                .putFile(it!!)
            i++
        }
        firestore.document("$adminId/$courseName/tempAttendance/${newStudentData.value.registerNo}")
            .set(hashMapOf("1" to false))
    }

    override suspend fun markAttendance(
        adminId: String,
        courseName: String,
        size: Int,
        registerNo: String,
        present: Boolean
    ) {
        firestore.document("$adminId/$courseName/tempAttendance/$registerNo")
            .update("$size", present)
    }

    override suspend fun updateCourseDetails(name: String, newCourseData: NewCoureModel) {
        newCourseData.let {
            firestore
                .document("${it.adminId}/$name")
                .update(
                    mapOf(
                        "adminId" to it.adminId,
                        "name" to it.name,
                        "courseName" to it.courseName,
                        "batchFrom" to it.batchFrom,
                        "batchTo" to it.batchTo,
                        "noAttendace" to it.noAttendace
                    )
                )
        }
    }

    override suspend fun fetchClasses(userId: String): MutableList<Pair<String, String>> {
        val listOfClasses = mutableListOf<Pair<String, String>>()
        firestore.collection(userId)
            .addSnapshotListener {  result, _ ->
                result?.let {  it1 ->
                    it1.documents.forEach {
                        if (!listOfClasses.contains(Pair(it.id, it.data!!["adminId"].toString()))) {
                            listOfClasses.add(Pair(it.id, it.data!!["adminId"].toString()))
                        }
                    }
//                    courseNames.value = listOfClasses
                }
            }
        return listOfClasses
    }

    override suspend fun getCourseDetails(id: String, name: String): NewCoureModel? {
        val ref = firestore.document("$id/$name")
        return ref.get().await().toObject(NewCoureModel::class.java)
    }

    override suspend fun ignoreTeacher(id: String, phone: String) {
        firestore.document("Requests/$phone/RequestToImport/$id")
            .delete()
    }

    override suspend fun getStudentDetails(courseName: String, adminId: String, registerNo: String): StudentDetail? {
        val ref = firestore.document("$adminId/$courseName/studentDetails/$registerNo")
        return ref.get().await().toObject(StudentDetail::class.java)

    }

    override suspend fun getStudentAtdDetails(courseName: String, adminId: String): MutableList<DocumentSnapshot> {
      val ref = firestore.collection("$adminId/$courseName/tempAttendance")
        return ref.get().await().documents
    }

    override suspend fun getAllStudents(adminId: String, courseName: String): MutableList<DocumentSnapshot> {
        val ref = firestore.collection("$adminId/$courseName/studentDetails")
        return  ref.get().await().documents
    }

    override suspend fun fetchAllTeachersDetails(adminId: String, courseName: String): MutableList<DocumentSnapshot> {
        val ref = firestore.collection("$adminId/$courseName/teacherDetails")
        return ref.get().await().documents
    }

    override suspend fun requestAdmin(data: RequestCourseModel) {
        firestore
            .collection("Requests/${data.AdminPhone}/RequestToImport")
            .add(data)
    }

    override suspend fun acceptTeacher(
        data: RequestCourseModel,
        userId: String,
        phone: String,
        courseData: NewCoureModel,
        teacherDetails: TeachersList
    ) {
        firestore.document("$userId/${data.ClassName}/teacherDetails/${courseData.name}")
            .set(teacherDetails)
        getCourseDetails(userId, data.ClassName)
        firestore
            .document("${data.TeacherUid}/${data.ClassName}")
            .set(courseData)
        firestore.document("Requests/$phone/RequestToImport/${data.requestId}").delete()
    }

    override suspend fun getAllNotifications(phone: String): MutableList<DocumentSnapshot> {
        val ref = firestore.collection("Requests/$phone/RequestToImport")
        return ref.get().await().documents
    }
}