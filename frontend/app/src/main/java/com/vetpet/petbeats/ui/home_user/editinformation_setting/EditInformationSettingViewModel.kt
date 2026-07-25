package com.vetpet.petbeats.ui.home_user.editinformation_setting

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vetpet.petbeats.core.base.DataResult
import com.vetpet.petbeats.data.remote.model.calendar.home_clinic.request.ProfileRequest
import com.vetpet.petbeats.data.remote.model.calendar.home_user.request.AppointmentIdRequest
import com.vetpet.petbeats.data.remote.model.calendar.home_user.request.UpdateProfileRequest
import com.vetpet.petbeats.data.repository.ErrorTarget
import com.vetpet.petbeats.data.repository.HomeUserRepository
import com.vetpet.petbeats.ui.home_clinic.informationclinic.InformationClinicEvent
import com.vetpet.petbeats.ui.home_user.editpassword_setting.EditPasswordSettingEvent
import com.vetpet.petbeats.ui.home_user.editpassword_setting.EditPasswordSettingState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.MultipartBody

class EditInformationSettingViewModel(
    private val repository: HomeUserRepository
): ViewModel() {
    private val _state = MutableStateFlow(EditInformationSettingState())
    val state = _state.asStateFlow()

    private val _event = MutableSharedFlow<EditInformationSettingEvent>()
    val event = _event.asSharedFlow()

    fun settingClick() {
        viewModelScope.launch {
            _event.emit(EditInformationSettingEvent.NavigationSetting)
        }
    }



    fun onNameChange(name: String) {
        _state.value = _state.value.copy(name = name, isName = false)
    }
    fun onPhoneChange(phone: String) {
        _state.value = _state.value.copy(phone = phone, isPhone = false)
    }
    fun onAddressChange(address: String) {
        _state.value = _state.value.copy(address = address, isAddress = false)
    }
    fun onEmailChange(email: String) {
        _state.value = _state.value.copy(email = email)
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



    fun onProfile(id: Int) {
        viewModelScope.launch {
            val image = _state.value.image
            val email = _state.value.email

            val requestProfile = UpdateProfileRequest( email, image, "không có")
            val result = repository.updateProfile(id, requestProfile)

            when(result) {
                is DataResult.Success -> {
                    _event.emit(EditInformationSettingEvent.NavigationSetting)
                }
                is DataResult.Error -> {
                    Log.d("TEST_CASE", "Mã lỗi: ${result.target} - Lý do: ${result.message}")
                    return@launch
                }
            }
        }
    }
    fun onInformationClick(id: Int) {
        viewModelScope.launch {
            val request = AppointmentIdRequest(id)
            val result = repository.takeAppointmentId(request)

            when (result) {
                is DataResult.Success -> {
                    _state.value = _state.value.copy(isName = false, isPhone = false)

                    _event.emit(EditInformationSettingEvent.NavigationSetting)
                }
                is DataResult.Error -> {
                    Log.d("TEST_CASE", "Mã lỗi: ${result.target} - Lý do: ${result.message}")
                    _state.value = _state.value.copy(
                        isName = (result.target == ErrorTarget.NAME || result.target == ErrorTarget.GENERAL),
                        isPhone = (result.target == ErrorTarget.PHONE || result.target == ErrorTarget.GENERAL),
                        nameError = if (result.target == ErrorTarget.NAME || result.target == ErrorTarget.GENERAL) result.message else "",
                        phoneError = if (result.target == ErrorTarget.PHONE || result.target == ErrorTarget.GENERAL) result.message else "",
                    )
                    return@launch
                }
            }
        }
    }



    fun onProfile() {
        viewModelScope.launch {
            val result = repository.profile()

            when (result) {
                is DataResult.Success -> {
                    val data = result.data

                    _state.value = _state.value.copy(
                        id = data.id,
                        image = data.avatar,
                        email = data.email,
                    )
                }
                is DataResult.Error -> {
                    Log.d("TEST_CASE", "lỗi trả: ${result.target} và message ${result.message}")

                    _state.value = _state.value.copy()
                    return@launch
                }
            }
        }
    }

}
