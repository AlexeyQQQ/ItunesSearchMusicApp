package ru.easycode.intensive2itunessearch.core

import kotlinx.coroutines.runBlocking
import okhttp3.Request
import okio.Timeout
import org.junit.Assert.assertEquals
import org.junit.Test
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.easycode.intensive2itunessearch.dashboard.data.cloud.CloudDataSource
import ru.easycode.intensive2itunessearch.dashboard.data.cloud.ItunesService
import ru.easycode.intensive2itunessearch.dashboard.data.cloud.ResponseCloud
import ru.easycode.intensive2itunessearch.dashboard.data.cloud.TrackCloud
import java.net.UnknownHostException

class ServiceTest {
    @Test
    fun testService() = runBlocking {
        val cloudDataSource = CloudDataSource.Base(2)
        val actual = cloudDataSource.data("sting")
        assertEquals(2, actual.size)
    }

    @Test
    fun testSuccess() = runBlocking {
        val cloudDataSource = CloudDataSource.Base(2, FakeSuccessService())
        val actual = cloudDataSource.data("test")
        val expected = listOf(
            TrackCloud(0, "testTitle1", "testSubTitle1", "previewUrl1", "artworkUrl1"),
            TrackCloud(1, "testTitle2", "testSubTitle2", "previewUrl2", "artworkUrl2"),
        )
        assertEquals(expected, actual)
    }

    @Test
    fun testUnknownHostException(): Unit = runBlocking {
        try {
            val cloudDataSource = CloudDataSource.Base(2, FakeErrorService(UnknownHostException()))
            cloudDataSource.data("test")
        } catch (e: Exception) {
            assertEquals(true, e is IllegalStateException)
            assertEquals("No internet connection", e.message)
        }
    }

    @Test
    fun testUnknownException(): Unit = runBlocking {
        try {
            val cloudDataSource =
                CloudDataSource.Base(2, FakeErrorService(IllegalArgumentException("unknown error")))
            cloudDataSource.data("test")
        } catch (e: Exception) {
            assertEquals(true, e is IllegalStateException)
            assertEquals("unknown error", e.message)
        }
    }
}

private class FakeSuccessService : ItunesService {

    override fun data(
        limit: Int,
        userText: String,
        media: String,
        entity: String
    ): Call<ResponseCloud> {
        return object : Call<ResponseCloud> {

            override fun clone(): Call<ResponseCloud> {
                TODO("Not yet implemented")
            }

            override fun execute(): Response<ResponseCloud> {
                return Response.success(
                    ResponseCloud(
                        listOf(
                            TrackCloud(
                                0,
                                "testTitle1",
                                "testSubTitle1",
                                "previewUrl1",
                                "artworkUrl1"
                            ),
                            TrackCloud(
                                1,
                                "testTitle2",
                                "testSubTitle2",
                                "previewUrl2",
                                "artworkUrl2"
                            ),
                        )
                    )
                )
            }

            override fun isExecuted(): Boolean {
                TODO("Not yet implemented")
            }

            override fun cancel() {
                TODO("Not yet implemented")
            }

            override fun isCanceled(): Boolean {
                TODO("Not yet implemented")
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
    }
}

private class FakeErrorService(private val exception: Exception) : ItunesService {
    override fun data(
        limit: Int,
        userText: String,
        media: String,
        entity: String
    ): Call<ResponseCloud> {
        throw exception
    }
}