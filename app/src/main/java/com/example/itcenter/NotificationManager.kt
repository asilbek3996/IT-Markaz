package com.example.itcenter

import androidx.lifecycle.MutableLiveData
import com.example.itcenter.model.Notification
import com.example.itcenter.utils.Constants
import com.orhanobut.hawk.Hawk

object NotificationManager {

    val notificationsLiveData: MutableLiveData<ArrayList<Notification>> = MutableLiveData()

    fun saveNotifications(notifications: ArrayList<Notification>) {
        Hawk.put(Constants.notification, notifications)
        notificationsLiveData.postValue(notifications)
    }

    fun getNotifications(): List<Notification>? {
        return Hawk.get(Constants.notification)
    }

    fun clearNotifications() {
        Hawk.deleteAll()
        notificationsLiveData.postValue(ArrayList())
    }
}