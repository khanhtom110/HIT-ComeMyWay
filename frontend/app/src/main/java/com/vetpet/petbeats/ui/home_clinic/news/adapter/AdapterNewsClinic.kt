package com.vetpet.petbeats.ui.home_clinic.news.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.VetPet.R
import com.vetpet.petbeats.ui.home_user.news.adapter.NewsChild
import com.vetpet.petbeats.ui.home_user.news.adapter.NewsDiffCallback

class AdapterNewsClinic: ListAdapter<NewsChildClinic, AdapterNewsClinic.ViewHolder>(NewsClinicDiffCallback()) {
    override fun onCreateViewHolder(holder: ViewGroup, position: Int): ViewHolder {
        val view = LayoutInflater.from(holder.context).inflate(R.layout.item_new_user, holder, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentNews = getItem(position)

        holder.bind(currentNews)
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val imgNews: ImageView = itemView.findViewById(R.id.imgNews)
        val imgClinic: ImageView = itemView.findViewById(R.id.imgClinic)
        val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
        val tvContent: TextView = itemView.findViewById(R.id.tvContent)

        fun bind(item: NewsChildClinic) {
            tvTitle.text = item.title
            tvContent.text = item.content


            Glide.with(itemView.context)
                .load(item.imageUrls)
                .into(imgNews)

            Glide.with(itemView.context)
                .load(item.imageClinic)
                .circleCrop()
                .into(imgClinic)

        }
    }
}