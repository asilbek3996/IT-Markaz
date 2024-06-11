package com.example.itcenter

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import androidx.work.CoroutineWorker
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.example.itcenter.activity.NotificationDetailActivity
import com.example.itcenter.activity.NotificationsActivity
import com.example.itcenter.api.repository.Repository
import com.example.itcenter.model.Notification
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit

class PollingWorker(context: Context, workerParams: WorkerParameters) : CoroutineWorker(context, workerParams) {
    private val repository = Repository()

    override suspend fun doWork(): Result {
        val successLiveData = MutableLiveData<List<Notification>>()
        val errorLiveData = MutableLiveData<String>()

        withContext(Dispatchers.IO) {
            repository.getNotification(errorLiveData, successLiveData)
        }

        successLiveData.value?.let {
            it.forEach { notification ->
                sendNotification(notification)
            }
        }

        return Result.success()
    }

    private fun sendNotification(message: Notification) {
        val intent = Intent(applicationContext, NotificationsActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(applicationContext, 0, intent,
            PendingIntent.FLAG_IMMUTABLE)

        val channelId = "new_message_channel_id"
        val notificationBuilder = NotificationCompat.Builder(applicationContext, channelId)
            .setSmallIcon(R.drawable.it_center_logo)
            .setContentTitle(message.title)
            .setContentText(message.comment)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        val notificationManager = NotificationManagerCompat.from(applicationContext)
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
                    applicationContext,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED) {
                notificationManager.notify(0, notificationBuilder.build())
            }
        }
    }
}
fun schedulePollingWorker(context: Context) {
    val workRequest = PeriodicWorkRequestBuilder<PollingWorker>(15, TimeUnit.MINUTES)
        .build()
    WorkManager.getInstance(context).enqueue(workRequest)
}