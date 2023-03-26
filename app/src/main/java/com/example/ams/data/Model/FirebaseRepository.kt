package com.example.ams.data.Model

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.compose.runtime.MutableState
import com.example.ams.data.DataClasses.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await
import java.time.LocalDate
import java.util.regex.Pattern

interface FirebaseRepository {
    suspend fun getUser(): FirebaseUser?
    suspend fun createNewClass(userId: String, newCourseData: NewCoureModel)
    suspend fun addNewStudent(courseName: String, adminId: String, newStudentData: MutableState<StudentDetail>, noAttendance: Int, studentImages: List<Uri>)
    suspend fun markAttendance(adminId: String, courseName: String, size: Int, registerNo: String, present: Boolean)
    suspend fun updateCourseDetails(name: String, newCourseData: NewCoureModel)
    suspend fun fetchClasses(userId: String):  MutableList<NewCoureModel>
    suspend fun getCourseDetails(id: String, name: String): NewCoureModel?
    suspend fun ignoreTeacher(id: String, phone: String, courseName: String)
    suspend fun getStudentDetails(courseName: String, adminId: String, registerNo: String): StudentDetail?
    suspend fun getStudentAtdDetails(courseName: String, adminId: String): MutableList<DocumentSnapshot>?
    suspend fun getAllStudents(adminId: String, courseName: String): MutableList<DocumentSnapshot>
    suspend fun fetchAllTeachersDetails(adminId: String, courseName: String):  MutableList<DocumentSnapshot>
    suspend fun requestAdmin(data: RequestCourseModel)
    suspend fun acceptTeacher(data: RequestCourseModel, adminId: String, phone: String, courseData: NewCoureModel, teacherDetails: TeachersList)
    suspend fun getAllRequests(adminId: String, courseName: String): MutableList<DocumentSnapshot>
    suspend fun getAllNotifications(courseName: String, userId: String): MutableList<DocumentSnapshot>
    fun createNewNotification(notificationData: MutableState<NotificationModel>, courseName: String, adminId: String)
    suspend fun deleteNotification(adminId: String, courseName: String, notificationId: String)
    suspend fun updateNotificatoin(adminId: String, courseName: String, data: MutableState<NotificationModel>)
    suspend fun getToatlAtd(courseName: String, adminId: String): Int
    suspend fun getPeriodNo(courseName: String, adminId: String): Int
    suspend fun updatePeriod(adminId: String, courseName: String, periodNo: Int)
    fun markRealAtd(adminId: String, courseName: String, atdList: List<Pair<String, Int>>, date: LocalDate?)
    suspend fun getStdRealAtd(courseName: String, adminId: String): MutableList<DocumentSnapshot>
    suspend fun getStdRealAtdDates(adminId: String, courseName: String): List<String>
    suspend fun getTotalNoStd(adminId: String, courseName: String): Int
    suspend fun getTotalNoTeacher(adminId: String, courseName: String): Int
    suspend fun getAdminData(adminId: String, courseName: String, phone: String?): DocumentSnapshot
    suspend fun getAllStudentData(adminId: String, courseName: String): MutableList<DocumentSnapshot>
    suspend fun getAllImages(adminId: String, courseName: String, registerNos: List<String>, callback: (MutableList<Pair<String, Bitmap>>) -> Unit)
    suspend fun addStudentImg(courseName: String, adminId: String, regNo: String, name: String, img: Uri)
    suspend fun addAdminAsTeacher(adminId: String, courseName: String, adminInfo: TeachersList)
    fun updateAStdAttendance(adminId: String, courseName: String, regNo: String, atd: Int)
    suspend fun getAllStudentAtd(adminId: String, courseName: String): MutableList<DocumentSnapshot>
    suspend fun getStudentRequest(courseName: String, adminId: String): MutableList<DocumentSnapshot>
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
        noAttendance: Int,
        studentImages: List<Uri>
        ) {
        val atdHashmap = (1..noAttendance).map{ "$it" }.associateWith { false }
        var i = 0
        val images = studentImages
        firestore
            .document("$adminId/$courseName/studentDetails/${newStudentData.value.registerNo}")
            .set(newStudentData.value)
        images.forEach {
            storageRef
                .child("$adminId/$courseName/${newStudentData.value.registerNo}/${newStudentData.value.name+i}")
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

    override suspend fun fetchClasses(userId: String): MutableList<NewCoureModel> {
        val listOfClasses = mutableListOf<NewCoureModel>()
        val deferred = CompletableDeferred<MutableList<NewCoureModel>>()
        withContext(Dispatchers.IO){
        firestore.collection(userId)
            .addSnapshotListener {  result, _ ->
                result?.let {  it1 ->
                    it1.documents.forEach {
                        val data = it.toObject(NewCoureModel::class.java)!!
                        if (!listOfClasses.contains(data)) {
                            listOfClasses.add(data)
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

    override suspend fun ignoreTeacher(id: String, phone: String, courseName: String) {
        firestore.document("$id/$courseName/Requests/$phone")
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
            .document("${data.adminId}/${data.className}/Requests/${data.teacherPhone}")
            .set(data)
    }

    override suspend fun acceptTeacher(
        data: RequestCourseModel,
        adminId: String,
        phone: String,
        courseData: NewCoureModel,
        teacherDetails: TeachersList
    ) {
        firestore.document("$adminId/${data.className}/teacherDetails/${teacherDetails.phone}")
            .set(teacherDetails)
        getCourseDetails(adminId, data.className)
        firestore
            .document("${data.teacherUid}/${data.className}")
            .set(courseData)
        firestore.document("${data.adminId}/${data.className}/Requests/${teacherDetails.phone}")
            .delete()
    }

    override suspend fun getAllRequests(adminId: String, phone: String): MutableList<DocumentSnapshot> {
        val ref = firestore.collection("$adminId/$phone/Requests")
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
        courseName: String,
        adminId: String
    ) {
        firestore
            .collection("$adminId/$courseName/Notifications")
            .add(notificationData.value)
    }

    override suspend fun deleteNotification(
        adminId: String,
        courseName: String,
        notificationId: String
    ) {
        firestore
            .document("$adminId/$courseName/Notifications/$notificationId")
            .delete()
    }

    override suspend fun updateNotificatoin(
        adminId: String,
        courseName: String,
        data: MutableState<NotificationModel>
    ) {
        firestore.document("$adminId/$courseName/Notifications/${data.value.notificationId}")
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

    override suspend fun getTotalNoStd(adminId: String, courseName: String): Int {
        val ref = firestore.collection("$adminId/$courseName/studentDetails")
        return ref.get().await().documents.size
    }

    override suspend fun getTotalNoTeacher(adminId: String, courseName: String): Int {
        val ref = firestore.collection("$adminId/$courseName/teacherDetails")
        return ref.get().await().documents.size
    }

    override suspend fun getAdminData(adminId: String, courseName: String, phone: String?): DocumentSnapshot {
        val ref = firestore.document("$adminId/$courseName/teacherDetails/$phone")
        return ref.get().await()
    }

    override suspend fun getAllStudentData(
        adminId: String,
        courseName: String
    ): MutableList<DocumentSnapshot> {
        val ref = firestore.collection("$adminId/$courseName/studentDetails")
        return ref.get().await().documents
    }

    override suspend fun getAllImages(
        adminId: String,
        courseName: String,
        registerNos: List<String>,
        callback: (MutableList<Pair<String, Bitmap>>) -> Unit
    ) {
        val imgFromServer =  mutableListOf<Pair<String, Bitmap>>()
        val imgName = mutableListOf<String>()
        val totalfile = registerNos.size
        var totalfetched = 0
        registerNos.forEach {
            totalfetched++
            storageRef
                .child("$adminId/$courseName/$it")
                .listAll()
                .addOnSuccessListener { result ->
                    for (item in result.items){
                        if (!imgName.contains(item.name)){
                            imgName.add(item.name)
                        }
                        GlobalScope.launch(Dispatchers.Main) {
                            val byteArr = item.getBytes(Long.MAX_VALUE).await()
                            val image = BitmapFactory.decodeByteArray(byteArr , 0, byteArr.size)
                            imgFromServer.add(Pair(item.name, image))
                            if (imgFromServer.size == imgName.size && totalfile == totalfetched) {
                                callback(imgFromServer)
                            }
                        }
                    }
                }
        }
    }

    override suspend fun addStudentImg(
        courseName: String,
        adminId: String,
        regNo: String,
        name: String,
        img: Uri
    ) {
        val lastSize = Pattern.compile("\\D+").matcher(name).replaceAll("") + 1
        val stdname = Pattern.compile("\\d+").matcher(name).replaceAll("")
        storageRef
            .child("$adminId/$courseName/$regNo/$stdname$lastSize")
            .putFile(img)
    }

    override suspend fun addAdminAsTeacher(
        adminId: String,
        courseName: String,
        adminInfo: TeachersList
    ) {
        firestore.document("$adminId/$courseName/teacherDetails/admin")
            .set(adminInfo)
    }

    override fun updateAStdAttendance(
        adminId: String,
        courseName: String,
        regNo: String,
        atd: Int
    ) {
        firestore.document("$adminId/$courseName/studentDetails/$regNo")
            .get()
            .addOnSuccessListener {
                val total  = it.toObject(StudentDetail::class.java)?.totoalAtd
                if (total != null) {
                    val newAtd = if (atd == 2) { total.plus(1) }
                        else if(atd == 1){ total.plus(0.5) }
                        else{ total }
                    firestore.document("$adminId/$courseName/studentDetails/$regNo")
                        .update(mapOf("totalAtd" to newAtd))
                }
            }
    }



    override suspend fun getAllStudentAtd(
        adminId: String,
        courseName: String
    ): MutableList<DocumentSnapshot> {
        val ref = firestore.collection("$adminId/$courseName/studentDetails")
        return ref.get().await().documents
    }

    override suspend fun getStudentRequest(
        courseName: String,
        adminId: String
    ): MutableList<DocumentSnapshot> {
        val ref = firestore.collection("$adminId/$courseName/studentRequest")
        return ref.get().await().documents
    }
}