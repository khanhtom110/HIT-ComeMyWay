package com.vetpet.petbeats.data.repository

import com.vetpet.petbeats.core.base.BaseRepository
import com.vetpet.petbeats.core.base.DataResult
import com.vetpet.petbeats.data.remote.api.ApiUserHome
import com.vetpet.petbeats.data.remote.model.calendar.auth.request.LogoutRequest
import com.vetpet.petbeats.data.remote.model.calendar.auth.request.OtpRequest
import com.vetpet.petbeats.data.remote.model.calendar.auth.request.ResetPasswordRequest
import com.vetpet.petbeats.data.remote.model.calendar.auth.response.ForgotPasswordResponse
import com.vetpet.petbeats.data.remote.model.calendar.auth.response.OtpResponse
import com.vetpet.petbeats.data.remote.model.calendar.home_user.request.AddFriendRequest
import com.vetpet.petbeats.data.remote.model.calendar.home_user.request.AppointmentIdRequest
import com.vetpet.petbeats.data.remote.model.calendar.home_user.request.ChangePasswordUserRequest
import com.vetpet.petbeats.data.remote.model.calendar.home_user.request.ClinicIdRequest
import com.vetpet.petbeats.data.remote.model.calendar.home_user.request.CreateAppointmentRequest
import com.vetpet.petbeats.data.remote.model.calendar.home_user.request.CreatePostLocketRequest
import com.vetpet.petbeats.data.remote.model.calendar.home_user.request.DeviceTokenFireBaseRequest
import com.vetpet.petbeats.data.remote.model.calendar.home_user.request.LinkFriendRequest
import com.vetpet.petbeats.data.remote.model.calendar.home_user.request.LocationRequest
import com.vetpet.petbeats.data.remote.model.calendar.home_user.request.SearchRequest
import com.vetpet.petbeats.data.remote.model.calendar.home_user.request.TakeBookingRequest
import com.vetpet.petbeats.data.remote.model.calendar.home_user.request.UpdateProfileRequest
import com.vetpet.petbeats.data.remote.model.calendar.home_user.response.AddFriendResponse
import com.vetpet.petbeats.data.remote.model.calendar.home_user.response.AppointmentIdResponse
import com.vetpet.petbeats.data.remote.model.calendar.home_user.response.ClinicIdResponse
import com.vetpet.petbeats.data.remote.model.calendar.home_user.response.CreateAppointmentResponse
import com.vetpet.petbeats.data.remote.model.calendar.home_user.response.CreatePostLocketResponse
import com.vetpet.petbeats.data.remote.model.calendar.home_user.response.LinkFriendResponse
import com.vetpet.petbeats.data.remote.model.calendar.home_user.response.LocationResponse
import com.vetpet.petbeats.data.remote.model.calendar.home_user.response.MyPostLocketResponse
import com.vetpet.petbeats.data.remote.model.calendar.home_user.response.SearchResponse
import com.vetpet.petbeats.data.remote.model.calendar.home_user.response.TakeAppointmentResponse
import com.vetpet.petbeats.data.remote.model.calendar.home_user.response.TakeBookingResponse
import com.vetpet.petbeats.data.remote.model.calendar.home_user.response.UpdateProfileResponse
import okhttp3.MultipartBody

