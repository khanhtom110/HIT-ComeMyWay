package com.vetpet.petbeats.ui.home_clinic.appointmentschedule.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.VetPet.R

class AppointmentWaitAdapter(
    private val onDetailClick: (Int) -> (Unit),
    private val onRefuseClick: (Int) -> (Unit),
    private val onReceiveClick: (Int) -> (Unit)
): ListAdapter<AppointmentChild, AppointmentWaitAdapter.ViewHolder>(AppointmentDiffCallback()) {
    override fun onCreateViewHolder(holder: ViewGroup, position: Int): ViewHolder {
        val view = LayoutInflater.from(holder.context).inflate(R.layout.item_wait_appointment, holder, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentBook = getItem(position)

        holder.bind(currentBook, onDetailClick, onRefuseClick, onReceiveClick)
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val imgPet: ImageView = itemView.findViewById(R.id.imgPet)
        val tvPet: TextView = itemView.findViewById(R.id.tvPet)
        val tvNameUser: TextView = itemView.findViewById(R.id.tvNameUser)
        val tvNameClinic: TextView = itemView.findViewById(R.id.tvNameClinic)
        val date: TextView = itemView.findViewById(R.id.date)
        val time: TextView = itemView.findViewById(R.id.time)

        val detail: TextView = itemView.findViewById(R.id.btnDetail)
        val refuse: TextView = itemView.findViewById(R.id.btnRefuse)
        val receive: TextView = itemView.findViewById(R.id.btnReceive)


        fun bind(item: AppointmentChild, onDetailClick: (Int) -> Unit, onRefuseClick: (Int) -> Unit, onReceiveClick: (Int) -> Unit) {
            tvPet.text = item.petName
            tvNameUser.text = "Chủ sở hữu: ${item.user}"
            tvNameClinic.text = item.clinic
            date.text = "${item.date} -"
            time.text = item.time.take(5)

            Glide.with(itemView.context)
                .load(item.image)
                .circleCrop()
                .into(imgPet)

            detail.setOnClickListener {
                onDetailClick(item.id)
            }
            refuse.setOnClickListener {
                onRefuseClick(item.id)
            }
            receive.setOnClickListener {
                onReceiveClick(item.id)
            }
        }
    }

}