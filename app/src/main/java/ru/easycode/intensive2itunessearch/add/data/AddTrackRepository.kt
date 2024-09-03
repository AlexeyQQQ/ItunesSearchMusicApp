package ru.easycode.intensive2itunessearch.add.data

import ru.easycode.intensive2itunessearch.core.data.cache.Now
import ru.easycode.intensive2itunessearch.core.data.cache.PlaylistCache
import ru.easycode.intensive2itunessearch.core.data.cache.PlaylistDao
import ru.easycode.intensive2itunessearch.core.data.cache.RelationCache
import ru.easycode.intensive2itunessearch.core.data.cache.RelationDao
import ru.easycode.intensive2itunessearch.dashboard.data.Track

interface AddTrackRepository {

    suspend fun savePlaylist(name: String)

    suspend fun playlists(track: Track): List<Playlist>

    suspend fun addTrackToPlaylist(playlist: Playlist, track: Track)

    class Base(
        private val now: Now,
        private val playlistDao: PlaylistDao,
        private val relationDao: RelationDao,
    ) : AddTrackRepository {

        override suspend fun savePlaylist(name: String) {
            playlistDao.insert(playlist = PlaylistCache(now.timeInMillis(), name))
        }

        override suspend fun playlists(track: Track): List<Playlist> {
            return relationDao.getPlaylistsWithoutTrack(trackId = track.trackId).map {
                Playlist(id = it.id, name = it.name)
            }
        }

        override suspend fun addTrackToPlaylist(playlist: Playlist, track: Track) {
            relationDao.insert(
                RelationCache(
                    playlistId = playlist.id,
                    trackId = track.trackId,
                )
            )
        }
    }
}