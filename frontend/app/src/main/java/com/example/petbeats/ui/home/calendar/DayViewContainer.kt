package com.example.petbeats.ui.home.calendar

import android.view.View
import com.example.petbeats.databinding.CalendarDayLayoutBinding
import com.kizitonwose.calendar.view.ViewContainer

class DayViewContainer(view: View): ViewContainer(view) {
    val textView = CalendarDayLayoutBinding.bind(view).calendarDayText
}