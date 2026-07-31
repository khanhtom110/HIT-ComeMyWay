package com.vetpet.petbeats.ui.home_user.calendar.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.VetPet.R

class TimePagerAdapter(
    private val itemClick: List<String>
): RecyclerView.Adapter<TimePagerAdapter.ViewHolder>() {
    override fun onCreateViewHolder(holder: ViewGroup, position: Int): TimePagerAdapter.ViewHolder {
        val view = LayoutInflater.from(holder.context).inflate(R.layout.item_time_number, holder, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: TimePagerAdapter.ViewHolder, position: Int) {
        holder.tvNumber.text = itemClick[position]
    }

    override fun getItemCount(): Int = itemClick.size

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val tvNumber: TextView = view.findViewById(R.id.tvNumber)
    }

}