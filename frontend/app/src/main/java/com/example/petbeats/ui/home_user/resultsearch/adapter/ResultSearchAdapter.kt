package com.example.petbeats.ui.home_user.resultsearch.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.VetPet.R

class ResultSearchAdapter(
    private val onItemClick: (Int) -> Unit
): ListAdapter<ResultSearchChild, ResultSearchAdapter.ViewHolder>(ResultSearchDiffCallBack()) {
    override fun onCreateViewHolder(holder: ViewGroup, position: Int): ResultSearchAdapter.ViewHolder {
        val view = LayoutInflater.from(holder.context).inflate(R.layout.result_search_child, holder, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ResultSearchAdapter.ViewHolder, position: Int) {
        val currentResultSearch = getItem(position)

        holder.bind(currentResultSearch, onItemClick)
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val nameRoom: TextView = itemView.findViewById(R.id.nameRoom)
        val image: ImageView = itemView.findViewById(R.id.image)
        val action: TextView = itemView.findViewById(R.id.action)
        val distance: TextView = itemView.findViewById(R.id.distance)
        val rating: TextView = itemView.findViewById(R.id.rating)
        val address: TextView = itemView.findViewById(R.id.address)
        val closeTime: TextView = itemView.findViewById(R.id.closeTime)
        val openTime: TextView = itemView.findViewById(R.id.opentTime)
        val detail: TextView = itemView.findViewById(R.id.btnDetail)

        fun bind(item: ResultSearchChild, onItemClick: (Int) -> Unit) {
            nameRoom.text = item.roomName
            distance.text = "${item.distance} km"
            rating.text = "Đánh giá: ${item.rating}/5"
            address.text = item.address
            openTime.text = "${item.openTime.take(5)} -"
            closeTime.text = "${item.closeTime.take(5)}"


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

            detail.setOnClickListener {
                onItemClick(item.id)
            }

            if (item.distance == 0.0) {
                distance.visibility = View.GONE
            }
            else {
                distance.visibility = View.VISIBLE
            }
        }
    }
}