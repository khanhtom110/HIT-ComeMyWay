package com.example.petbeats.ui.home.book

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.petbeats.data.repository.HomeRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class BookViewModel(
    private val repository: HomeRepository
): ViewModel() {
    private val _state = MutableStateFlow(BookState())
    val state = _state.asStateFlow()

    private val _event = MutableSharedFlow<BookEvent>()
    val event = _event.asSharedFlow()
    
    fun searchClick() {
        viewModelScope.launch {
            _event.emit(BookEvent.NavigationSearch)
        }
    }

}