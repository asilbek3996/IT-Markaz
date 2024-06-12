package uz.fergana.itcenter

import androidx.lifecycle.MutableLiveData
import com.orhanobut.hawk.Hawk
import uz.fergana.itcenter.model.Notification
import uz.fergana.itcenter.utils.Constants

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