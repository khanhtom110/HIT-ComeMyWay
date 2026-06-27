package com.example.petbeats.ui.home.search.adapterhistory

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.petbeats.R

class AdapterHistory: ListAdapter<HistoryChild, AdapterHistory.ViewHolder>(HistoryDiffCallback()) {
    override fun onCreateViewHolder(holder: ViewGroup, position: Int): ViewHolder {
        val view = LayoutInflater.from(holder.context).inflate(R.layout.history_child, holder, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentHistory = getItem(position)

        holder.bind(currentHistory)
    }


    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.nameRoom)

        fun bind(item: HistoryChild) {
            name.text = item.nameSearch
        }
    }
}