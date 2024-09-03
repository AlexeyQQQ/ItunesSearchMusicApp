package ru.easycode.intensive2itunessearch.dashboard.data

import ru.easycode.intensive2itunessearch.core.data.cache.CurrentPlaylistCache
import ru.easycode.intensive2itunessearch.core.data.cache.CurrentTrackIdCache
import ru.easycode.intensive2itunessearch.core.data.cache.StringCache
import ru.easycode.intensive2itunessearch.core.presentation.PlayTrackRepository
import ru.easycode.intensive2itunessearch.dashboard.data.cache.CacheDashboardDataSource
import ru.easycode.intensive2itunessearch.dashboard.data.cloud.CloudDataSource

interface DashboardRepository : PlayTrackRepository {

    suspend fun tracks(query: String): LoadResult

    fun saveUserQuery(value: String)

    fun readUserQuery(): String

    class Base(
        private val cloud: CloudDataSource,
        private val cache: CacheDashboardDataSource,
        currentTrackIdCache: CurrentTrackIdCache,
        currentPlaylistCache: CurrentPlaylistCache,
        private val userQuery: StringCache,
    ) : PlayTrackRepository.Abstract(currentTrackIdCache, currentPlaylistCache),
        DashboardRepository {

        override suspend fun tracks(query: String): LoadResult {
            val data = cache.tracks(query)
            return if (data.isEmpty()) {
                try {
                    val cloudData = cloud.data(query)
                    cache.save(query, cloudData)
                    val tracks = cloudData.map { trackCloud ->
                        Track(
                            trackId = trackCloud.trackId,
                            artistName = trackCloud.artistName,
                            trackName = trackCloud.trackName,
                            trackUrl = trackCloud.previewUrl,
                            coverUrl = trackCloud.artworkUrl
                        )
                    }
                    LoadResult.Success(tracks, query)
                } catch (e: Exception) {
                    LoadResult.Error(e.message.toString())
                }
            } else {
                LoadResult.Success(data.map { trackCache ->
                    Track(
                        trackId = trackCache.trackId,
                        artistName = trackCache.artistName,
                        trackName = trackCache.trackName,
                        trackUrl = trackCache.previewUrl,
                        coverUrl = trackCache.artworkUrl
                    )
                }, query)
            }
        }

        override fun saveUserQuery(value: String) = userQuery.save(value)

        override fun readUserQuery(): String = userQuery.read()
    }
}

interface LoadResult {

    fun isSuccessful(): Boolean
    fun message(): String
    fun list(): List<Track>
    fun query(): String

    data class Success(
        private val list: List<Track>,
        private val query: String,
    ) : LoadResult {

        override fun isSuccessful(): Boolean = true

        override fun message(): String = throw IllegalStateException("Success message cannot exist")

        override fun list(): List<Track> = list

        override fun query(): String = query
    }

    data class Error(private val message: String) : LoadResult {

        override fun isSuccessful(): Boolean = false

        override fun message(): String = message

        override fun list(): List<Track> = throw IllegalStateException("Tracklist cannot exist")

        override fun query(): String = throw IllegalStateException("Query cannot exist")
    }
}