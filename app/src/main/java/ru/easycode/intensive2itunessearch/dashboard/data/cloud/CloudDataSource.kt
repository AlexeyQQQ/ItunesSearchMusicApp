package ru.easycode.intensive2itunessearch.dashboard.data.cloud

import com.google.gson.annotations.SerializedName
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.UnknownHostException

interface CloudDataSource {

    suspend fun data(query: String): List<TrackCloud>

    class Base(
        private val max: Int,
        private val service: ItunesService
    ) : CloudDataSource {

        constructor(max: Int) : this(
            max = max,
            service = Retrofit.Builder().baseUrl("https://itunes.apple.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(
                    OkHttpClient.Builder()
                        .retryOnConnectionFailure(true)
                        .addInterceptor(HttpLoggingInterceptor().apply {
                            setLevel(HttpLoggingInterceptor.Level.BODY)
                        })
                        .build()
                )
                .build().create(ItunesService::class.java)
        )


        override suspend fun data(query: String): List<TrackCloud> {
            try {
                val data: Call<ResponseCloud> = service.data(limit = max, userText = query)
                return data.execute().body()!!.items
            } catch (e: Exception) {
                if (e is UnknownHostException)
                    throw IllegalStateException("No internet connection")
                else
                    throw IllegalStateException(e.message.toString())
            }
        }
    }
}

data class ResponseCloud(
    @SerializedName("results")
    val items: List<TrackCloud>,
)

data class TrackCloud(

    @SerializedName("trackId")
    val trackId: Long,
    @SerializedName("artistName")
    val artistName: String,
    @SerializedName("trackName")
    val trackName: String,
    @SerializedName("previewUrl")
    val previewUrl: String,
    @SerializedName("artworkUrl100")
    val artworkUrl: String,
)
