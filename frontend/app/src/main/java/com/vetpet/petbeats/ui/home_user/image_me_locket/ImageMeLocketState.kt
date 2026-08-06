package com.vetpet.petbeats.ui.home_user.image_me_locket

import com.vetpet.petbeats.ui.home_user.image_me_locket.adapter.ImageLocketChild

data class ImageMeLocketState (
    val listMeLocket: List<ImageLocketChild> = emptyList(),
    val lastPostId: Int? = null,
    val hasNext: Boolean = true,
    val isLoading: Boolean = false,

    val isDown: Boolean = false,
)