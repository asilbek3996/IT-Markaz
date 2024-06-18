package uz.fergana.it_center.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import uz.fergana.it_center.activity.QuizActivity
import uz.fergana.it_center.databinding.QuizLevelItemLayoutBinding
import uz.fergana.it_center.model.QuizLevelModel
import uz.fergana.it_center.utils.PrefUtils

interface level {
    fun onItemClicked(position: QuizLevelModel)
}
class QuizLevelAdapter(var items: List<QuizLevelModel>, private val context: Context,var language: String, private val listener: level): RecyclerView.Adapter<QuizLevelAdapter.ItemHolder>() {
    inner class ItemHolder(val binding: QuizLevelItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        return ItemHolder(
            QuizLevelItemLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return items.count()
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        val item = items[position]
        holder.binding.tvMain.text = item.text
        var pref = PrefUtils(holder.itemView.context)
        var level = pref.getQuizLevel()
        holder.itemView.setOnClickListener {
            if (item.id<=level){
                val intent = Intent(context, QuizActivity::class.java)
                intent.putExtra("level",item.id.toString())
                intent.putExtra("language",language)
                context.startActivity(intent)
                if (context is Activity) {
                    context.finish()
            }
        }else{
            listener.onItemClicked(item)
            }
        }
    }
}