package com.example.petbeats.ui.home_user.book.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.VetPet.R

class BookAdapter(
    private val onItemClick: (Int, Int) -> Unit
): ListAdapter<BookChild, BookAdapter.ViewHolder>(BookDiffCallback()) {
    override fun onCreateViewHolder(holder: ViewGroup, position: Int): ViewHolder {
        val view = LayoutInflater.from(holder.context).inflate(R.layout.item_book_child, holder, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentBook = getItem(position)

        holder.bind(currentBook, onItemClick)
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val nameRoom: TextView = itemView.findViewById(R.id.nameRoom)
        val image: ImageView = itemView.findViewById(R.id.image)
        val address: TextView = itemView.findViewById(R.id.address)
        val date: TextView = itemView.findViewById(R.id.date)
        val time: TextView = itemView.findViewById(R.id.time)
        val status: TextView = itemView.findViewById(R.id.status)

        fun bind(item: BookChild, onItemClick: (Int, Int) -> Unit) {
            nameRoom.text = item.nameClinic
            address.text = item.address
            date.text = "${item.calendar} -"
            time.text = item.time.take(5)


            Glide.with(itemView.context)
                .load(item.thumbnailUrl)
                .circleCrop()
                .into(image)

            when(item.status) {
                BookChildState.PENDING -> {
                    status.text = "Chờ xử lý"
                    status.setTextColor(Color.parseColor("#F7C120"))
                }
                BookChildState.SUCCESS -> {
                    status.text = "Đặt lịch thành công"
                    status.setTextColor(Color.parseColor("#00FF0B"))
                }
                BookChildState.CANCELLED -> {
                    status.text = "Từ chối"
                    status.setTextColor(Color.parseColor("#CC0900"))
                }
            }

            nameRoom.setOnClickListener {
                onItemClick(item.id, item.clinicId)
            }
        }
    }
}