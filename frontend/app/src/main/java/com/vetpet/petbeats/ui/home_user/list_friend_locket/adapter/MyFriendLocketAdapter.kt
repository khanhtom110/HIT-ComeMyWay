package com.vetpet.petbeats.ui.home_user.list_friend_locket.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.VetPet.R

class MyFriendLocketAdapter(
    private val onItemClick: (Int) -> Unit
): ListAdapter<FriendChild, MyFriendLocketAdapter.ViewHolder>(MyFriendDiffCallback()) {
    override fun onCreateViewHolder(holder: ViewGroup, position: Int): ViewHolder {
        val view = LayoutInflater.from(holder.context).inflate(R.layout.item_myfriend_locket, holder, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = getItem(position)

        holder.bind(currentItem, onItemClick)

    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.tvName)
        val image: ImageView = itemView.findViewById(R.id.imgName)
        val btnCancel: ImageView = itemView.findViewById(R.id.btnCancel)

        fun bind(item: FriendChild, onItemClick: (Int) -> Unit) {
            name.text = item.username

            Glide.with(itemView.context)
                .load(item.avatar)
                .circleCrop()
                .into(image)

            btnCancel.setOnClickListener {
                onItemClick(item.friendId)
            }
        }
    }


}