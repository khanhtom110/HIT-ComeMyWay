package com.vetpet.petbeats.ui.home_user.historylistall

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vetpet.petbeats.data.local.dao.HistoryDao
import com.vetpet.petbeats.data.local.entity.HistoryEntity
import com.vetpet.petbeats.data.remote.sharepreference.TokenManager
import com.vetpet.petbeats.data.repository.HomeUserRepository
import com.vetpet.petbeats.ui.home_user.search.adapterhistory.HistoryChild
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HistoryListAllViewModel(
    private val repository: HomeUserRepository,
    private val historyDao: HistoryDao,
    private val tokenManager: TokenManager
): ViewModel() {
    private var _state = MutableStateFlow(HistoryListAllState())
    val state = _state.asStateFlow()

    private var _event = MutableSharedFlow<HistoryListAllEvent>()
    val event = _event.asSharedFlow()

    //Gán database vào list của mình để hiển thị lên màn hình
    init {
        viewModelScope.launch {
            val currentUserId = tokenManager.getUserId()

            historyDao.listHistory(currentUserId).collect { historyDao ->
                val showList = historyDao.map { listDao ->
                    HistoryChild(nameSearch = listDao.keyword)
                }
                _state.value = _state.value.copy(listHistory = showList)
            }
        }
    }

    //Lưu lịch sử vào database
    fun insertHistory() {
        val search = _state.value.search

        if (search.trim().isEmpty()) {
            return
        }
        else {
            viewModelScope.launch {
                val currentUserId = tokenManager.getUserId()

                val newHistory = HistoryEntity(
                    keyword = search.trim(),
                    userId = currentUserId
                )
                historyDao.insertHistory(newHistory)

                _state.value = _state.value.copy(search = "")
                _event.emit(HistoryListAllEvent.NavigationResultSearch(search))
            }
        }
    }

    fun searchClick() {
        viewModelScope.launch {
            _event.emit(HistoryListAllEvent.NavigationSearch)
        }
    }

    fun onSearchChange(search: String) {
        viewModelScope.launch {
            _state.value = _state.value.copy(search = search)
        }
    }

    fun onCheck(isSearch: Boolean) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isSearch = isSearch)
        }
    }

    //Dùng để lấy search của history sang cho màn resultSearch
    fun itemClickHistory(nameSearch: String) {
        viewModelScope.launch {
            _event.emit(HistoryListAllEvent.NavigationResultSearch(nameSearch))
        }
    }
}