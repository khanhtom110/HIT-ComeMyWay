package com.vetpet.petbeats.ui.home_user.chatbot.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.VetPet.R
import com.vetpet.petbeats.data.remote.model.calendar.home_user.response.chatbotresponse.RecommendClinic

class ListClinicAdapter(
    private val onItemClick: (Int) -> Unit
): ListAdapter<RecommendClinic, ListClinicAdapter.ViewHolder>(ListClinicDiffcallback()) {
    override fun onCreateViewHolder(holder: ViewGroup, position: Int): ViewHolder {
        val view = LayoutInflater.from(holder.context).inflate(R.layout.item_hint_chatbot, holder, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val current = getItem(position)

        holder.bind(current, onItemClick)
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val nameClinic: TextView = itemView.findViewById(R.id.nameRoom)
        val address: TextView = itemView.findViewById(R.id.address)
        val image: ImageView = itemView.findViewById(R.id.image)

        fun bind(item: RecommendClinic, onItemClick: (Int) -> Unit) {
            nameClinic.text = item.name
            address.text = item.address

            Glide.with(itemView.context)
                .load(item.thumbnailUrl)
                .circleCrop()
                .into(image)

            nameClinic.setOnClickListener {
                onItemClick(item.id)
            }
        }
    }
}