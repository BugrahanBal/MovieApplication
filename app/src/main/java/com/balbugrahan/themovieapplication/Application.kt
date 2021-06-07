package com.balbugrahan.themovieapplication

import androidx.multidex.MultiDexApplication

class Application : MultiDexApplication(){

    companion object {
        private var instance: Application? = null
        fun getInstance(): Application? {
            return instance
        }
    }
    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}