package com.vetpet.petbeats.ui.home_user.list_friend_locket

import androidx.lifecycle.ViewModel
import com.vetpet.petbeats.data.repository.HomeUserRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow

class ListFriendLocketViewModel(
    private val repository: HomeUserRepository,
): ViewModel() {
    private val _state = MutableStateFlow(ListFriendLocketState())
    val state = _state.asStateFlow()

    private val _event = MutableSharedFlow<ListFriendLocketEvent>()
    val event = _event.asSharedFlow()
}