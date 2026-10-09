package com.vetpet.petbeats.ui.home_admin.list_appointment_admin.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.VetPet.R

class AppointmentAdminCurrentAdapter(
    private val onSettingClick: (Int, View) -> (Unit),
    private val onClinicDetailClick: (Int) -> (Unit)
): ListAdapter<AppointmentAdminChild, AppointmentAdminCurrentAdapter.ViewHolder>(AppointmentAdminDiffCallback()) {
    override fun onCreateViewHolder(holder: ViewGroup, position: Int): ViewHolder {
        val view = LayoutInflater.from(holder.context).inflate(R.layout.item_account_clinic, holder, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentBook = getItem(position)

        holder.bind(currentBook, onSettingClick, onClinicDetailClick)
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val tvNameAccount: TextView = itemView.findViewById(R.id.tvNameAccount)
        val btnSetting: ImageView = itemView.findViewById(R.id.btnSetting)


        fun bind(item: AppointmentAdminChild, onSettingClick: (Int, View) -> Unit, onClinicDetailClick: (Int) -> Unit) {
            tvNameAccount.text = item.nameAccount

            btnSetting.setOnClickListener { view ->
                onSettingClick(item.id, view)
            }

            tvNameAccount.setOnClickListener {
                onClinicDetailClick(item.clinicId)
            }
        }
    }
}