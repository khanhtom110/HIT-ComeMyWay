package com.vetpet.petbeats.data.repository.repository_springboot

import com.vetpet.petbeats.core.base.BaseRepository
import com.vetpet.petbeats.core.base.DataResult
import com.vetpet.petbeats.data.remote.api.api_springboot.ApiUserHome
import com.vetpet.petbeats.data.remote.dto.auth.request.ForgotPasswordRequest
import com.vetpet.petbeats.data.remote.dto.auth.request.LogoutRequest
import com.vetpet.petbeats.data.remote.dto.auth.request.OtpRequest
import com.vetpet.petbeats.data.remote.dto.auth.request.ResetPasswordRequest
import com.vetpet.petbeats.data.remote.dto.auth.response.ForgotPasswordResponse
import com.vetpet.petbeats.data.remote.dto.auth.response.OtpResponse
import com.vetpet.petbeats.data.remote.dto.home_user.request.AddFriendRequest
import com.vetpet.petbeats.data.remote.dto.home_user.request.AppointmentIdRequest
import com.vetpet.petbeats.data.remote.dto.home_user.request.ChangePasswordUserRequest
import com.vetpet.petbeats.data.remote.dto.home_user.request.ChatRequest
import com.vetpet.petbeats.data.remote.dto.home_user.request.ClinicIdRequest
import com.vetpet.petbeats.data.remote.dto.home_user.request.CreateAppointmentRequest
import com.vetpet.petbeats.data.remote.dto.home_user.request.CreatePostLocketRequest
import com.vetpet.petbeats.data.remote.dto.home_user.request.DeviceTokenFireBaseRequest
import com.vetpet.petbeats.data.remote.dto.home_user.request.LinkFriendRequest
import com.vetpet.petbeats.data.remote.dto.home_user.request.LocationRequest
import com.vetpet.petbeats.data.remote.dto.home_user.request.SearchRequest
import com.vetpet.petbeats.data.remote.dto.home_user.request.TakeBookingRequest
import com.vetpet.petbeats.data.remote.dto.home_user.request.UpdateProfileRequest
import com.vetpet.petbeats.data.remote.dto.home_user.response.AddFriendResponse
import com.vetpet.petbeats.data.remote.dto.home_user.response.AppointmentIdResponse
import com.vetpet.petbeats.data.remote.dto.home_user.response.ClinicIdResponse
import com.vetpet.petbeats.data.remote.dto.home_user.response.ClinicPostConstainResponse
import com.vetpet.petbeats.data.remote.dto.home_user.response.ClinicPostDetailResponse
import com.vetpet.petbeats.data.remote.dto.home_user.response.CreateAppointmentResponse
import com.vetpet.petbeats.data.remote.dto.home_user.response.CreatePostLocketResponse
import com.vetpet.petbeats.data.remote.dto.home_user.response.LinkFriendResponse
import com.vetpet.petbeats.data.remote.dto.home_user.response.LocationResponse
import com.vetpet.petbeats.data.remote.dto.home_user.response.MyPostLocketResponse
import com.vetpet.petbeats.data.remote.dto.home_user.response.SearchResponse
import com.vetpet.petbeats.data.remote.dto.home_user.response.TakeAppointmentResponse
import com.vetpet.petbeats.data.remote.dto.home_user.response.TakeBookingResponse
import com.vetpet.petbeats.data.remote.dto.home_user.response.UpdateProfileResponse
import com.vetpet.petbeats.data.remote.dto.home_user.response.chatbotresponse.ChatResponse
import okhttp3.MultipartBody
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HomeUserRepository @Inject constructor(
    private val apiUserHome: ApiUserHome
): BaseRepository() {
    suspend fun location(request: com.vetpet.petbeats.data.remote.dto.home_user.request.LocationRequest): DataResult<List<com.vetpet.petbeats.data.remote.dto.home_user.response.LocationResponse>> {
        return safeApiCall {
            apiUserHome.location(
                latitude = request.latitude,
                longitude = request.longitude
            )
        }
    }

    suspend fun clinicid(request: com.vetpet.petbeats.data.remote.dto.home_user.request.ClinicIdRequest): DataResult<com.vetpet.petbeats.data.remote.dto.home_user.response.ClinicIdResponse> {
        return safeApiCall {
            apiUserHome.clinicid(
                clinicId = request.id,
                latitude = request.latitude,
                longitude = request.longitude
            )
        }
    }

    suspend fun search(request: com.vetpet.petbeats.data.remote.dto.home_user.request.SearchRequest): DataResult<List<com.vetpet.petbeats.data.remote.dto.home_user.response.SearchResponse>> {
        return safeApiCall {
            apiUserHome.search(
                keyword = request.keyword,
                latitude = request.latitude,
                longitude = request.longitude
            )
        }
    }

    suspend fun takeBooking(request: com.vetpet.petbeats.data.remote.dto.home_user.request.TakeBookingRequest): DataResult<com.vetpet.petbeats.data.remote.dto.home_user.response.TakeBookingResponse> {
        return safeApiCall {
            apiUserHome.takeBooking(
                clinicId = request.clinicId
            )
        }
    }

    suspend fun takeAppointment(): DataResult<List<com.vetpet.petbeats.data.remote.dto.home_user.response.TakeAppointmentResponse>> {
        return safeApiCall {
            apiUserHome.takeAppointment()
        }
    }

    suspend fun createAppointment(request: com.vetpet.petbeats.data.remote.dto.home_user.request.CreateAppointmentRequest): DataResult<com.vetpet.petbeats.data.remote.dto.home_user.response.CreateAppointmentResponse> {
        return safeApiCall {
            apiUserHome.createAppointment(request)
        }
    }

    suspend fun editAppointment(id: com.vetpet.petbeats.data.remote.dto.home_user.request.AppointmentIdRequest, request: com.vetpet.petbeats.data.remote.dto.home_user.request.CreateAppointmentRequest): DataResult<com.vetpet.petbeats.data.remote.dto.home_user.response.CreateAppointmentResponse> {
        return safeApiCall {
            apiUserHome.editAppointment(
                id = id.id,
                request = request
            )
        }
    }

    suspend fun takeAppointmentId(request: com.vetpet.petbeats.data.remote.dto.home_user.request.AppointmentIdRequest): DataResult<com.vetpet.petbeats.data.remote.dto.home_user.response.AppointmentIdResponse> {
        return safeApiCall {
            apiUserHome.takeAppointmentId(
                id = request.id
            )
        }
    }

    suspend fun cancelAppointment(request: com.vetpet.petbeats.data.remote.dto.home_user.request.AppointmentIdRequest): DataResult<com.vetpet.petbeats.data.remote.dto.home_user.response.AppointmentIdResponse> {
        return safeApiCall {
            apiUserHome.cancelAppointment(
                id = request.id
            )
        }
    }

    suspend fun deviceToken(request: com.vetpet.petbeats.data.remote.dto.home_user.request.DeviceTokenFireBaseRequest): DataResult<Any> {
        return safeApiCall {
            apiUserHome.deviceToken(request)
        }
    }

    suspend fun updateProfile(id: Int, request: com.vetpet.petbeats.data.remote.dto.home_user.request.UpdateProfileRequest): DataResult<com.vetpet.petbeats.data.remote.dto.home_user.response.UpdateProfileResponse> {
        return safeApiCall {
            apiUserHome.updateProfile(
                id = id,
                request = request
            )
        }
    }

    suspend fun profile(): DataResult<com.vetpet.petbeats.data.remote.dto.home_user.response.UpdateProfileResponse> {
        return safeApiCall {
            apiUserHome.profile()
        }
    }

    suspend fun logoutUser(token: String, request: LogoutRequest): DataResult<Unit> {
        return safeApiCall {
            apiUserHome.logout(token, request)
        }
    }

    suspend fun onUploadImage(file: MultipartBody.Part?): DataResult<String> {
        return  safeApiCall {
            apiUserHome.uploadImage(file)
        }
    }

    suspend fun changePasswordUser(request: com.vetpet.petbeats.data.remote.dto.home_user.request.ChangePasswordUserRequest): DataResult<Any> {
        return safeApiCall {
            apiUserHome.changePasswordUser(request)
        }
    }

    suspend fun chatBot(request: com.vetpet.petbeats.data.remote.dto.home_user.request.ChatRequest): DataResult<com.vetpet.petbeats.data.remote.dto.home_user.response.chatbotresponse.ChatResponse> {
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



    suspend fun createPostLocket(request: com.vetpet.petbeats.data.remote.dto.home_user.request.CreatePostLocketRequest): DataResult<com.vetpet.petbeats.data.remote.dto.home_user.response.CreatePostLocketResponse> {
        return safeApiCall {
            apiUserHome.createPostLocket(request)
        }
    }

    suspend fun getMyPostLocket(lastPostId: Int?, size: Int): DataResult<com.vetpet.petbeats.data.remote.dto.home_user.response.MyPostLocketResponse> {
        return safeApiCall {
            apiUserHome.getMyPostLocket(lastPostId, size)
        }
    }

    suspend fun getMyFeedLocket(lastPostId: Int?, size: Int): DataResult<com.vetpet.petbeats.data.remote.dto.home_user.response.MyPostLocketResponse> {
        return safeApiCall {
            apiUserHome.getMyFeedLocket(lastPostId, size)
        }
    }

    suspend fun rejectFriendLocket(requestId: Int): DataResult<Any> {
        return safeApiCall {
            apiUserHome.rejectFriendLocket(requestId)
        }
    }

    suspend fun sendFriendLocket(request: com.vetpet.petbeats.data.remote.dto.home_user.request.AddFriendRequest): DataResult<com.vetpet.petbeats.data.remote.dto.home_user.response.AddFriendResponse> {
        return safeApiCall {
            apiUserHome.sendFriendLocket(request)
        }
    }

    suspend fun acceptFriendLocket(requestId: Int): DataResult<Any> {
        return safeApiCall {
            apiUserHome.acceptFriendLocket(requestId)
        }
    }

    suspend fun getPendingFriendLocket(): DataResult<List<com.vetpet.petbeats.data.remote.dto.home_user.response.AddFriendResponse>> {
        return safeApiCall {
            apiUserHome.getPendingFriendLocket()
        }
    }

    suspend fun getMyFriendLocket(): DataResult<List<com.vetpet.petbeats.data.remote.dto.home_user.response.AddFriendResponse>> {
        return safeApiCall {
            apiUserHome.getMyFriendLocket()
        }
    }

    suspend fun getLocketLink(): DataResult<String> {
        return safeApiCall {
            apiUserHome.getLocketLink()
        }
    }

    suspend fun findFriend(request: com.vetpet.petbeats.data.remote.dto.home_user.request.LinkFriendRequest): DataResult<com.vetpet.petbeats.data.remote.dto.home_user.response.LinkFriendResponse> {
        return safeApiCall {
            apiUserHome.findFriend(request)
        }
    }

    suspend fun unFriend(friendId: Int): DataResult<Unit> {
        return safeApiCall {
            apiUserHome.unFriend(friendId)
        }
    }

    suspend fun deletePost(postId: Int?): DataResult<Unit> {
        return safeApiCall {
            apiUserHome.deletePost(postId)
        }
    }

    suspend fun clinicPost(lastPostId: Int?, limit: Int): DataResult<com.vetpet.petbeats.data.remote.dto.home_user.response.ClinicPostConstainResponse> {
        return safeApiCall {
            apiUserHome.clinicPost(lastPostId, limit)
        }
    }

    suspend fun clinicPostDetail(id: Int): DataResult<com.vetpet.petbeats.data.remote.dto.home_user.response.ClinicPostDetailResponse> {
        return safeApiCall {
            apiUserHome.clinicPostDetail(id)
        }
    }
}