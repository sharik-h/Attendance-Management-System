package com.example.ams.data.ViewModel

import android.app.Application

class FirebaseApplication: Application() {
    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer()
    }
}