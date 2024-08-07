package uz.fergana.it_center.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import uz.fergana.it_center.activity.AboutStudentActivity
import uz.fergana.it_center.databinding.AlStudentItemLayoutBinding
import uz.fergana.it_center.model.AllStudentModel

class StudentAdapter(var items: List<AllStudentModel>): RecyclerView.Adapter<StudentAdapter.ItemHolder>() {
    inner class ItemHolder(val binding: AlStudentItemLayoutBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        return ItemHolder(AlStudentItemLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
        return items.count()
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        val item = items[position]
        Glide.with(holder.binding.img)
            .load(item.userPhoto)
            .into(holder.binding.img)
        holder.binding.name.text = item.fullName
        holder.binding.progress.progress = item.userPercentage!!
        var txt = item.userPercentage
        holder.binding.foiz.text = txt.toString()
        holder.itemView.setOnClickListener {
            val intent = Intent(it.context, AboutStudentActivity::class.java)
            intent.putExtra("Student", item.fullName)
            it.context.startActivity(intent)
        }
    }
    fun filter(filter: ArrayList<AllStudentModel>) {
        items = filter
        notifyDataSetChanged()
    }
}