class HomeUserRepository(
    private val apiUserHome: ApiUserHome
): BaseRepository() {
    suspend fun location(request: LocationRequest): DataResult<List<LocationResponse>> {
        return safeApiCall {
            apiUserHome.location(
                latitude = request.latitude,
                longitude = request.longitude
            )
        }
    }

    suspend fun clinicid(request: ClinicIdRequest): DataResult<ClinicIdResponse> {
        return safeApiCall {
            apiUserHome.clinicid(
                clinicId = request.id,
                latitude = request.latitude,
                longitude = request.longitude
            )
        }
    }

    suspend fun search(request: SearchRequest): DataResult<List<SearchResponse>> {
        return safeApiCall {
            apiUserHome.search(
                keyword = request.keyword,
                latitude = request.latitude,
                longitude = request.longitude
            )
        }
    }

    suspend fun takeBooking(request: TakeBookingRequest): DataResult<TakeBookingResponse> {
        return safeApiCall {
            apiUserHome.takeBooking(
                clinicId = request.clinicId
            )
        }
    }

    suspend fun takeAppointment(): DataResult<List<TakeAppointmentResponse>> {
        return safeApiCall {
            apiUserHome.takeAppointment()
        }
    }

    suspend fun createAppointment(request: CreateAppointmentRequest): DataResult<CreateAppointmentResponse> {
        return safeApiCall {
            apiUserHome.createAppointment(request)
        }
    }

    suspend fun editAppointment(id: AppointmentIdRequest, request: CreateAppointmentRequest): DataResult<CreateAppointmentResponse> {
        return safeApiCall {
            apiUserHome.editAppointment(
                id = id.id,
                request = request
            )
        }
    }

    suspend fun takeAppointmentId(request: AppointmentIdRequest): DataResult<AppointmentIdResponse> {
        return safeApiCall {
            apiUserHome.takeAppointmentId(
                id = request.id
            )
        }
    }

    suspend fun cancelAppointment(request: AppointmentIdRequest): DataResult<AppointmentIdResponse> {
        return safeApiCall {
            apiUserHome.cancelAppointment(
                id = request.id
            )
        }
    }

    suspend fun deviceToken(request: DeviceTokenFireBaseRequest): DataResult<Any> {
        return safeApiCall {
            apiUserHome.deviceToken(request)
        }
    }

    suspend fun updateProfile(id: Int, request: UpdateProfileRequest): DataResult<UpdateProfileResponse> {
        return safeApiCall {
            apiUserHome.updateProfile(
                id = id,
                request = request
            )
        }
    }

    suspend fun profile(): DataResult<UpdateProfileResponse> {
        return safeApiCall {
            apiUserHome.profile()
        }
    }

    suspend fun logoutUser(request: LogoutRequest): DataResult<Unit> {
        return safeApiCall {
            apiUserHome.logout(request)
        }
    }

    suspend fun onUploadImage(file: MultipartBody.Part?): DataResult<String> {
        return  safeApiCall {
            apiUserHome.uploadImage(file)
        }
    }

    suspend fun changePasswordUser(request: ChangePasswordUserRequest): DataResult<Any> {
        return safeApiCall {
            apiUserHome.changePasswordUser(request)
        }
    }

    suspend fun chatBot(request: ChatRequest): DataResult<ChatResponse> {
        return safeApiCall {
            apiUserHome.chatBot(request)
        }
    }



    suspend fun forgotPasswordUser(request: ForgotPasswordRequest): DataResult<ForgotPasswordResponse> {
        return safeApiCall {
            apiUserHome.forgotpassword(request)
        }
    }

    suspend fun resetOtpUser(request: OtpRequest): DataResult<OtpResponse> {
        return safeApiCall {
            apiUserHome.resetotp(request)
        }
    }

    suspend fun resetpasswordUser(request: ResetPasswordRequest): DataResult<Nothing> {
        return safeApiCall {
            apiUserHome.resetpassword(request)
        }
    }



    suspend fun createPostLocket(request: CreatePostLocketRequest): DataResult<CreatePostLocketResponse> {
        return safeApiCall {
            apiUserHome.createPostLocket(request)
        }
    }

    suspend fun getMyPostLocket(lastPostId: Int?, size: Int): DataResult<MyPostLocketResponse> {
        return safeApiCall {
            apiUserHome.getMyPostLocket(lastPostId, size)
        }
    }

    suspend fun getMyFeedLocket(lastPostId: Int?, size: Int): DataResult<MyPostLocketResponse> {
        return safeApiCall {
            apiUserHome.getMyFeedLocket(lastPostId, size)
        }
    }

    suspend fun rejectFriendLocket(requestId: Int): DataResult<Any> {
        return safeApiCall {
            apiUserHome.rejectFriendLocket(requestId)
        }
    }

    suspend fun sendFriendLocket(request: AddFriendRequest): DataResult<AddFriendResponse> {
        return safeApiCall {
            apiUserHome.sendFriendLocket(request)
        }
    }

    suspend fun acceptFriendLocket(requestId: Int): DataResult<Any> {
        return safeApiCall {
            apiUserHome.acceptFriendLocket(requestId)
        }
    }

    suspend fun getPendingFriendLocket(): DataResult<List<AddFriendResponse>> {
        return safeApiCall {
            apiUserHome.getPendingFriendLocket()
        }
    }

    suspend fun getMyFriendLocket(): DataResult<List<AddFriendResponse>> {
        return safeApiCall {
            apiUserHome.getMyFriendLocket()
        }
    }

    suspend fun getLocketLink(): DataResult<String> {
        return safeApiCall {
            apiUserHome.getLocketLink()
        }
    }

    suspend fun findFriend(request: LinkFriendRequest): DataResult<LinkFriendResponse> {
        return safeApiCall {
            apiUserHome.findFriend(request)
        }
    }

    suspend fun unFriend(friendId: Int): DataResult<Unit> {
        return safeApiCall {
            apiUserHome.unFriend(friendId)
        }
    }

}