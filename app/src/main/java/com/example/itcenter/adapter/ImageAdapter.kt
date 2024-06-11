package com.example.itcenter.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.itcenter.R
import com.example.itcenter.model.ImageItem


class ImageAdapter(val imageList: ArrayList<ImageItem>, private val viewPager2: ViewPager2) :
    RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {

    class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageView);
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.view_pager_item, parent, false)
        return ImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        Glide.with(holder.imageView)
                .load(imageList[position].image)
                .into(holder.imageView)
        if (position == imageList.size-1){
            viewPager2.post(runnable)
        }
    }

    override fun getItemCount(): Int {
        return imageList.size
    }

    private val runnable = Runnable {
        imageList.addAll(imageList)
        notifyDataSetChanged()
    }
}
//class ImageAdapter : ListAdapter<ImageItem, ImageAdapter.ViewHolder>(DiffCallback()){
//
//    class DiffCallback : DiffUtil.ItemCallback<ImageItem>(){
//        override fun areItemsTheSame(oldItem: ImageItem, newItem: ImageItem): Boolean {
//            return oldItem.id == newItem.id
//        }
//
//        override fun areContentsTheSame(oldItem: ImageItem, newItem: ImageItem): Boolean {
//            return oldItem == newItem
//        }
//
//    }
//    class ViewHolder(iteView: View): RecyclerView.ViewHolder(iteView){
//        private val imageView = iteView.findViewById<ImageView>(R.id.imageView)
//
//        fun bindData(item: ImageItem){
//            Glide.with(itemView)
//                .load(item.image)
//                .into(imageView)
//        }
//
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        return ViewHolder(
//            LayoutInflater.from(parent.context)
//                .inflate(R.layout.view_pager_item,parent,false)
//        )
//    }
//
//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        val imageItem = getItem(position)
//        holder.bindData(imageItem)
//    }
//}