package com.example.petbeats.ui.home.book.adapter

import androidx.recyclerview.widget.DiffUtil

class BookDiffCallback: DiffUtil.ItemCallback<BookChild>() {
    override fun areItemsTheSame(old: BookChild, new: BookChild): Boolean {
        return old.nameClinic == new.nameClinic
    }

    override fun areContentsTheSame(old: BookChild, new: BookChild): Boolean {
        return old == new
    }

}