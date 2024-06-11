package com.example.itcenter.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.itcenter.R
import com.example.itcenter.activity.AboutStudentActivity
import com.example.itcenter.activity.VideoActivity
import com.example.itcenter.databinding.DarslarBinding
import com.example.itcenter.model.AllStudentModel
import com.example.itcenter.model.DarslarModel
import com.example.itcenter.utils.PrefUtils

class DarsAdapter(var items: ArrayList<DarslarModel>): RecyclerView.Adapter<DarsAdapter.ItemHolder>() {
    inner class ItemHolder(val binding: DarslarBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        return ItemHolder(DarslarBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
        return items.count()
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        val item = items[position]
        var pref = PrefUtils(holder.itemView.context)
        var lesson = pref.getLesson()
        if (position<lesson){
            holder.binding.time.setImageResource(R.drawable.check)
            holder.itemView.setOnClickListener {
                val intent = Intent(it.context, VideoActivity::class.java)
                intent.putExtra("video", item.id)
                it.context.startActivity(intent)
            }
        }else if (position==lesson){
            holder.binding.time.setImageResource(R.drawable.play)
            holder.itemView.setOnClickListener {
                val intent = Intent(it.context, VideoActivity::class.java)
                intent.putExtra("video", item.id)
                it.context.startActivity(intent)
            }
        }else {
            holder.binding.time.setImageResource(R.drawable.clock)
            holder.itemView.setOnClickListener {
                val intent = Intent(it.context, VideoActivity::class.java)
                intent.putExtra("video", item.id)
                it.context.startActivity(intent)
            }
        }
        var txt = position+1
        holder.binding.tvName.text = "${txt}-dars. ${item.lessonName}"
    }
    fun filter(filter: ArrayList<DarslarModel>) {
        items = filter
        notifyDataSetChanged()
    }
}