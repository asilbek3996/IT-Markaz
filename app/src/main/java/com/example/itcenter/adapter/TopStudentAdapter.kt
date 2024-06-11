package com.example.itcenter.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.itcenter.activity.AboutStudentActivity
import com.example.itcenter.activity.AllStudentActivity
import com.example.itcenter.activity.DarslarActivity
import com.example.itcenter.databinding.ActivityAllStudentBinding
import com.example.itcenter.databinding.AlStudentItemLayoutBinding
import com.example.itcenter.databinding.DarslarBinding
import com.example.itcenter.databinding.TopStudentItemLayouBinding
import com.example.itcenter.model.AllStudentModel
import com.example.itcenter.model.DarslarModel
import com.example.itcenter.model.GroupModel

class TopStudentAdapter(var items: List<GroupModel>): RecyclerView.Adapter<TopStudentAdapter.ItemHolder>() {
    inner class ItemHolder(val binding: TopStudentItemLayouBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        return ItemHolder(TopStudentItemLayouBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
        return items.count()
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        val item = items[position]
        val intent2 = Intent(holder.itemView.context, AllStudentActivity::class.java)

        holder.binding.groupName.text = item.name
        holder.binding.tvAll.setOnClickListener {
                intent2.putExtra("Group", item.group)
                intent2.putExtra("GroupName", item.name)
                it.context.startActivity(intent2)
        }

//        if (item.students.isEmpty()){
//            holder.binding.students.visibility = View.GONE
//            holder.binding.notStudents.visibility = View.VISIBLE
//        }else {
            val intent = Intent(holder.itemView.context, AboutStudentActivity::class.java)
            val top = item.students.sortedByDescending { it.userPercentage }
            if (item.students.size in 1..1) {
                top.take(1)
                holder.binding.tvAll.visibility = View.VISIBLE
                holder.binding.item3.visibility = View.INVISIBLE
                holder.binding.item2.visibility = View.INVISIBLE
                holder.binding.item1.setOnClickListener {
                    intent.putExtra("Student", top[0].fullName)
                    it.context.startActivity(intent)
                }
                Glide.with(holder.binding.img1).load(top[0].userPhoto).into(holder.binding.img1)
                holder.binding.name1.text = top[0].fullName
                holder.binding.progress1.progress = top[0].userPercentage!!
                var topAndroidtxt = top[0].userPercentage.toString()
                holder.binding.foiz1.text = topAndroidtxt
            } else if (item.students.size in 1..2) {
                top.take(2)
                holder.binding.tvAll.visibility = View.VISIBLE
                holder.binding.item3.visibility = View.INVISIBLE
                holder.binding.item1.setOnClickListener {
                    intent.putExtra("Student", top[0].fullName)
                    it.context.startActivity(intent)
                }
                holder.binding.item2.setOnClickListener {
                    intent.putExtra("Student", top[1].fullName)
                    it.context.startActivity(intent)
                }
                Glide.with(holder.binding.img1).load(top[0].userPhoto).into(holder.binding.img1)
                Glide.with(holder.binding.img2).load(top[1].userPhoto).into(holder.binding.img2)
                holder.binding.name1.text = top[0].fullName
                holder.binding.name2.text = top[1].fullName
                holder.binding.progress1.progress = top[0].userPercentage!!
                holder.binding.progress2.progress = top[1].userPercentage!!
                var top1txt = top[0].userPercentage.toString()
                var top2txt = top[1].userPercentage.toString()
                holder.binding.foiz1.text = top1txt
                holder.binding.foiz2.text = top2txt
            } else if (item.students.size > 2) {
                top.take(3)
                holder.binding.tvAll.visibility = View.VISIBLE
                holder.binding.item1.setOnClickListener {
                    intent.putExtra("Student", top[0].fullName)
                    it.context.startActivity(intent)
                }
                holder.binding.item2.setOnClickListener {
                    intent.putExtra("Student", top[1].fullName)
                    it.context.startActivity(intent)
                }
                holder.binding.item3.setOnClickListener {
                    intent.putExtra("Student", top[2].fullName)
                    it.context.startActivity(intent)
                }
                Glide.with(holder.binding.img1).load(top[0].userPhoto).into(holder.binding.img1)
                Glide.with(holder.binding.img2).load(top[1].userPhoto).into(holder.binding.img2)
                Glide.with(holder.binding.img3).load(top[2].userPhoto).into(holder.binding.img3)
                holder.binding.name1.text = top[0].fullName
                holder.binding.name2.text = top[1].fullName
                holder.binding.name3.text = top[2].fullName
                holder.binding.progress1.progress = top[0].userPercentage!!
                holder.binding.progress2.progress = top[1].userPercentage!!
                holder.binding.progress3.progress = top[2].userPercentage!!
                var top1txt = top[0].userPercentage.toString()
                var top2txt = top[1].userPercentage.toString()
                var top3txt = top[2].userPercentage.toString()
                holder.binding.foiz1.text = top1txt
                holder.binding.foiz2.text = top2txt
                holder.binding.foiz3.text = top3txt
            } else {
                holder.binding.tvAll.visibility = View.GONE
                holder.binding.students.visibility = View.GONE
                holder.binding.notStudents.visibility = View.VISIBLE
            }
//        }
    }
}