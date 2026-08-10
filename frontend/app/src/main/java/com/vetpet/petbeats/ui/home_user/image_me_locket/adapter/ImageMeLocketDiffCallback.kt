package com.vetpet.petbeats.ui.home_user.image_me_locket.adapter

import androidx.recyclerview.widget.DiffUtil

class ImageMeLocketDiffCallback: DiffUtil.ItemCallback<ImageLocketChild>() {
    override fun areItemsTheSame(old: ImageLocketChild, new: ImageLocketChild): Boolean {
        return old.lastPostId == new.lastPostId
    }

    override fun areContentsTheSame(old: ImageLocketChild, new: ImageLocketChild): Boolean {
        return old == new
    }
}