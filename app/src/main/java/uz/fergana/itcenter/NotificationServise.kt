package uz.fergana.itcenter

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import uz.fergana.itcenter.activity.NotificationsActivity
import uz.fergana.itcenter.api.repository.Repository
import uz.fergana.itcenter.model.Notification

class NotificationService : Service() {

    private val repository = Repository()
    private val handler = Handler(Looper.getMainLooper())
    private val pollingInterval = 1000L

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startPolling()
        return START_STICKY
    }

    private fun startPolling() {
        handler.post(object : Runnable {
            override fun run() {
                fetchNotifications()
                handler.postDelayed(this, pollingInterval)
            }
        })
    }

    private fun fetchNotifications() {
        val errorLiveData = MutableLiveData<String>()
        val successLiveData = MutableLiveData<List<Notification>>()

        repository.getNotification(errorLiveData, successLiveData)

        successLiveData.observeForever { notifications ->
            notifications?.let {
                it.forEach { notification ->
                    sendNotification(notification)
                }
            }
        }
    }


    private fun sendNotification(message: Notification) {
        val intent = Intent(this, NotificationsActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent,
            PendingIntent.FLAG_IMMUTABLE)

        val channelId = "new_message_channel_id"
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.it_center_logo)
            .setContentTitle(message.title)
            .setContentText(message.comment)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        val notificationManager = NotificationManagerCompat.from(this)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "New Message Channel",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED) {
                notificationManager.notify(0, notificationBuilder.build())
            }
        }
    }


    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)
    }
}
