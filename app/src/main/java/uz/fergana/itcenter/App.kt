package uz.fergana.itcenter

import android.content.Intent
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import com.orhanobut.hawk.Hawk
import uz.fergana.itcenter.room.AppDatabase

class App: MultiDexApplication(){
    override fun onCreate() {
        super.onCreate()
        MultiDex.install(this)
        Hawk.init(this).build()
        AppDatabase.initDatabase(this)
        val intent = Intent(this, uz.fergana.itcenter.NotificationService::class.java)
        startService(intent)
        uz.fergana.itcenter.schedulePollingWorker(this)
    }
}