package uz.fergana.it_center.activity

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
import uz.fergana.it_center.NotificationManager
import uz.fergana.it_center.adapter.NotificationAdapter
import uz.fergana.it_center.databinding.ActivityNotificationsBinding
import uz.fergana.it_center.model.Notification
import uz.fergana.it_center.utils.Constants

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
            }
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
        }
    }
    private fun allNotification(): ArrayList<Notification> {
        return Hawk.get(Constants.notification, ArrayList())
    }

    override fun onResume() {
        super.onResume()
        loadAdapter()
    }

    fun loadAdapter(it: ArrayList<Notification> = allNotification()){

        if (it.isEmpty()){
            binding.linearLayout1.visibility = View.VISIBLE
        }else{
            binding.notificationRecycler.layoutManager = LinearLayoutManager(this)
            binding.notificationRecycler.adapter = NotificationAdapter(it,this)
            binding.linearLayout1.visibility = View.GONE
        }
    }
}