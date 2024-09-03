package ru.easycode.intensive2itunessearch.playlist_details.data

import ru.easycode.intensive2itunessearch.core.data.cache.CurrentPlaylistCache
import ru.easycode.intensive2itunessearch.core.data.cache.CurrentTrackIdCache
import ru.easycode.intensive2itunessearch.core.data.cache.RelationDao
import ru.easycode.intensive2itunessearch.core.presentation.PlayTrackRepository
import ru.easycode.intensive2itunessearch.dashboard.data.Track

interface PlaylistDetailsRepository : PlayTrackRepository {

    suspend fun tracks(playlistId: Long): List<Track>

    class Base(
        private val relationDao: RelationDao,
        currentTrackIdCache: CurrentTrackIdCache,
        currentPlaylistCache: CurrentPlaylistCache,
    ) : PlayTrackRepository.Abstract(currentTrackIdCache, currentPlaylistCache),
        PlaylistDetailsRepository {

        override suspend fun tracks(playlistId: Long): List<Track> {
            val cloudData = relationDao.playlistTracks(playlistId = playlistId)

            val tracks = cloudData.map { trackCache ->
                Track(
                    trackId = trackCache.trackId,
                    artistName = trackCache.artistName,
                    trackName = trackCache.trackName,
                    trackUrl = trackCache.previewUrl,
                    coverUrl = trackCache.artworkUrl
                )
            }
            return tracks
        }
    }
}
