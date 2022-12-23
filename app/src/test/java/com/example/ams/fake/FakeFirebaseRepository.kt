package com.example.ams.fake

import androidx.compose.runtime.MutableState
import com.example.ams.data.DataClasses.NewCoureModel
import com.example.ams.data.DataClasses.RequestCourseModel
import com.example.ams.data.DataClasses.StudentDetail
import com.example.ams.data.DataClasses.TeachersList
import com.example.ams.data.Model.FirebaseRepository
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentSnapshot

class FakeFirebaseRepository: FirebaseRepository {
    override suspend fun fetchClasses(userId: String): MutableList<Pair<String, String>> {
        return FakeDataSource.classes
    }

    override suspend fun getUser(): FirebaseUser? {
        return FakeDataSource.user
    }

    override suspend fun createNewClass(userId: String, newCourseData: NewCoureModel) {
        TODO("Not yet implemented")
    }

    override suspend fun addNewStudent(
        courseName: String,
        adminId: String,
        newStudentData: MutableState<StudentDetail>
    ) {
        TODO("Not yet implemented")
    }

    override suspend fun markAttendance(
        adminId: String,
        courseName: String,
        size: Int,
        registerNo: String,
        present: Boolean
    ) {
        TODO("Not yet implemented")
    }

    override suspend fun updateCourseDetails(name: String, newCourseData: NewCoureModel) {
        TODO("Not yet implemented")
    }

    override suspend fun getCourseDetails(id: String, name: String): NewCoureModel? {
       return FakeDataSource.courseDetails
    }

    override suspend fun ignoreTeacher(id: String, phone: String) {
        TODO("Not yet implemented")
    }

    override suspend fun getStudentDetails(
        courseName: String,
        adminId: String,
        registerNo: String
    ): StudentDetail? {
        return FakeDataSource.studentDetails
    }

    override suspend fun getStudentAtdDetails(
        courseName: String,
        adminId: String
    ): MutableList<DocumentSnapshot> {
        return FakeDataSource.atdDetailsSnapshot
    }

    override suspend fun getAllStudents(
        adminId: String,
        courseName: String
    ): MutableList<DocumentSnapshot> {
        return FakeDataSource.allStudentsSnapshot
    }

    override suspend fun fetchAllTeachersDetails(
        adminId: String,
        courseName: String
    ): MutableList<DocumentSnapshot> {
        return FakeDataSource.teacherDataSnapshot
    }

    override suspend fun requestAdmin(data: RequestCourseModel) {
        TODO("Not yet implemented")
    }

    override suspend fun acceptTeacher(
        data: RequestCourseModel,
        userId: String,
        phone: String,
        courseData: NewCoureModel,
        teacherDetails: TeachersList
    ) {
        TODO("Not yet implemented")
    }

    override suspend fun getAllNotifications(phone: String): MutableList<DocumentSnapshot> {
        return FakeDataSource.notificationSnapshot
    }
}