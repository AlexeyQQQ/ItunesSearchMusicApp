package ru.easycode.intensive2itunessearch.playlists.data

import ru.easycode.intensive2itunessearch.add.data.Playlist
import ru.easycode.intensive2itunessearch.core.data.cache.PlaylistDao

interface PlaylistsRepository {

    suspend fun playlists(): List<Playlist>

    class Base(
        private val playlistDao: PlaylistDao
    ) : PlaylistsRepository {

        override suspend fun playlists(): List<Playlist> {
            return playlistDao.playlists().map { playlistCache ->
                Playlist(playlistCache.id, playlistCache.name)
            }
        }
    }
}