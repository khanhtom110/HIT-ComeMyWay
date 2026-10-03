package com.vetpet.petbeats.ui.home_clinic.news.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.VetPet.R
import com.vetpet.petbeats.ui.home_user.news.adapter.NewsChild
import com.vetpet.petbeats.ui.home_user.news.adapter.NewsDiffCallback

class AdapterNewsClinic(
    private val onItemClick: (Int) -> Unit
): ListAdapter<NewsChild, AdapterNewsClinic.ViewHolder>(NewsDiffCallback()) {
    override fun onCreateViewHolder(holder: ViewGroup, position: Int): ViewHolder {
        val view = LayoutInflater.from(holder.context).inflate(R.layout.item_hint_child, holder, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentNews = getItem(position)

        holder.bind(currentNews, onItemClick)
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val imgNews: ImageView = itemView.findViewById(R.id.imgNews)
        val imgClinic: ImageView = itemView.findViewById(R.id.imgClinic)
        val tvClinicName: TextView = itemView.findViewById(R.id.tvClinicName)
        val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)

        fun bind(item: NewsChild, onItemClick: (Int) -> Unit) {
            tvClinicName.text = item.nameClinic
            tvTitle.text = item.titleClinic

            Glide.with(itemView.context)
                .load(item.thumbnailUrlNews)
                .circleCrop()
                .into(imgNews)

            Glide.with(itemView.context)
                .load(item.thumbnailUrlClinic)
                .circleCrop()
                .into(imgClinic)


            itemView.setOnClickListener {
                onItemClick(item.id)
            }
        }
    }
}