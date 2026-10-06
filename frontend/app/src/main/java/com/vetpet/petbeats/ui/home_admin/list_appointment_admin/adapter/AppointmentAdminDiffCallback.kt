package com.vetpet.petbeats.ui.home_admin.list_appointment_admin.adapter

import androidx.recyclerview.widget.DiffUtil

class AppointmentAdminDiffCallback: DiffUtil.ItemCallback<AppointmentAdminChild>() {
    override fun areItemsTheSame(old: AppointmentAdminChild, new: AppointmentAdminChild): Boolean {
        return old.id == new.id
    }

    override fun areContentsTheSame(old: AppointmentAdminChild, new: AppointmentAdminChild): Boolean {
        return old == new
    }

}