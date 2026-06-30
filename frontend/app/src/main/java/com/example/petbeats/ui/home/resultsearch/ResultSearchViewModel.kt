package com.example.petbeats.ui.home.resultsearch

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.petbeats.core.base.DataResult
import com.example.petbeats.data.remote.model.calendar.home.request.SearchRequest
import com.example.petbeats.data.repository.HomeRepository
import com.example.petbeats.ui.home.resultsearch.adapter.ResultSearchChild
import com.example.petbeats.ui.home.search.adapterhint.HintChild
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

    fun onChangeSearch(search: String) {
        _state.value = _state.value.copy(search = search)
    }

    fun onResultSearchList(latitude: Double, longitude: Double) {
        viewModelScope.launch {
            val search = _state.value.search

            android.util.Log.d("TEST_API", "Đang gửi lên Server -> Keyword: '$search' | Tọa độ: $latitude, $longitude")

            val request = SearchRequest(search, latitude, longitude)
            val result = repository.search(request)


            when (result) {
                is DataResult.Success -> {
                    android.util.Log.d("TEST_API", "Thành công! Tìm thấy: ${result.data?.size} phòng khám")

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
                    android.util.Log.e("TEST_API", "Toang rồi! Lỗi là: ${result.message}")

                    _state.value = _state.value.copy(listResultSearch = emptyList())
                }
            }
        }


    }
}