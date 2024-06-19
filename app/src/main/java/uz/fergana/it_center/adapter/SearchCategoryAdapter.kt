package uz.fergana.it_center.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import uz.fergana.it_center.activity.DarslarActivity
import uz.fergana.it_center.databinding.AllCategoryItemLayoutBinding
import uz.fergana.it_center.model.CategoryModel

class SearchCategoryAdapter(var items: List<CategoryModel>): RecyclerView.Adapter<SearchCategoryAdapter.ItemHolder>() {
    inner class ItemHolder(val binding: AllCategoryItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        return ItemHolder(
            AllCategoryItemLayoutBinding.inflate(
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
//        Glide.with(holder.binding.caytegoryImg)
//            .load(item.image)
//            .into(holder.binding.caytegoryImg)
        holder.binding.categoryTxt.text = item.language
        holder.itemView.setOnClickListener {
            val intent = Intent(it.context, DarslarActivity::class.java)
            intent.putExtra("Til", item.language)
            it.context.startActivity(intent)
        }
    }

    fun filter(filter: List<CategoryModel>) {
        items = filter
        notifyDataSetChanged()
    }
}