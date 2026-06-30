package com.example.petbeats.ui.home.search.adapterhint

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.petbeats.R

class AdapterHint: ListAdapter<HintChild, AdapterHint.ViewHolder>(HintDiffCallback()) {
    override fun onCreateViewHolder(holder: ViewGroup, position: Int): ViewHolder {
        val view = LayoutInflater.from(holder.context).inflate(R.layout.hint_child, holder, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentHint = getItem(position)

        holder.bind(currentHint)
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val roomName: TextView = itemView.findViewById(R.id.nameRoom)
        val image: ImageView = itemView.findViewById(R.id.image)
        val address: TextView = itemView.findViewById(R.id.address)

        fun bind(item: HintChild) {
            roomName.text = item.roomName
            address.text = item.address

            Glide.with(itemView.context)
                .load(item.image)
                .circleCrop()
                .into(image)
        }
    }
}