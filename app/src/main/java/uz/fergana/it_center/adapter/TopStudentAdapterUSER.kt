package uz.fergana.it_center.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import uz.fergana.it_center.activity.AboutStudentActivity
import uz.fergana.it_center.activity.AllStudentActivity
import uz.fergana.it_center.databinding.TopStudentItemLayouBinding
import uz.fergana.it_center.databinding.TopStudentItemLayoutUserBinding
import uz.fergana.it_center.model.AllStudentModel
import uz.fergana.it_center.model.GroupModel

class TopStudentAdapterUSER(var items: List<AllStudentModel>): RecyclerView.Adapter<TopStudentAdapterUSER.ItemHolder>() {
    inner class ItemHolder(val binding: TopStudentItemLayoutUserBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        return ItemHolder(TopStudentItemLayoutUserBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
        return items.count()
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        val item = items[position]
            val intent = Intent(holder.itemView.context, AboutStudentActivity::class.java)
            if (items.isNullOrEmpty()) {
                holder.binding.students.visibility = View.GONE
                holder.binding.notStudents.visibility = View.VISIBLE
            } else {
                holder.itemView.setOnClickListener {
                    intent.putExtra("Student", item.fullName)
                    it.context.startActivity(intent)
                }
                Glide.with(holder.binding.img1).load(item.userPhoto).into(holder.binding.img1)
                holder.binding.name1.text = item.fullName
                holder.binding.progress1.progress = item.userPercentage!!
                var topAndroidtxt = item.userPercentage.toString()
                holder.binding.foiz1.text = topAndroidtxt
        }
    }
}