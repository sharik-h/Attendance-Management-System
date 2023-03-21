package com.example.ams.MainPages.Attendance

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.lifecycle.MutableLiveData
import com.example.ams.data.DataClasses.AttendceDetail
import com.example.ams.data.DataClasses.NewCoureModel
import com.example.ams.data.Model.DefaultFirebaseRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.*
import java.time.LocalDate

class MarkDailyAtd: BroadcastReceiver() {

    val attendanceDetail: MutableLiveData<List<AttendceDetail>> = MutableLiveData()
    val courseNames: MutableLiveData<List<NewCoureModel>> = MutableLiveData()

    val firestore = Firebase.firestore
    val storage = Firebase.storage.reference
    val firebaseRepository = DefaultFirebaseRepository(firestore = firestore, storageRef = storage)

    override fun onReceive(context: Context?, intent: Intent?) {
        CoroutineScope(Dispatchers.Main).launch {
            val uid = FirebaseAuth.getInstance().currentUser?.uid
            markTodayAtd(adminId = uid!!)
        }
    }


    suspend fun markTodayAtd(adminId: String) {
        courseNames.value = firebaseRepository.fetchClasses(userId = adminId)

        courseNames.value?.forEach {


            // get all student attendance in a particalar class from firestore
            if (it.adminId == adminId) {
                val atdDetails = mutableListOf<AttendceDetail>()
                val data = firebaseRepository.getStudentAtdDetails(courseName = it.name, adminId = adminId)
                data?.forEach {
                    val eachSTd = mutableListOf<Pair<Int, Boolean>>()
                    it.data?.forEach { it2->
                        eachSTd.add(Pair(it2.key, it2.value) as Pair<Int, Boolean>)
                    }
                    val atd = AttendceDetail(registerNo = it.id, attendance = eachSTd)
                    atdDetails.add(atd)
                    attendanceDetail.value = atdDetails
                }


                val totalAtd = firebaseRepository.getToatlAtd(adminId = adminId, courseName = it.name)
                val periodNo = firebaseRepository.getPeriodNo(adminId = adminId, courseName = it.name)
                val atdList = mutableListOf<Pair<String, Int>>()

                if (periodNo == totalAtd) {
                    attendanceDetail.value?.forEach { std ->
                        var total = 0
                        var atd = 0
                        std.attendance.forEach {
                            if (it.second) {
                                total++
                            }
                        }
                        if (totalAtd == total) atd = 2
                        else if (totalAtd / 2 <= total) atd = 1
                        else atd = 0
                        atdList.add(Pair(std.registerNo, atd))
                    }
                    firebaseRepository.markRealAtd(
                        adminId = adminId,
                        courseName = it.name,
                        atdList = atdList,
                        date = LocalDate.now()
                    )
                    atdList.forEach {std ->
                        firebaseRepository.updateAStdAttendance(
                            adminId = adminId,
                            courseName = it.name,
                            regNo = std.first,
                            atd = std.second
                        )
                    }
                    firebaseRepository.updatePeriod(
                        adminId = adminId,
                        courseName = it.name,
                        periodNo = periodNo + 1
                    )
                }
            }
        }
    }
}