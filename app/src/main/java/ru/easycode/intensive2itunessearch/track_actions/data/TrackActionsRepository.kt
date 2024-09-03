package ru.easycode.intensive2itunessearch.track_actions.data

import ru.easycode.intensive2itunessearch.add.data.Playlist
import ru.easycode.intensive2itunessearch.core.data.cache.Now
import ru.easycode.intensive2itunessearch.core.data.cache.PlaylistCache
import ru.easycode.intensive2itunessearch.core.data.cache.PlaylistDao
import ru.easycode.intensive2itunessearch.core.data.cache.RelationCache
import ru.easycode.intensive2itunessearch.core.data.cache.RelationDao
import ru.easycode.intensive2itunessearch.dashboard.data.Track

interface TrackActionsRepository {

    suspend fun savePlaylist(name: String)
    suspend fun playlists(track: Track): List<Playlist>
    suspend fun addTrackToPlaylist(playlist: Playlist, track: Track)
    suspend fun removeTrackFromPlaylist(track: Track, playlist: Playlist)

    class Base(
        private val now: Now,
        private val playlistDao: PlaylistDao,
        private val relationDao: RelationDao,
    ) : TrackActionsRepository {

        override suspend fun savePlaylist(name: String) {
            playlistDao.insert(playlist = PlaylistCache(id = now.timeInMillis(), name))
        }

        override suspend fun playlists(track: Track): List<Playlist> {
            return relationDao.getPlaylistsWithoutTrack(track.trackId).map {
                Playlist(it.id, it.name)
            }
        }

        override suspend fun addTrackToPlaylist(playlist: Playlist, track: Track) {
            relationDao.insert(
                relationCache = RelationCache(
                    playlistId = playlist.id,
                    trackId = track.trackId,
                )
            )
        }

        override suspend fun removeTrackFromPlaylist(track: Track, playlist: Playlist) {
            relationDao.removeTrackFromPlaylist(
                trackId = track.trackId, playlistId = playlist.id
            )
        }
    }
}