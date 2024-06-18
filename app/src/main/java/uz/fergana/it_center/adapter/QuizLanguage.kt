package uz.fergana.it_center.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import uz.fergana.it_center.activity.QuizLevelActivity
import uz.fergana.it_center.databinding.QuizItemLayoutBinding
import uz.fergana.it_center.model.CategoryModel

class QuizLanguage(var items: List<CategoryModel>, private val context: Context): RecyclerView.Adapter<QuizLanguage.ItemHolder>() {
    inner class ItemHolder(val binding: QuizItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        return ItemHolder(
            QuizItemLayoutBinding.inflate(
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
        Glide.with(holder.binding.ivItemQuiz)
            .load(item.image)
            .into(holder.binding.ivItemQuiz)
        holder.binding.tvQuizItem.text = item.language
        holder.itemView.setOnClickListener {
            val intent = Intent(context, QuizLevelActivity::class.java)
            intent.putExtra("language", item.language)
            context.startActivity(intent)
            if (context is Activity) {
                context.finish()
            }
        }
    }
}