package uz.fergana.it_center

import android.content.Intent
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import com.orhanobut.hawk.Hawk
import uz.fergana.it_center.room.AppDatabase

class App: MultiDexApplication(){
    override fun onCreate() {
        super.onCreate()
        MultiDex.install(this)
        Hawk.init(this).build()
        AppDatabase.initDatabase(this)
        val intent = Intent(this, uz.fergana.it_center.NotificationService::class.java)
        startService(intent)
        uz.fergana.it_center.schedulePollingWorker(this)
    }
}