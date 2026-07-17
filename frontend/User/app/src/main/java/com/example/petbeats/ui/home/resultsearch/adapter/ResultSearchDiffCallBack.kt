package com.example.petbeats.ui.home.resultsearch.adapter

import androidx.recyclerview.widget.DiffUtil

class ResultSearchDiffCallBack: DiffUtil.ItemCallback<ResultSearchChild>() {
    override fun areItemsTheSame(old: ResultSearchChild, new: ResultSearchChild): Boolean {
        return old.id == new.id
    }

    override fun areContentsTheSame(old: ResultSearchChild, new: ResultSearchChild): Boolean {
        return old == new
    }

}