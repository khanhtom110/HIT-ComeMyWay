package com.vetpet.petbeats.ui.home_clinic.news.adapter

import androidx.recyclerview.widget.DiffUtil

class NewsClinicDiffCallback: DiffUtil.ItemCallback<NewsChildClinic>() {
    override fun areItemsTheSame(old: NewsChildClinic, new: NewsChildClinic): Boolean {
        return old.id == new.id
    }

    override fun areContentsTheSame(old: NewsChildClinic, new: NewsChildClinic): Boolean {
        return old == new
    }

}