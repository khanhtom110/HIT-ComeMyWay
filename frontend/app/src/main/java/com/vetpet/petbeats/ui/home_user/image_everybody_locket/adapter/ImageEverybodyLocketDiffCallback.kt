package com.vetpet.petbeats.ui.home_user.image_everybody_locket.adapter

import androidx.recyclerview.widget.DiffUtil
import com.vetpet.petbeats.ui.home_user.image_me_locket.adapter.ImageLocketChild

class ImageEverybodyLocketDiffCallback: DiffUtil.ItemCallback<ImageLocketChild>() {
    override fun areItemsTheSame(old: ImageLocketChild, new: ImageLocketChild): Boolean {
        return old.lastPostId == new.lastPostId
    }

    override fun areContentsTheSame(old: ImageLocketChild, new: ImageLocketChild): Boolean {
        return old == new
    }
}