package com.vetpet.petbeats.ui.home_user.search.adapterhistory

import androidx.recyclerview.widget.DiffUtil

class HistoryDiffCallback: DiffUtil.ItemCallback<HistoryChild>() {
    override fun areItemsTheSame(old: HistoryChild, new: HistoryChild): Boolean {
        return old.nameSearch == new.nameSearch
    }

    override fun areContentsTheSame(old: HistoryChild, new: HistoryChild): Boolean {
        return old == new
    }

}