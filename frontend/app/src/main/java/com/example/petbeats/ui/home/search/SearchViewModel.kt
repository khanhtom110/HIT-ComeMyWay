package com.example.petbeats.ui.home.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.petbeats.core.base.DataResult
import com.example.petbeats.data.local.dao.HistoryDao
import com.example.petbeats.data.local.entity.HistoryEntity
import com.example.petbeats.data.remote.model.calendar.home.request.ClinicIdRequest
import com.example.petbeats.data.remote.model.calendar.home.request.LocationRequest
import com.example.petbeats.data.remote.model.calendar.home.request.SuggestRequest
import com.example.petbeats.data.repository.HomeRepository
import com.example.petbeats.ui.home.search.adapterhint.HintChild
import com.example.petbeats.ui.home.search.adapterhistory.HistoryChild
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SearchViewModel(
    private val repository: HomeRepository,
    private val historyDao: HistoryDao
): ViewModel() {
    private var _state = MutableStateFlow(SearchState())
    val state = _state.asStateFlow()

    private var _event = MutableSharedFlow<SearchEvent>()
    val event = _event.asSharedFlow()


    //Gán database vào list của mình để hiển thị lên màn hình
    init {
        viewModelScope.launch {
            historyDao.listHistory().collect { historyDao ->
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
                val newHistory = HistoryEntity(keyword = search.trim())
                historyDao.insertHistory(newHistory)

                _state.value = _state.value.copy(search = "")

                _event.emit(SearchEvent.NavigationResultSearch(search))
            }
        }
    }

    fun bookClick() {
        viewModelScope.launch {
            _event.emit(SearchEvent.NavigationBook)
        }
    }

    fun checkButtonAll() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isButtonAll = !_state.value.isButtonAll)
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


    fun onHintList(latitude: Double, longitude: Double) {
        viewModelScope.launch {
            val request = LocationRequest(latitude, longitude)
            val result = repository.location(request)

            when (result) {
                is DataResult.Success -> {
                    val apiDataList = result.data ?: emptyList()

                    val showList = apiDataList.map { list ->
                        HintChild(
                            id = list.id,
                            roomName = list.name,
                            image = list.thumbnailUrl,
                            address = list.address
                        )
                    }

                    _state.value = _state.value.copy(listHint = showList)
                }
                is DataResult.Error -> {
                    _state.value = _state.value.copy(listHint = emptyList())
                }
            }
        }
    }

    fun onHintSearch() {
        viewModelScope.launch {
            val search = _state.value.search

            val request = SuggestRequest(search)
            val result = repository.suggest(request)

            when (result) {
                is DataResult.Success -> {
                    val apiDataList = result.data ?: emptyList()

                    val showList = apiDataList.map { list ->
                        HintChild(
                            id = list.id,
                            roomName = list.name,
                            image = list.thumbnailUrl,
                            address = list.address
                        )
                    }

                    _state.value = _state.value.copy(listSearch = showList)
                }
                is DataResult.Error -> {
                    _state.value = _state.value.copy(listSearch = emptyList())
                }
            }
        }
    }
}