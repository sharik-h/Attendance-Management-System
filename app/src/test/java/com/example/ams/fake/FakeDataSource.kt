package com.example.ams.fake

import com.example.ams.data.DataClasses.*
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.*
import kotlinx.coroutines.tasks.await
import org.junit.Assert
import org.mockito.ArgumentMatchers
import org.mockito.Mockito
import org.mockito.Mockito.`when`

object FakeDataSource {

    const val id1 = "101"
    const val id2 = "103"
    const val name1 = "2nd year"
    const val name2 = "3rd year"
    const val regNo = "fake-reg-number"

    // created  a list of class data format
    val classes = mutableListOf(
        Pair(id1, name1),
        Pair(id2, name2)
    )

    // Create and stores the FirebaseUser Data
    val user = createFakeFirebaseUser()

    // Create and store CourseData
    val courseDetails = NewCoureModel(
        adminId = "fake_admin_id",
        name = "fake-name",
        courseName = "fake-course-name",
        batchFrom = "fake-from",
        batchTo = "fake-to",
        noAttendace = "1"
    )

    // Create and Store a Attendance Data
    val atdDetails = mutableListOf(
        AttendceDetail(
            registerNo = "fake-register-no",
            attendance = mutableListOf(Pair(1, true))
        )
    )
    val atdDetailsSnapshot = createAtdSnapshot()

    // Stores student details
    val studentDetails = StudentDetail(
        name = "fake-name",
        phone = "fake-phone",
        registerNo = "fake-regNo",
        images = mutableListOf()
    )

    // Store a list of students data
    val allStudents = listOf("student-1", "student-2", "student-3")
    val allStudentsSnapshot = createFakeStudentsSnapshot()

    // Create a TeacherData
    val teacherData = mutableListOf(
        TeachersList(
            uid = "teacherId",
            name = "teacher1",
            phone = "1234567890",
            email = "teacher@gmail.com",
            image = null
        )
    )
    val teacherDataSnapshot = createFakeTeacherSnapshot()

    // Create a notification data
    val notificationData = mutableListOf(
        RequestCourseModel(
            requestId = "fake-id",
            ClassName = "fake-class",
            AdminPhone = "phone",
            TeacherUid = "Tuid",
            TeacherName = "Tname",
            TeacherPhone = "Tphone",
            TeacherEmail = "fakeTemail.com"
        )
    )
    val notificationSnapshot = createFakeNotificationSnapshot()

    private fun createFakeNotificationSnapshot(): MutableList<DocumentSnapshot> {
        // Create mock document snapshot
        val notification = Mockito.mock(DocumentSnapshot::class.java)

        // Assign id to created snapshot
        `when`(notification.id).thenReturn("teacherId")

        // Assign value to be returned when toObject() is called on created snapshot
        `when`(notification.toObject(RequestCourseModel::class.java)).thenReturn(FakeDataSource.notificationData.first())

        return mutableListOf(notification)

    }

    private fun createFakeTeacherSnapshot(): MutableList<DocumentSnapshot> {
        //Create mock document snapshot
        val teacher1 = Mockito.mock(DocumentSnapshot::class.java)

        // Assign id to created snapshot
        `when`(teacher1.id).thenReturn("teacherId")

        // Assign value to be returned when toObject() is called on created snapshot
        `when`(teacher1.toObject(TeachersList::class.java)).thenReturn(FakeDataSource.teacherData.first())

        return mutableListOf(teacher1)
    }

    private fun createFakeStudentsSnapshot(): MutableList<DocumentSnapshot> {
        // Create multiple variable that mock Document snapshot
        val student1 = Mockito.mock(DocumentSnapshot::class.java)
        val student2 = Mockito.mock(DocumentSnapshot::class.java)
        val student3 = Mockito.mock(DocumentSnapshot::class.java)

        // Assign id value to all created snapshots
        `when`(student1.id).thenReturn("student-1")
        `when`(student2.id).thenReturn("student-2")
        `when`(student3.id).thenReturn("student-3")

        // create a list containing above variables
        val data = mutableListOf(student1, student2, student3)

        return data
    }

    private fun createFakeFirebaseUser(): FirebaseUser {
        // Mock a FirebaseUser
        val user = Mockito.mock(FirebaseUser::class.java)
        `when`(user.uid).thenReturn("fake-user-id")
        `when`(user.email).thenReturn("fake@example.com")
        `when`(user.phoneNumber).thenReturn("1234567890")
        return user
    }

    private fun createAtdSnapshot(): MutableList<DocumentSnapshot> {
        // Create a mock DocumentSnapshot
        val snapshot = Mockito.mock(DocumentSnapshot::class.java)
        `when`(snapshot.id).thenReturn("fake-register-no")
        `when`(snapshot.data).thenReturn(mutableMapOf("1" to true) as Map<String, Any>?)

        return mutableListOf(snapshot)
    }

}