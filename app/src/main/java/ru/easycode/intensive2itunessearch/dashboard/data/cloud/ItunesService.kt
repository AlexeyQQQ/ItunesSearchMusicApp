package ru.easycode.intensive2itunessearch.dashboard.data.cloud

import okhttp3.Request
import okio.Timeout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import java.net.UnknownHostException

interface ItunesService {

    @GET("search")
    fun data(
        @Query("limit") limit: Int,
        @Query("term") userText: String,
        @Query("media") media: String = "music",
        @Query("entity") entity: String = "song",
    ): Call<ResponseCloud>
}

class FakeService : ItunesService {

    private val response = ResponseCloud(
        listOf(
            TrackCloud(
                0,
                "testSubTitle1",
                "testTitle1",
                "previewUrl1",
                "artworkUrl1"
            ),
            TrackCloud(
                1,
                "testSubTitle2",
                "testTitle2",
                "previewUrl2",
                "artworkUrl2"
            ),
        )
    )

    private var showSuccess = false

    override fun data(limit: Int, userText: String, media: String, entity: String): Call<ResponseCloud> {
        Thread.sleep(1000)
        if (showSuccess)
            return object : Call<ResponseCloud> {
                override fun clone(): Call<ResponseCloud> {
                    return this
                }

                override fun execute(): Response<ResponseCloud> {
                    return Response.success(response)
                }

                override fun isExecuted(): Boolean {
                    return false
                }

                override fun cancel() {
                }

                override fun isCanceled(): Boolean {
                    return false
                }

                override fun request(): Request {
                    TODO("Not yet implemented")
                }

                override fun timeout(): Timeout {
                    TODO("Not yet implemented")
                }

                override fun enqueue(callback: Callback<ResponseCloud>) {
                    TODO("Not yet implemented")
                }
            }
        else {
            showSuccess = true
            throw UnknownHostException()
        }
    }
}