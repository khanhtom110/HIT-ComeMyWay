package com.vetpet.petbeats.ui.home_clinic.informationclinic

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vetpet.petbeats.core.base.DataResult
import com.vetpet.petbeats.data.remote.model.calendar.home_clinic.request.ProfileRequest
import com.vetpet.petbeats.data.repository.ErrorTarget
import com.vetpet.petbeats.data.repository.HomeClinicRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.MultipartBody

class InformationClinicViewModel(
    private val repository: HomeClinicRepository
): ViewModel() {
    private var _state = MutableStateFlow(InformationClinicState())
    val state = _state.asStateFlow()

    private var _event = MutableSharedFlow<InformationClinicEvent>()
    val event = _event.asSharedFlow()



    fun onNameChange(name: String) {
        _state.value = _state.value.copy(name = name, isName = false)
    }
    fun onPhoneChange(phone: String) {
        _state.value = _state.value.copy(phone = phone, isPhone = false)
    }
    fun onAddressChange(address: String) {
        _state.value = _state.value.copy(address = address, isAddress = false)
    }
    fun onStateChange(state: String) {
        _state.value = _state.value.copy(state = state, isInputState = false)
    }
    fun onLinkChange(link: String) {
        _state.value = _state.value.copy(link = link, isLink = false)
    }


    fun onTimeOpenSelect(openHour: String, openMinute: String) {
        _state.value = _state.value.copy(openHour = openHour, openMinute = openMinute)
    }
    fun onTimeCloseSelect(closeHour: String, closeMinute: String) {
        _state.value = _state.value.copy(closeHour = closeHour, closeMinute = closeMinute)
    }


    fun onOtherClinic() {
        _state.value = _state.value.copy(isFormVisible = true, inputCount = 1)
    }
    fun onAddRowClick() {
        _state.value = _state.value.copy(inputCount = _state.value.inputCount + 1)
    }
    fun onRemoteRowClick() {
        if (_state.value.inputCount > 1) {
            _state.value = _state.value.copy(inputCount = _state.value.inputCount - 1)
        }
        else {
            _state.value = _state.value.copy()
        }
    }
    fun onCancelClick() {
        _state.value = _state.value.copy(isFormVisible = false, inputCount = 1)
    }
    fun onServiceOpen(serviceName: String) {
        val currentList = _state.value.selectService.toMutableList()
        if (!currentList.contains(serviceName)) {
            currentList.add(serviceName)
            _state.value = _state.value.copy(selectService = currentList, isService = false)
        }
    }
    fun onServiceClose(serviceName: String) {
        val currentList = _state.value.selectService.toMutableList()
        if (currentList.contains(serviceName)) {
            currentList.remove(serviceName)
            _state.value = _state.value.copy(selectService = currentList)
        }
    }
    fun onSubmitClick(newService: List<String>) {
        val updatedList = _state.value.services.toMutableList().apply {
            addAll(newService)
        }

        _state.value = _state.value.copy(
            services = updatedList,
            isFormVisible = false,
            inputCount = 1
        )
    }


    fun onUploadImage(file: MultipartBody.Part) {
        viewModelScope.launch {
            val result = repository.onUploadImage(file)

            when(result) {
                is DataResult.Success -> {
                    val imageUrl = result.data
                    Log.d("UPLOAD_SUCCESS", "URL ảnh nhận từ Server: $imageUrl")

                    _state.value = _state.value.copy(image = imageUrl, isImage = false)
                }
                is DataResult.Error -> {
                    Log.e("UPLOAD_ERROR", "Upload ảnh thất bại: ${result.message}")
                    return@launch
                }
            }
        }

    }




    fun onInformationClinicClick() {
        viewModelScope.launch {
            val image = _state.value.image
            val name = _state.value.name
            val phone = _state.value.phone
            val address = _state.value.address
            val link = _state.value.link
            val openTime = "${_state.value.openHour}:${_state.value.openMinute}"
            val closeTime = "${_state.value.closeHour}:${_state.value.closeMinute}"
            val state = _state.value.state
            val service = _state.value.selectService

            val request = ProfileRequest(
                name = name,
                address = address,
                mapLink = link,
                phone = phone,
                description = state,
                thumbnailUrl = image,
                openTime = openTime,
                closeTime = closeTime,
                services = service)
            val result = repository.completeProfile(request)

            when (result) {
                is DataResult.Success -> {
                    _state.value = _state.value.copy(isName = false, isAddress = false, isLink = false, isPhone = false, isInputState = false, isImage = false, isTime = false, isService = false)

                    _event.emit(InformationClinicEvent.NavigationInformationSuccess)
                }
                is DataResult.Error -> {
                    Log.d("TEST_CASE", "Mã lỗi: ${result.target} - Lý do: ${result.message}")
                    _state.value = _state.value.copy(
                        isName = (result.target == ErrorTarget.NAME || result.target == ErrorTarget.GENERAL),
                        isPhone = (result.target == ErrorTarget.PHONE || result.target == ErrorTarget.GENERAL),
                        isLink = (result.target == ErrorTarget.LINK || result.target == ErrorTarget.GENERAL),
                        isInformation = (result.target == ErrorTarget.INFORMATION || result.target == ErrorTarget.GENERAL),
                        isTime = (result.target == ErrorTarget.TIME || result.target == ErrorTarget.GENERAL),
                        isService = (result.target == ErrorTarget.SERVICE || result.target == ErrorTarget.GENERAL),
                        nameError = if (result.target == ErrorTarget.NAME || result.target == ErrorTarget.GENERAL) result.message else "",
                        phoneError = if (result.target == ErrorTarget.PHONE || result.target == ErrorTarget.GENERAL) result.message else "",
                        linkError = if (result.target == ErrorTarget.LINK || result.target == ErrorTarget.GENERAL) result.message else "",
                        informationError = if (result.target == ErrorTarget.INFORMATION || result.target == ErrorTarget.GENERAL) result.message else "",
                        timeError = if (result.target == ErrorTarget.TIME || result.target == ErrorTarget.GENERAL) result.message else "",
                        serviceError = if (result.target == ErrorTarget.SERVICE || result.target == ErrorTarget.GENERAL) result.message else "",
                    )

                    return@launch
                }
            }
        }
    }
}