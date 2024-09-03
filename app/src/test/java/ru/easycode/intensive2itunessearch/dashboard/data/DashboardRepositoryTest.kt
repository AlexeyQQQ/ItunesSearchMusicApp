package ru.easycode.intensive2itunessearch.dashboard.data

import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import ru.easycode.intensive2itunessearch.core.data.cache.CurrentPlaylistCache
import ru.easycode.intensive2itunessearch.core.data.cache.CurrentTrackIdCache
import ru.easycode.intensive2itunessearch.core.data.cache.StringCache
import ru.easycode.intensive2itunessearch.core.data.cache.TrackCache
import ru.easycode.intensive2itunessearch.dashboard.data.cache.CacheDashboardDataSource
import ru.easycode.intensive2itunessearch.dashboard.data.cloud.CloudDataSource
import ru.easycode.intensive2itunessearch.dashboard.data.cloud.TrackCloud

class DashboardRepositoryTest {

    private lateinit var repository: DashboardRepository

    @Test
    fun cacheIsNotEmpty() = runBlocking {
        repository = DashboardRepository.Base(
            cloud = FakeCloudError(),
            cache = FakeCacheData(),
            currentTrackIdCache = FakeCurrentTrackIdCache(),
            currentPlaylistCache = FakeCurrentPlaylistCache(),
            userQuery = FakeStringCache(),
        )
        val actual = repository.tracks("test")
        val expected = LoadResult.Success(
            list = listOf(
                Track(
                    0,
                    "previewUrlCache1",
                    "artworkUrlCache1",
                    "trackNameCache1",
                    "artistNameCache1"
                ),
                Track(
                    1,
                    "previewUrlCache2",
                    "artworkUrlCache2",
                    "trackNameCache2",
                    "artistNameCache2"
                )
            ),
            query = "test"
        )
        assertEquals(expected, actual)
    }

    @Test
    fun cloudError() = runBlocking {
        repository = DashboardRepository.Base(
            cloud = FakeCloudError(),
            cache = FakeCacheEmpty(),
            currentTrackIdCache = FakeCurrentTrackIdCache(),
            currentPlaylistCache = FakeCurrentPlaylistCache(),
            userQuery = FakeStringCache(),
        )
        val actual = repository.tracks("test")
        val excepted = LoadResult.Error("FakeCloudError error")
        assertEquals(excepted, actual)
    }

    @Test
    fun cloudSuccess() = runBlocking {
        val cache = FakeCacheEmpty()
        repository = DashboardRepository.Base(
            cloud = FakeCloudSuccess(),
            cache = cache,
            currentTrackIdCache = FakeCurrentTrackIdCache(),
            currentPlaylistCache = FakeCurrentPlaylistCache(),
            userQuery = FakeStringCache(),
        )
        val actual = repository.tracks("test")
        val excepted = LoadResult.Success(
            list = listOf(
                Track(
                    0,
                    "previewUrlCloud1",
                    "artworkUrlCloud1",
                    "trackNameCloud1",
                    "artistNameCloud1"
                ),
                Track(
                    1,
                    "previewUrlCloud2",
                    "artworkUrlCloud2",
                    "trackNameCloud2",
                    "artistNameCloud2"
                )
            ),
            query = "test"
        )

        assertEquals(excepted, actual)
        assertEquals(excepted.list().map {
            TrackCache(
                term = "test",
                trackId = it.trackId,
                artistName = it.artistName,
                trackName = it.trackName,
                previewUrl = it.trackUrl,
                artworkUrl = it.coverUrl
            )
        }, cache.tracks("test"))
    }
}


private class FakeCacheData : CacheDashboardDataSource {

    override suspend fun save(term: String, data: List<TrackCloud>) = throw IllegalStateException()

    override suspend fun tracks(term: String): List<TrackCache> {
        return listOf(
            TrackCache(
                "term",
                0,
                "artistNameCache1",
                "trackNameCache1",
                "previewUrlCache1",
                "artworkUrlCache1"
            ),
            TrackCache(
                "term",
                1,
                "artistNameCache2",
                "trackNameCache2",
                "previewUrlCache2",
                "artworkUrlCache2"
            ),
        )
    }
}

private class FakeCacheEmpty : CacheDashboardDataSource {

    private val list = mutableListOf<TrackCache>()

    override suspend fun save(term: String, data: List<TrackCloud>) {
        val trackCacheList = data.map {
            TrackCache(
                term = term,
                trackId = it.trackId,
                artistName = it.artistName,
                trackName = it.trackName,
                previewUrl = it.previewUrl,
                artworkUrl = it.artworkUrl
            )
        }
        list.addAll(trackCacheList)
    }

    override suspend fun tracks(term: String): List<TrackCache> {
        return list
    }
}

private class FakeCloudError : CloudDataSource {

    override suspend fun data(query: String): List<TrackCloud> =
        throw IllegalStateException("FakeCloudError error")
}

private class FakeCloudSuccess : CloudDataSource {

    override suspend fun data(query: String): List<TrackCloud> {
        return listOf(
            TrackCloud(
                0,
                "artistNameCloud1",
                "trackNameCloud1",
                "previewUrlCloud1",
                "artworkUrlCloud1"
            ),
            TrackCloud(
                1,
                "artistNameCloud2",
                "trackNameCloud2",
                "previewUrlCloud2",
                "artworkUrlCloud2"
            ),
        )
    }
}

private class FakeCurrentPlaylistCache : CurrentPlaylistCache {

    var calledPlaylistIdArg: String = ""

    override fun isPlaylistFromQuery(): Boolean {
        val id = currentPlaylistQuery().toLongOrNull()
        return id == null
    }

    override fun currentPlaylistSaved(): Boolean {
        return currentPlaylistQuery().isNotEmpty()
    }

    override fun currentPlaylistId(): Long {
        return currentPlaylistQuery().toLong()
    }

    override fun currentPlaylistQuery(): String = calledPlaylistIdArg

    override fun saveCurrentPlaylistId(id: String) {
        calledPlaylistIdArg = id
    }
}

private class FakeCurrentTrackIdCache : CurrentTrackIdCache {

    var calledTrackIdArg: Long = -1

    override fun save(id: Long) {
        calledTrackIdArg = id
    }

    override fun read(): Long = calledTrackIdArg
}

private class FakeStringCache : StringCache {

    var saveUserQuery: String = ""

    override fun save(value: String) {
        saveUserQuery = value
    }

    override fun read(): String = saveUserQuery
}