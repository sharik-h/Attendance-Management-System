package com.example.ams.data.ViewModel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.ams.fake.FakeDataSource
import com.example.ams.fake.FakeFirebaseRepository
import com.example.ams.rules.TestDispatcherRule
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class FirebaseViewModelTest {

    lateinit var firebaseViewModel: FirebaseViewModel
    lateinit var user: FirebaseUser

    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val testDispatcher = TestDispatcherRule()

    @Before
    fun setup() {
        firebaseViewModel = FirebaseViewModel(firebaseRepository = FakeFirebaseRepository())
        user = firebaseViewModel.getuser
    }

    @Test
    @OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
    fun firebaseViewModel_fetchClasses_verifyClassList() =
        runTest {
            assertEquals(firebaseViewModel.courseNames.value, FakeDataSource.classes)
        }

    @Test
    fun firebaseViewModel_getUser_verifyUserData() {
        assertEquals(firebaseViewModel.getuser, FakeDataSource.user)
    }

    @Test
    fun firebaseViewModel_getCourseDetails_verifyWhetherDataIsCorrectlyFetched() {
        val classes = firebaseViewModel.courseNames.value
        firebaseViewModel.getCourseDetails(id = user.uid, name = classes?.first()!!.first)
        assertEquals(firebaseViewModel.newCourseData.value, FakeDataSource.courseDetails)
    }

    @Test
    fun firebaseViewModel_getStudentAtdDetails_verifyIfDataIsFetchedCorrectly() {
        val classes = firebaseViewModel.courseNames.value
        firebaseViewModel.getStudentAtdDetails(
            courseName = classes?.first()!!.first,
            adminId = user.uid
        )
        assertEquals(
            FakeDataSource.atdDetails.first().registerNo,
            firebaseViewModel.attendanceDetail.value!!.first().registerNo
        )
        assertEquals(
            FakeDataSource.atdDetails.size,
            firebaseViewModel.attendanceDetail.value!!.size
        )
    }

    @Test
    fun firebaseViewModel_getStudentAtdDetails_verifyIfStudentDataIsFetchedCorrectly() {
        firebaseViewModel.getStudentDetails(
            courseName = FakeDataSource.name1,
            adminId = user.uid,
            registerNo = FakeDataSource.regNo
        )
        assertEquals(FakeDataSource.studentDetails, firebaseViewModel.newStudent.value)
    }

    @Test
    fun firebaseViewModel_getAllStudents_verifyAllStudents() {
        firebaseViewModel.getAllStudents(adminId = user.uid, courseName = FakeDataSource.name1)
        assertEquals(FakeDataSource.allStudents, firebaseViewModel.studentList.value)
    }

    @Test
    fun firebaseViewModel_getAllTeacherDetails_verifyAllTeacherData() {
        firebaseViewModel.fetchAllTeachersDetails(
            adminId = user.uid,
            courseName = FakeDataSource.name1
        )
        assertEquals(FakeDataSource.teacherData, firebaseViewModel.teacherDetailsList.value!!)
    }

    @Test
    fun firebaseViewModel_getAllNotifications_verifyAllNotifications() {
        firebaseViewModel.getAllNotifications()
        assertEquals(FakeDataSource.notificationData, firebaseViewModel.allNotification.value)
    }
}

