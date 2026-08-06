package com.vetpet.petbeats.ui.home_user.list_friend_locket.adapter

import androidx.recyclerview.widget.DiffUtil

class AddFriendDiffCallback: DiffUtil.ItemCallback<FriendChild>() {
    override fun areItemsTheSame(old: FriendChild, new: FriendChild): Boolean {
        return old == new
    }

    override fun areContentsTheSame(old: FriendChild, new: FriendChild): Boolean {
        return old == new
    }
}