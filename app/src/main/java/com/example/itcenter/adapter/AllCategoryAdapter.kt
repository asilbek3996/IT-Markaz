package com.example.itcenter.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.itcenter.activity.LevelActivity
import com.example.itcenter.databinding.AllCategoryItemLayoutBinding
import com.example.itcenter.model.CategoryModel

class AllCategoryAdapter(var items: List<CategoryModel>): RecyclerView.Adapter<AllCategoryAdapter.ItemHolder>() {
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
        Glide.with(holder.binding.caytegoryImg)
            .load(item.image)
            .into(holder.binding.caytegoryImg)
        holder.binding.categoryTxt.text = item.language
        holder.itemView.setOnClickListener {
            val intent = Intent(it.context, LevelActivity::class.java)
            intent.putExtra("Til", item.language)
            it.context.startActivity(intent)
        }
    }
}