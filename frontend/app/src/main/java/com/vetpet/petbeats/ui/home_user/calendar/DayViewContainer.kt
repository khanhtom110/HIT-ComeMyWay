package com.vetpet.petbeats.ui.home_user.calendar

import android.view.View
import com.example.VetPet.databinding.LayoutCalendarDayBinding
import com.kizitonwose.calendar.view.ViewContainer

class DayViewContainer(view: View): ViewContainer(view) {
    val textView = LayoutCalendarDayBinding.bind(view).calendarDayText
}