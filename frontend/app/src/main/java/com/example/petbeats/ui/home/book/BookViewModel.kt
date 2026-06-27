package com.example.petbeats.ui.home.book

import android.graphics.pdf.models.ListItem
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.petbeats.R
import com.example.petbeats.ui.home.book.adapter.BookChild
import com.example.petbeats.ui.home.book.adapter.BookChildState
import com.example.petbeats.ui.home.book.BookState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class BookViewModel: ViewModel() {
    private val fakeList = mutableListOf(
        BookChild(
            id = "1",
            roomName = "Phòng khám thú y Hà Nội Funpet",
            image = R.drawable.image_test,
            action = "Đang hoạt động",
            distance = 0.2,
            rating = 4.8f,
            address = "83 Giải Phóng, P. Đồng Tâm",
            time = "Thứ 5, 25/06/06 - 9:00 AM",
            status = BookChildState.PENDING,
        ),
        BookChild(
            id = "2",
            roomName = "Phòng khám thú y Hà Nội Funpet",
            image = R.drawable.image_test,
            action = "Đang hoạt động",
            distance = 3.0,
            rating = 4.8f,
            address = "83 Giải Phóng, P. Đồng Tâm",
            time = "Thứ 5, 25/06/06 - 9:00 AM",
            status = BookChildState.SUCCESS
        ),
        BookChild(
            id = "2",
            roomName = "Phòng khám thú y Hà Nội Funpet",
            image = R.drawable.image_test,
            action = "Đang hoạt động",
            distance = 3.0,
            rating = 2f,
            address = "83 Giải Phóng, P. Đồng Tâm",
            time = "Thứ 5, 25/06/06 - 9:00 AM",
            status = BookChildState.REFUSE
        )

    )

    private val _state = MutableStateFlow(BookState(list = fakeList))
    val state = _state.asStateFlow()

    private val _event = MutableSharedFlow<BookEvent>()
    val event = _event.asSharedFlow()
    
    fun searchClick() {
        viewModelScope.launch {
            _event.emit(BookEvent.NavigationSearch)
        }
    }
}