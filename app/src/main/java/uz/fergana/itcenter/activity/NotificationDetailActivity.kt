package uz.fergana.itcenter.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.orhanobut.hawk.Hawk
import uz.fergana.itcenter.databinding.ActivityNotificationDetailBinding
import uz.fergana.itcenter.model.Notification
import uz.fergana.itcenter.utils.Constants

class NotificationDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNotificationDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotificationDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        var message = intent.getIntExtra("notification",-1)
        var all = allNotification()
        Toast.makeText(this, "$message", Toast.LENGTH_SHORT).show()
        binding.back.setOnClickListener {
            startActivity(Intent(this,NotificationsActivity::class.java))
            finish()
        }
        for (i in all){
            if (i.id==message){
                binding.title.text = i.title
                binding.comment.text = i.comment
                binding.notification.text = i.title
                Glide.with(this).load(i.image).into(binding.imgNotification)
            }
        }
        saveQuestionsToCache(message)
    }
    private fun saveQuestionsToCache(id: Int) {
        var notification = allNotification()
        for (item in notification) {
            if (item.id == id) {
                item.isRead = true
                break
            }
        }
        Hawk.put(Constants.notification, notification)
    }

    private fun allNotification(): ArrayList<Notification> {
        return Hawk.get(Constants.notification, ArrayList())
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this,NotificationsActivity::class.java))
        finish()
    }
}