package com.vetpet.petbeats.ui.home_user.image_everybody_locket.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.VetPet.R
import com.vetpet.petbeats.ui.home_user.image_me_locket.adapter.ImageLocketChild

class ImageEverybodyLocketAdapter: ListAdapter<ImageLocketChild, ImageEverybodyLocketAdapter.ViewHolder>(ImageEverybodyLocketDiffCallback()) {
    override fun onCreateViewHolder(holder: ViewGroup, position: Int): ViewHolder {
        val view = LayoutInflater.from(holder.context).inflate(R.layout.item_image_everybody_locket, holder, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = getItem(position)

        holder.bind(currentItem)
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.imgLocket)
        val editFeel: TextView = itemView.findViewById(R.id.editFeel)
        val userName: TextView = itemView.findViewById(R.id.userName)

        fun bind(item: ImageLocketChild) {
            editFeel.text = item.caption
            userName.text = item.userName

            Glide.with(itemView)
                .load(item.imageUrl)
                .centerCrop()
                .into(image)
        }
    }
}