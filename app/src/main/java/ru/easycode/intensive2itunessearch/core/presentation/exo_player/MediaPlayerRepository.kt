package ru.easycode.intensive2itunessearch.core.presentation.exo_player

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.easycode.intensive2itunessearch.core.data.cache.CurrentPlaylistCache
import ru.easycode.intensive2itunessearch.core.data.cache.CurrentTrackIdCache
import ru.easycode.intensive2itunessearch.core.data.cache.RelationDao
import ru.easycode.intensive2itunessearch.core.data.cache.TrackCache
import ru.easycode.intensive2itunessearch.core.data.cache.TracksDao
import ru.easycode.intensive2itunessearch.dashboard.presentation.adapter.DashboardUi

interface MediaPlayerRepository {

    fun cachedPlaylistId(): String

    fun cachedPlaylist(): List<TrackCache>

    fun saveCurrentTrackId(currentId: Long)

    suspend fun updatePlaylistInController(controllerWrapper: IsPlaying): List<DashboardUi>

    class Base(
        private val currentTrackIdCache: CurrentTrackIdCache,
        private val currentPlaylistCache: CurrentPlaylistCache,
        private val tracksDao: TracksDao,
        private val relationDao: RelationDao,
    ) : MediaPlayerRepository {

        private var cachedPlaylistId: String = ""
        private var cachedPlaylist: List<TrackCache> = emptyList()

        override fun cachedPlaylistId(): String = cachedPlaylistId

        override fun cachedPlaylist(): List<TrackCache> = cachedPlaylist

        override fun saveCurrentTrackId(currentId: Long) {
            currentTrackIdCache.save(currentId)
        }

        override suspend fun updatePlaylistInController(
            controllerWrapper: IsPlaying
        ): List<DashboardUi> {
            cachedPlaylist = withContext(Dispatchers.IO) {
                if (currentPlaylistCache.currentPlaylistSaved()) {
                    if (currentPlaylistCache.isPlaylistFromQuery()) {
                        cachedPlaylistId = currentPlaylistCache.currentPlaylistQuery()
                        tracksDao.tracks(cachedPlaylistId)
                    } else {
                        val playlistId = currentPlaylistCache.currentPlaylistId()
                        cachedPlaylistId = playlistId.toString()
                        relationDao.playlistTracks(playlistId)
                    }
                } else {
                    emptyList()
                }
            }

            return cachedPlaylist.map {
                DashboardUi.Track(
                    playlistId = cachedPlaylistId,
                    trackId = it.trackId,
                    trackUrl = it.previewUrl,
                    coverUrl = it.artworkUrl,
                    trackName = it.trackName,
                    artistName = it.artistName,
                    playing = currentTrackIdCache.read() == it.trackId && controllerWrapper.isPlaying()
                )
            }
        }
    }
}