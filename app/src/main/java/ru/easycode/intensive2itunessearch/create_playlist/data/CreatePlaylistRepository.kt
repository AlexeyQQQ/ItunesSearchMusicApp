package ru.easycode.intensive2itunessearch.create_playlist.data

import ru.easycode.intensive2itunessearch.core.data.cache.Now
import ru.easycode.intensive2itunessearch.core.data.cache.PlaylistCache
import ru.easycode.intensive2itunessearch.core.data.cache.PlaylistDao

interface CreatePlaylistRepository {

    suspend fun createPlaylist(playlistName: String)

    class Base(
        private val now: Now,
        private val playlistDao: PlaylistDao,
    ) : CreatePlaylistRepository {

        override suspend fun createPlaylist(playlistName: String) {
            playlistDao.insert(playlist = PlaylistCache(now.timeInMillis(), playlistName))
        }
    }
}



