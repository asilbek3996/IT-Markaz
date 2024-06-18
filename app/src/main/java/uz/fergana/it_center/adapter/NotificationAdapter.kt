package uz.fergana.it_center.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import uz.fergana.it_center.activity.NotificationDetailActivity
import uz.fergana.it_center.databinding.NotificationItemLayoutBinding
import uz.fergana.it_center.model.Notification
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class NotificationAdapter(var items: ArrayList<Notification>, private val context: Context) : RecyclerView.Adapter<NotificationAdapter.Viewholder>() {

    inner class Viewholder(val binding: NotificationItemLayoutBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Viewholder {
        val inflate = LayoutInflater.from(parent.context)
        val binding = NotificationItemLayoutBinding.inflate(inflate, parent, false)
        return Viewholder(binding)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: Viewholder, position: Int) {
        val item = items[position]
        val binding = holder.binding

        if (item.isRead) {
            binding.ntRead.visibility = View.GONE
        } else {
            binding.ntRead.visibility = View.VISIBLE
        }

        val formattedDate = formatDateTime(item.createdAt)
        binding.date.text = formattedDate
        binding.notificationTxt.text = item.title

        val intent = Intent(holder.itemView.context, NotificationDetailActivity::class.java)
        holder.itemView.setOnClickListener {
            intent.putExtra("notification", item.id)
            it.context.startActivity(intent)
            if (context is Activity) {
                context.finish()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun formatDateTime(datetime: String): String {
        val formatter = DateTimeFormatter.ISO_DATE_TIME
        val parsedDate = LocalDateTime.parse(datetime, formatter)

        val year = parsedDate.year
        val month = parsedDate.monthValue
        val day = parsedDate.dayOfMonth

        return "$day.$month.$year"
    }
}
