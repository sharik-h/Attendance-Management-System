package com.example.ams.data.Model

import android.util.Log
import androidx.compose.runtime.MutableState
import com.example.ams.data.DataClasses.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.time.LocalDate

interface FirebaseRepository {
    suspend fun getUser(): FirebaseUser?
    suspend fun createNewClass(userId: String, newCourseData: NewCoureModel)
    suspend fun addNewStudent(courseName: String, adminId: String, newStudentData: MutableState<StudentDetail>, noAttendance: Int)
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
    suspend fun getAllRequests(phone: String): MutableList<DocumentSnapshot>
    suspend fun getAllNotifications(courseName: String, userId: String): MutableList<DocumentSnapshot>
    fun createNewNotification(notificationData: MutableState<NotificationModel>, courseName: String)
    suspend fun deleteNotification(userId: String, courseName: String, notificationId: String)
    suspend fun updateNotificatoin(userId: String, courseName: String, data: MutableState<NotificationModel>)
    suspend fun getToatlAtd(courseName: String, adminId: String): Int
    suspend fun getPeriodNo(courseName: String, adminId: String): Int
    suspend fun updatePeriod(adminId: String, courseName: String, periodNo: Int)
    fun markRealAtd(adminId: String, courseName: String, atdList: List<Pair<String, Int>>, date: LocalDate?)
    suspend fun getStdRealAtd(courseName: String, adminId: String): MutableList<DocumentSnapshot>
    suspend fun getStdRealAtdDates(adminId: String, courseName: String): List<String>
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


    override suspend fun addNewStudent(
        courseName: String,
        adminId: String,
        newStudentData: MutableState<StudentDetail>,
        noAttendance: Int
    ) {
        val atdHashmap = (1..noAttendance).map{ "$it" }.associateWith { false }
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
            .set(atdHashmap)
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
        val deferred = CompletableDeferred<MutableList<Pair<String, String>>>()
        withContext(Dispatchers.IO){
        firestore.collection(userId)
            .addSnapshotListener {  result, _ ->
                result?.let {  it1 ->
                    it1.documents.forEach {
                        if (!listOfClasses.contains(Pair(it.id, it.data!!["adminId"].toString()))) {
                            listOfClasses.add(Pair(it.id, it.data!!["adminId"].toString()))
                        }
                    }
                    deferred.complete(listOfClasses)
                }
            }
        }
        return deferred.await()
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

    override suspend fun getAllRequests(phone: String): MutableList<DocumentSnapshot> {
        val ref = firestore.collection("Requests/$phone/RequestToImport")
        return ref.get().await().documents
    }

    override suspend fun getAllNotifications(
        courseName: String,
        userId: String
    ): MutableList<DocumentSnapshot> {
        val ref = firestore.collection("$userId/$courseName/Notifications")
        return ref.get().await().documents
    }

    override fun createNewNotification(
        notificationData: MutableState<NotificationModel>,
        courseName: String
    ) {
        firestore
            .collection("${notificationData.value.id}/$courseName/Notifications")
            .add(notificationData.value)
    }

    override suspend fun deleteNotification(
        userId: String,
        courseName: String,
        notificationId: String
    ) {
        firestore
            .document("$userId/$courseName/Notifications/$notificationId")
            .delete()
    }

    override suspend fun updateNotificatoin(
        userId: String,
        courseName: String,
        data: MutableState<NotificationModel>
    ) {
        firestore.document("$userId/$courseName/Notifications/${data.value.notificationId}")
            .update(mapOf(
                "heading" to data.value.heading,
                "discription" to data.value.discription,
                "date" to data.value.date,
                "id" to data.value.id
            ))
    }

    override suspend fun getToatlAtd(courseName: String, adminId: String): Int {
        val ref = firestore.document("$adminId/$courseName")
        return ref.get().await().toObject(NewCoureModel::class.java)?.noAttendace?.toInt() ?: 0
    }

    override suspend fun getPeriodNo(courseName: String, adminId: String): Int {
        val ref = firestore.document("$adminId/$courseName")
        return ref.get().await().toObject(NewCoureModel::class.java)?.periodNo?.toInt() ?: 0
    }

    override suspend fun updatePeriod(adminId: String, courseName: String, periodNo: Int) {
        firestore.document("$adminId/$courseName").update(mapOf("periodNo" to "$periodNo"))
    }

    override fun markRealAtd(
        adminId: String,
        courseName: String,
        atdList: List<Pair<String, Int>>,
        date: LocalDate?
    ) {
      firestore.document("$adminId/$courseName/Attendance/$date")
          .set(atdList.toMap())
    }

    override suspend fun getStdRealAtd(courseName: String, adminId: String): MutableList<DocumentSnapshot> {
        val ref = firestore.collection("$adminId/$courseName/Attendance")
        return ref.get().await().documents
    }

    override suspend fun getStdRealAtdDates(adminId: String, courseName: String): List<String> {
        val date = mutableListOf<String>()
        firestore
            .collection("$adminId/$courseName/Attendance")
            .get()
            .addOnSuccessListener {
                it.documents.forEach {
                    date.add(it.id)
                }
            }.await()
        return date
    }
}