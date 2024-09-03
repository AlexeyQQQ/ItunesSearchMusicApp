package ru.easycode.intensive2itunessearch.delete_playlist.data

import ru.easycode.intensive2itunessearch.core.data.cache.PlaylistDao
import ru.easycode.intensive2itunessearch.core.data.cache.RelationDao

interface EditPlaylistRepository {

    suspend fun deletePlaylist(playlistId: Long)

    suspend fun renamePlaylist(playlistId: Long, name: String)

    class Base(
        private val playlistDao: PlaylistDao,
        private val relationDao: RelationDao,
    ) : EditPlaylistRepository {

        override suspend fun deletePlaylist(playlistId: Long) {
            playlistDao.deletePlaylist(playlistId = playlistId)
            relationDao.deleteRelationsForPlaylist(playlistId = playlistId)
        }

        override suspend fun renamePlaylist(playlistId: Long, name: String) {
            playlistDao.update(playlistId = playlistId, name = name)
        }
    }
}