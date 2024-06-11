package com.example.itcenter.activity

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.itcenter.NotificationManager
import com.example.itcenter.R
import com.example.itcenter.adapter.NotificationAdapter
import com.example.itcenter.databinding.ActivityNotificationsBinding
import com.example.itcenter.model.AllStudentModel
import com.example.itcenter.model.Notification
import com.example.itcenter.utils.Constants
import com.orhanobut.hawk.Hawk

class NotificationsActivity : AppCompatActivity() {
    private lateinit var binding:ActivityNotificationsBinding
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