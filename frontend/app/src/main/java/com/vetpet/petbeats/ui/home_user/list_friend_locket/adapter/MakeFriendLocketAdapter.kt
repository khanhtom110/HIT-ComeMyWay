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

class MakeFriendLocketAdapter(
    private val onItemClick: (Int) -> Unit
): ListAdapter<FriendChild, MakeFriendLocketAdapter.ViewHolder>(MakeFriendDiffCallback()) {
    override fun onCreateViewHolder(holder: ViewGroup, position: Int): ViewHolder {
        val view = LayoutInflater.from(holder.context).inflate(R.layout.item_makefriend_locket, holder, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = getItem(position)

        holder.bind(currentItem, onItemClick)
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.tvName)
        val image: ImageView = itemView.findViewById(R.id.imgName)
        val btnMakeFriend: ImageView = itemView.findViewById(R.id.btnMakeFriend)

        fun bind(item: FriendChild, onItemClick: (Int) -> Unit) {
            name.text = item.username


            Glide.with(itemView.context)
                .load(item.avatar)
                .circleCrop()
                .into(image)


            if (item.isMakeFriend) {
                btnMakeFriend.setImageResource(R.drawable.button_accept_makefriend)
            }
            else {
                btnMakeFriend.setImageResource(R.drawable.button_makefriend)
            }


            btnMakeFriend.setOnClickListener {
                onItemClick(item.friendId)
            }
        }
    }

}