package com.example.petbeats.data.repository

import com.example.petbeats.core.base.DataResult
import com.example.petbeats.core.network.ApiResponse
import com.example.petbeats.data.remote.model.calendar.auth.request.LogoutRequest
import com.example.petbeats.data.remote.api.ApiAuth
import com.example.petbeats.data.remote.model.calendar.auth.request.ForgotPasswordRequest
import com.example.petbeats.data.remote.model.calendar.auth.request.LoginRequest
import com.example.petbeats.data.remote.model.calendar.auth.response.LoginResponse
import com.example.petbeats.data.remote.model.calendar.auth.request.OtpRequest
import com.example.petbeats.data.remote.model.calendar.auth.response.OtpResponse
import com.example.petbeats.data.remote.model.calendar.auth.request.RegisterRequest
import com.example.petbeats.data.remote.model.calendar.auth.response.RegisterResponse
import com.example.petbeats.data.remote.model.calendar.auth.request.ResetPasswordRequest
import com.google.gson.JsonParser
import retrofit2.HttpException
import java.io.IOException

class AuthRepository(
    private val apiAuth: ApiAuth
) {

    private fun getErrorTargetAndMessage(englishMessage: String?): Pair<ErrorTarget, String> {
        return when (englishMessage) {
            //Username
            "Username already exists. Please choose a different one." ->
                Pair(ErrorTarget.NAME, "Tên đăng nhập đã tồn tại. Vui lòng chọn tên khác.")
            "Username must be 4-120 characters long and contain only letters, numbers, and underscores." -> Pair(
                ErrorTarget.NAME, "Tên đăng nhập chỉ được chứa chữ, số và dấu gạch dưới.")
            "Username length is invalid. It must be between 4 and 120 characters." -> Pair(
                ErrorTarget.NAME, "Tên người dùng không hợp lệ. Tên người dùng phải có độ \ndài từ 4 đến 120 ký tự.")


            //Email
            "Email already exists. Please use another email or log in." ->
                Pair(ErrorTarget.EMAIL, "Email này đã được sử dụng.")
            "Please enter a valid email address." ->
                Pair(ErrorTarget.EMAIL, "Định dạng email không hợp lệ.")
            "User does not exist." ->
                Pair(ErrorTarget.EMAIL, "Người dùng không tồn tại.")

            //Password
            "Password must be 8-120 characters long and include at least one uppercase letter, one lowercase letter, one number, and one special character." ->
                Pair(ErrorTarget.PASSWORD, "Mật khẩu chưa đủ mạnh (cần chữ hoa, chữ thường, số và \nký tự đặc biệt).")
            "Passwords do not match." ->
                Pair(ErrorTarget.PASSWORD, "Mật khẩu xác nhận không khớp.")
            "New password must be different from the current password." ->
                Pair(ErrorTarget.PASSWORD, "Mật khẩu mới phải khác mật khẩu hiện tại.")

            //Otp
            "The provided OTP is invalid. Please check and try again." ->
                Pair(ErrorTarget.OTP, "Mã OTP không hợp lệ.")
            "The OTP has expired. Please request a new one." ->
                Pair(ErrorTarget.OTP, "Mã OTP đã hết hạn. Vui lòng yêu cầu mã mới.")

            //General, Hệ thống, token
            "One or more fields contain invalid data." ->
                Pair(ErrorTarget.GENERAL, "Dữ liệu không hợp lệ. Vui lòng kiểm tra lại.")
            "This field is required and cannot be empty.", "This field cannot be blank." ->
                Pair(ErrorTarget.GENERAL, "Vui lòng nhập đầy đủ thông tin bắt buộc.")
            "Your session has expired. Please log in again.", "Session expired" ->
                Pair(ErrorTarget.GENERAL, "Phiên đăng nhập đã hết hạn. Vui lòng đăng nhập lại.")
            "Login failed. Please check your credentials and try again." ->
                Pair(ErrorTarget.GENERAL, "Đăng nhập thất bại. Vui lòng thử lại.")
            "Failed to send the email. Please try again later." ->
                Pair(ErrorTarget.GENERAL, "Gửi email thất bại. Vui lòng thử lại sau.")
            "An unexpected system error occurred. Please try again later." ->
                Pair(ErrorTarget.GENERAL, "Lỗi hệ thống. Vui lòng thử lại sau.")

            "The request is invalid or malformed." ->
                Pair(ErrorTarget.GENERAL, "Vui lòng nhập đầy đủ thông tin")
            "Too many request. Please try again after 1 minute." ->
                Pair(ErrorTarget.GENERAL, "Có quá nhiều yêu cầu. Làm ơn hãy thử lại sau 1 phút")
            "Invalid username or password." ->
                Pair(ErrorTarget.GENERAL, "Tài khoản hoặc mật khẩu không chính xác.")

            //Be trả lỗi lạ
            null -> Pair(ErrorTarget.GENERAL, "Lỗi không xác định.")
            else -> Pair(ErrorTarget.GENERAL, englishMessage)
        }
    }

    // Hàm dùng chung
    private suspend fun <T> safeApiCall(apiCall: suspend () -> ApiResponse<T>): DataResult<T> {
        return try {
            val response = apiCall()
            DataResult.Success<T>(data = response.data as T, message = getErrorTargetAndMessage(response.message).second)
        } catch (e: HttpException) {
            val errorMessage = try {
                val errorBody = e.response()?.errorBody()?.string()

                val jsonObject = JsonParser.parseString(errorBody).asJsonObject


                var finalError: String? = if (jsonObject.has("message")) jsonObject.get("message").asString else null

                val dataElement = jsonObject.get("data")
                if (dataElement != null && dataElement.isJsonObject) {
                    val dataObj = dataElement.asJsonObject

                    val firstKey = dataObj.keySet().firstOrNull()
                    if (firstKey != null) {
                        finalError = dataObj.get(firstKey).asString
                    }
                }


                getErrorTargetAndMessage(finalError)
            } catch (e: Exception) {
                Pair(ErrorTarget.GENERAL, "Lỗi dữ liệu lấy từ máy chủ")
            }

            DataResult.Error(target = errorMessage.first, message = errorMessage.second)
        } catch (e: IOException) {
            DataResult.Error(target = ErrorTarget.GENERAL, message = "Không có kết nối mạng. Vui lòng kiểm tra lại")
        } catch (e: Exception) {
            DataResult.Error(target = ErrorTarget.GENERAL, message = "Đã có lỗi bất ngờ xảy ra: ${e.message}")
        }
    }

    suspend fun loginUser(request: LoginRequest): DataResult<LoginResponse> {
        return safeApiCall {
            apiAuth.login(request)
        }
    }

    suspend fun registerUser(request: RegisterRequest): DataResult<RegisterResponse> {
        return safeApiCall {
            apiAuth.register(request)
        }
    }

    suspend fun forgotpasswordUser(request: ForgotPasswordRequest): DataResult<RegisterResponse> {
        return safeApiCall {
            apiAuth.forgotpassword(request)
        }
    }

    suspend fun resetOtpUser(request: OtpRequest): DataResult<OtpResponse> {
        return safeApiCall {
            apiAuth.resetotp(request)
        }
    }

    suspend fun registerOtpUser(request: OtpRequest): DataResult<OtpResponse> {
        return safeApiCall {
            apiAuth.registerotp(request)
        }
    }

    suspend fun resetpasswordUser(request: ResetPasswordRequest): DataResult<Nothing> {
        return safeApiCall {
            apiAuth.resetpassword(request)
        }
    }

    suspend fun logoutUser(request: LogoutRequest): DataResult<Nothing> {
        return safeApiCall {
            apiAuth.logout(request)
        }
    }
}