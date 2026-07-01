package com.example.petbeats.ui.home.resultsearch

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.petbeats.core.base.DataResult
import com.example.petbeats.data.remote.model.calendar.home.request.SearchRequest
import com.example.petbeats.data.repository.HomeRepository
import com.example.petbeats.ui.home.resultsearch.adapter.ResultSearchChild
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ResultSearchViewModel(
    private val repository: HomeRepository
): ViewModel() {

    private val _state = MutableStateFlow(ResultSearchState())
    val state = _state.asStateFlow()

    private val _event = MutableSharedFlow< ResultSearchEvent>()
    val event = _event.asSharedFlow()

    fun backClick() {
        viewModelScope.launch {
            _event.emit(ResultSearchEvent.NavigationSearch)
        }
    }

    fun onCheck(isSearch: Boolean) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isSearch = isSearch)
        }
    }

    fun onChangeSearch(search: String) {
        _state.value = _state.value.copy(search = search)
    }

    fun onLatiLong(latitude: Double, longitude: Double) {
        _state.value = _state.value.copy(latitude = latitude, longitude = longitude)
    }

    fun itemClick(id: Int) {
        viewModelScope.launch {
            _event.emit(ResultSearchEvent.NavigationInformation(id))
        }
    }

    fun onResultSearchList() {
        viewModelScope.launch {
            val search = _state.value.search
            val latitude = _state.value.latitude
            val longitude = _state.value.longitude

            Log.d("TEST_API", "Đang gửi -> Keyword: $search, Tọa độ: $latitude, $longitude")

            val request = SearchRequest(search, latitude, longitude)
            val result = repository.search(request)


            when (result) {
                is DataResult.Success -> {
                    val apiDataList = result.data ?: emptyList()

                    val showList = apiDataList.map { list ->
                        ResultSearchChild(
                            id = list.id,
                            image = list.thumbnailUrl,
                            roomName = list.name,
                            isOperating = list.isOperating,
                            distance = list.distance,
                            rating = list.rating,
                            address = list.address,
                            closeTime = list.closeTime,
                            openTime = list.openTime
                        )
                    }

                    _state.value = _state.value.copy(listResultSearch = showList)
                }
                is DataResult.Error -> {
                    _state.value = _state.value.copy(listResultSearch = emptyList())
                }
            }
        }


    }
}