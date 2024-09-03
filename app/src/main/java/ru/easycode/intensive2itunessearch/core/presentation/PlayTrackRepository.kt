package ru.easycode.intensive2itunessearch.core.presentation

import ru.easycode.intensive2itunessearch.core.data.cache.CurrentPlaylistCache
import ru.easycode.intensive2itunessearch.core.data.cache.CurrentTrackIdCache

interface PlayTrackRepository {

    fun readCurrentlyPlayingId(): Long

    fun saveCurrentlyPlayingId(id: Long)

    fun saveCurrentPlayingPlaylist(id: String)

    abstract class Abstract(
        private val currentTrackIdCache: CurrentTrackIdCache,
        private val currentPlaylistCache: CurrentPlaylistCache,
    ) : PlayTrackRepository {

        override fun readCurrentlyPlayingId(): Long {
            return currentTrackIdCache.read()
        }

        override fun saveCurrentlyPlayingId(id: Long) {
            currentTrackIdCache.save(id)
        }

        override fun saveCurrentPlayingPlaylist(id: String) {
            currentPlaylistCache.saveCurrentPlaylistId(id)
        }
    }
}