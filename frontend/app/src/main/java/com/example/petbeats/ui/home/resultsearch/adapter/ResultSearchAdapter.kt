package com.example.petbeats.ui.home.resultsearch.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.petbeats.R
import com.example.petbeats.ui.home.book.adapter.BookAdapter.ViewHolder
import com.example.petbeats.ui.home.book.adapter.BookChild
import com.example.petbeats.ui.home.book.adapter.BookChildState

class ResultSearchAdapter: ListAdapter<ResultSearchChild, ResultSearchAdapter.ViewHolder>(ResultSearchDiffCallBack()) {
    override fun onCreateViewHolder(holder: ViewGroup, position: Int): ResultSearchAdapter.ViewHolder {
        val view = LayoutInflater.from(holder.context).inflate(R.layout.result_search_child, holder, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ResultSearchAdapter.ViewHolder, position: Int) {
        val currentResultSearch = getItem(position)

        holder.bind(currentResultSearch)
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val nameRoom: TextView = itemView.findViewById(R.id.nameRoom)
        val image: ImageView = itemView.findViewById(R.id.image)
        val action: TextView = itemView.findViewById(R.id.action)
        val distance: TextView = itemView.findViewById(R.id.distance)
        val rating: TextView = itemView.findViewById(R.id.rating)
        val address: TextView = itemView.findViewById(R.id.address)
        val closeTime: TextView = itemView.findViewById(R.id.closeTime)
        val opentTime: TextView = itemView.findViewById(R.id.opentTime)

        fun bind(item: ResultSearchChild) {
            nameRoom.text = item.roomName
            distance.text = "${item.distance} km"
            rating.text = "Đánh giá: ${item.rating}/5"
            address.text = item.address
            closeTime.text = "${item.closeTime} - "
            opentTime.text = item.openTime

            if (item.isOperating) {
                action.text = "Đang hoạt động"
            }
            else {
                action.text = "Không hoạt động"
            }

            Glide.with(itemView.context)
                .load(item.image)
                .circleCrop()
                .into(image)
        }
    }
}