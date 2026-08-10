package com.vetpet.petbeats.ui.home_clinic.appointmentschedule.adapter

import androidx.recyclerview.widget.DiffUtil

class AppointmentDiffCallback: DiffUtil.ItemCallback<AppointmentChild>() {
    override fun areItemsTheSame(old: AppointmentChild, new: AppointmentChild): Boolean {
        return old.id == new.id
    }

    override fun areContentsTheSame(old: AppointmentChild, new: AppointmentChild): Boolean {
        return old == new
    }

}