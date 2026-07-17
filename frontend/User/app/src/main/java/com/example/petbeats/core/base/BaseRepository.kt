package com.example.petbeats.core.base

import com.example.petbeats.core.network.ApiResponse
import com.example.petbeats.data.repository.ErrorTarget
import com.example.petbeats.data.utils.ErrorUtils.getErrorTargetAndMessage
import com.google.gson.JsonParser
import retrofit2.HttpException
import java.io.IOException

abstract class BaseRepository {
    // Hàm dùng chung
    protected suspend fun <T> safeApiCall(apiCall: suspend () -> ApiResponse<T>): DataResult<T> {
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
}