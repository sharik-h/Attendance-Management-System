package com.example.ams.ViewModel

import android.app.Application

class FirebaseApplication: Application() {
    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer()
    }
}