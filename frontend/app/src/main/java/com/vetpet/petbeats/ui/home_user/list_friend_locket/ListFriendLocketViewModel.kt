package com.vetpet.petbeats.ui.home_user.list_friend_locket

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vetpet.petbeats.core.base.DataResult
import com.vetpet.petbeats.data.remote.model.calendar.home_user.request.AddFriendRequest
import com.vetpet.petbeats.data.remote.model.calendar.home_user.request.LinkFriendRequest
import com.vetpet.petbeats.data.repository.HomeUserRepository
import com.vetpet.petbeats.ui.home_user.list_friend_locket.adapter.FriendChild
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ListFriendLocketViewModel(
    private val repository: HomeUserRepository,
): ViewModel() {
    private val _state = MutableStateFlow(ListFriendLocketState())
    val state = _state.asStateFlow()

    private val _event = MutableSharedFlow<ListFriendLocketEvent>()
    val event = _event.asSharedFlow()



    fun locketClick() {
        viewModelScope.launch {
            _event.emit(ListFriendLocketEvent.NavigationLocket)
        }
    }



    fun searchFriend(search: String) {
        _state.value = _state.value.copy(searchFriend = search)
    }

    fun isSearch(isSearch: Boolean) {
        _state.value = _state.value.copy(isSearch = isSearch)
    }
    fun showSearchFriend() {
        _state.value = _state.value.copy(isShowSearch = true)
    }



    fun itemClickAddFriend(id: Int) {
        viewModelScope.launch {
            val result = repository.acceptFriendLocket(id)

            when (result) {
                is DataResult.Success -> {
                    return@launch
                }
                is DataResult.Error -> {
                    return@launch
                }
            }
        }
    }
    fun itemClickCancelFriend(id: Int) {
        viewModelScope.launch {
            val result = repository.rejectFriendLocket(id)

            when (result) {
                is DataResult.Success -> {
                    return@launch
                }
                is DataResult.Error -> {
                    return@launch
                }
            }
        }
    }
    fun itemClickMyFriend(id: Int) {
        viewModelScope.launch {
            val result = repository.unFriend(id)

            when (result) {
                is DataResult.Success -> {
                    return@launch
                }
                is DataResult.Error -> {
                    return@launch
                }
            }
        }
    }
    fun itemClickMakeFriend(id: Int) {
        viewModelScope.launch {
            val request = AddFriendRequest(id)
            val result = repository.sendFriendLocket(request)

            when (result) {
                is DataResult.Success -> {
                    return@launch
                }
                is DataResult.Error -> {
                    return@launch
                }
            }
        }
    }



    fun onAddFriendList() {
        viewModelScope.launch {
            val result = repository.getPendingFriendLocket()

            when (result) {
                is DataResult.Success -> {
                    val apiDataList = result.data

                    val showList = apiDataList.map { list ->
                        FriendChild(
                            friendshipId = list.friendshipId,
                            friendId = list.friendId,
                            avatar = list.avatar,
                            username = list.username,
                        )
                    }

                    _state.value = _state.value.copy(pendingFriend = showList)
                }
                is DataResult.Error -> {
                    _state.value = _state.value.copy(pendingFriend = emptyList())
                }
            }
        }
    }
    fun onMyFriendList() {
        viewModelScope.launch {
            val result = repository.getMyFriendLocket()

            when (result) {
                is DataResult.Success -> {
                    val apiDataList = result.data

                    val showList = apiDataList.map { list ->
                        FriendChild(
                            friendshipId = list.friendshipId,
                            friendId = list.friendId,
                            avatar = list.avatar,
                            username = list.username
                        )
                    }

                    _state.value = _state.value.copy(myFriend = showList)
                }
                is DataResult.Error -> {
                    _state.value = _state.value.copy(myFriend = emptyList())
                }
            }
        }
    }
    fun onMakeFriendList() {
//        viewModelScope.launch {
//            val searchFriend = _state.value.searchFriend
//
//            val request = LinkFriendRequest(searchFriend)
//            val result = repository.findFriend(request)
//
//            when (result) {
//                is DataResult.Success -> {
//                    val apiDataList = result.data
//
//                    val showList = apiDataList.map { list ->
//                        FriendChild(
//                            friendshipId = list.friendshipId,
//                            friendId = list.friendId,
//                            avatar = list.avatar,
//                            username = list.username,
//                            isMakeFriend = true
//                        )
//                    }
//
//                    _state.value = _state.value.copy(myFriend = showList)
//                }
//                is DataResult.Error -> {
//                    _state.value = _state.value.copy(myFriend = emptyList())
//                }
//            }
//        }
    }


    fun onGetLinkLocket() {
        viewModelScope.launch {
            val result = repository.getLocketLink()

            when (result) {
                is DataResult.Success -> {
                    val linkData = result.data

                    _event.emit(ListFriendLocketEvent.CopyLink(linkData.toString()))
                }
                is DataResult.Error -> {
                    Log.d("TEST_LINK", "message: ${result.message}  result: ${result.code}")

                    return@launch
                }
            }
        }
    }

}