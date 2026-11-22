package com.example.dogbreedsgallery

import android.app.Application

class DogBreedsApplication : Application() {

    lateinit var dependencies: AppDependencies
        private set

    override fun onCreate() {
        super.onCreate()
        dependencies = AppDependencies(this)
    }
}