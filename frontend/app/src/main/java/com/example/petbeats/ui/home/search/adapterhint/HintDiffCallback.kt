package com.example.petbeats.ui.home.search.adapterhint

import androidx.recyclerview.widget.DiffUtil

class HintDiffCallback: DiffUtil.ItemCallback<HintChild>() {
    override fun areItemsTheSame(old: HintChild, new: HintChild): Boolean {
        return old.id == new.id
    }

    override fun areContentsTheSame(old: HintChild, new: HintChild): Boolean {
        return old == new
    }

}