package com.example.petbeats.ui.home.search.adapterhint

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.VetPet.R

class AdapterHint(
    private val onItemClick: (Int) -> Unit
): ListAdapter<HintChild, AdapterHint.ViewHolder>(HintDiffCallback()) {
    override fun onCreateViewHolder(holder: ViewGroup, position: Int): ViewHolder {
        val view = LayoutInflater.from(holder.context).inflate(R.layout.item_hint_child, holder, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentHint = getItem(position)

        holder.bind(currentHint, onItemClick)
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.nameRoom)
        val thumbnailUrl: ImageView = itemView.findViewById(R.id.image)
        val address: TextView = itemView.findViewById(R.id.address)
        val distance: TextView = itemView.findViewById(R.id.distance)
        val rating: TextView = itemView.findViewById(R.id.rating)
        val iconrating: ImageView = itemView.findViewById(R.id.iconRating)


        fun bind(item: HintChild, onItemClick: (Int) -> Unit) {
            name.text = item.name
            address.text = item.address
            distance.text = "${item.distance} km"
            rating.text = "${item.rating}/5"

            Glide.with(itemView.context)
                .load(item.thumbnailUrl)
                .circleCrop()
                .into(thumbnailUrl)


            name.setOnClickListener {
                onItemClick(item.id)
            }

            //ẩn hiện rating và distance
            if (item.distance == 0.0) {
                distance.visibility = View.GONE
                rating.visibility = View.VISIBLE
                iconrating.visibility = View.VISIBLE
            }
            else {
                distance.visibility = View.VISIBLE
                rating.visibility = View.GONE
                iconrating.visibility = View.GONE
            }
        }
    }
}