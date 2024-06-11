package com.example.itcenter

import android.content.Intent
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import com.example.itcenter.room.AppDatabase
import com.orhanobut.hawk.Hawk

class App: MultiDexApplication(){
    override fun onCreate() {
        super.onCreate()
        MultiDex.install(this)
        Hawk.init(this).build()
        AppDatabase.initDatabase(this)
        val intent = Intent(this, NotificationService::class.java)
        startService(intent)
        schedulePollingWorker(this)
    }
}