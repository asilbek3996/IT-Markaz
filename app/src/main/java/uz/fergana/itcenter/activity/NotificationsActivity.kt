package uz.fergana.itcenter.activity

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.orhanobut.hawk.Hawk
import uz.fergana.itcenter.NotificationManager
import uz.fergana.itcenter.adapter.NotificationAdapter
import uz.fergana.itcenter.databinding.ActivityNotificationsBinding
import uz.fergana.itcenter.model.Notification
import uz.fergana.itcenter.utils.Constants

class NotificationsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNotificationsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotificationsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loadAdapter()
        binding.back.setOnClickListener {
            finish()
        }
        NotificationManager.notificationsLiveData.observe(this, Observer { notifications ->
            notifications?.let {
                checkNotification()
                loadAdapter(it)
            }
        })
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED) {
                // Ruxsat so'rash
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }else{
                checkNotification()
            }
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            checkNotification()
        }
    }
    fun checkNotification(){
        var a = allNotification()
        var check = 0
        var check2 = 0
        for (i in a){
            if (!i.isRead){
                check++
            }else{
                check2++
            }
        }
        if (check>0) {
            binding.ntRead.visibility = View.VISIBLE

            if (check < 100) {
                binding.tvNotification.text = check.toString()
            } else {
                binding.tvNotification.text = "+99"
            }
        }else{
            binding.ntRead.visibility = View.GONE
        }
    }
    private fun allNotification(): ArrayList<Notification> {
        return Hawk.get(Constants.notification, ArrayList())
    }

    override fun onResume() {
        super.onResume()
        loadAdapter()
        checkNotification()
    }

    fun loadAdapter(it: ArrayList<Notification> = allNotification()){
        binding.notificationRecycler.layoutManager = LinearLayoutManager(this)
        binding.notificationRecycler.adapter = NotificationAdapter(it,this)
    }
}