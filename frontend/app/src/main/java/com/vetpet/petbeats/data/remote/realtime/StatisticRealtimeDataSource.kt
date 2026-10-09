package com.vetpet.petbeats.data.remote.realtime

import com.google.gson.Gson
import com.vetpet.petbeats.data.remote.model.calendar.home_admin.response.StatisticResponse
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.conflate
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.sse.EventSource
import okhttp3.sse.EventSourceListener
import okhttp3.sse.EventSources
import retrofit2.Retrofit
import java.io.IOException
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

class StatisticsHttpException(
    val code: Int
) : IOException("HTTP $code khi kết nối thống kê")



@Singleton
class StatisticRealtimeDataSource @Inject constructor(
    @Named("NodeAuthRetrofit") retrofit: Retrofit
) {
    private val gson = Gson()

    private val client = (retrofit.callFactory() as OkHttpClient)
            .newBuilder()
            // Backend gửi keepalive mỗi 15 giây.
            .readTimeout(45, TimeUnit.SECONDS)
            .callTimeout(0, TimeUnit.SECONDS)
            .build()

    private val streamUrl = requireNotNull(
        retrofit.baseUrl().resolve(
            "/api/v1/admin/statistics/stream"
        )
    )

    fun observe(): Flow<StatisticResponse> = callbackFlow<StatisticResponse> {
            val request = Request.Builder()
                .url(streamUrl)
                .header("Accept", "text/event-stream")
                .build()

            val listener = object : EventSourceListener() {
                override fun onEvent(
                    eventSource: EventSource,
                    id: String?,
                    type: String?,
                    data: String
                ) {
                    if (type != "statistics") return

                    try {
                        val snapshot = requireNotNull(
                            gson.fromJson(
                                data,
                                StatisticResponse::class.java
                            )
                        )

                        trySend(snapshot)
                    } catch (e: Exception) {
                        close(e)
                    }
                }

                override fun onFailure(
                    eventSource: EventSource,
                    t: Throwable?,
                    response: Response?
                ) {
                    val error =
                        if (response != null &&
                            !response.isSuccessful
                        ) {
                            StatisticsHttpException(response.code)
                        } else {
                            t ?: IOException("Mất kết nối thống kê")
                        }

                    close(error)
                }

                override fun onClosed(eventSource: EventSource) {
                    close(IOException("Kết nối thống kê đã đóng"))
                }
            }

            val source = EventSources.createFactory(client)
                .newEventSource(request, listener)

            awaitClose {
                source.cancel()
            }
        }.conflate()
}