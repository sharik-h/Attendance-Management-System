package com.example.ams.data.ViewModel

import com.example.ams.ViewModel.DefaultFirebaseRepository
import com.example.ams.ViewModel.FirebaseRepository
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

interface AppContainer {
    val firebaseRepository: FirebaseRepository
}

class DefaultAppContainer : AppContainer {
    private val firestore = Firebase.firestore
    private val storage = Firebase.storage.reference

    override val firebaseRepository: FirebaseRepository by lazy {
        DefaultFirebaseRepository(firestore = firestore, storageRef = storage)
    }
}