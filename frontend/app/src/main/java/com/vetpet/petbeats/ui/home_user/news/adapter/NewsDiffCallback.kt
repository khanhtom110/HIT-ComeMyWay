package com.vetpet.petbeats.ui.home_user.news.adapter

import androidx.recyclerview.widget.DiffUtil

class NewsDiffCallback: DiffUtil.ItemCallback<NewsChild>() {
    override fun areItemsTheSame(old: NewsChild, new: NewsChild): Boolean {
        return old.id == new.id
    }

    override fun areContentsTheSame(old: NewsChild, new: NewsChild): Boolean {
        return old == new
    }

}