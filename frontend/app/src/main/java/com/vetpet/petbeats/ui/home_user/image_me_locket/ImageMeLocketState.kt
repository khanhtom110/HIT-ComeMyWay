package com.vetpet.petbeats.ui.home_user.image_me_locket

import com.vetpet.petbeats.ui.home_user.image_me_locket.adapter.ImageLocketChild

data class ImageMeLocketState (
    val lastPostId: Int = 0,
    val listMeLocket: List<ImageLocketChild> = emptyList(),
)