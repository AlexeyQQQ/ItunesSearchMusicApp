package ru.easycode.intensive2itunessearch.core.data.cache

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface RelationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(relationCache: RelationCache)

    @Query(
        """
        SELECT playlists_table.* FROM playlists_table
        LEFT JOIN relation_table
        ON playlists_table.id = relation_table.playlistId AND relation_table.trackId = :trackId
        WHERE relation_table.trackId IS NULL"""
    )
    suspend fun getPlaylistsWithoutTrack(trackId: Long): List<PlaylistCache>

    @Query(
        """
        SELECT tracks_table.* FROM tracks_table
        INNER JOIN relation_table
        ON tracks_table.trackId = relation_table.trackId
        WHERE relation_table.playlistId = :playlistId
    """
    )
    suspend fun playlistTracks(playlistId: Long): List<TrackCache>

    @Query("DELETE FROM relation_table WHERE playlistId = :playlistId")
    suspend fun deleteRelationsForPlaylist(playlistId: Long)

    @Query("DELETE FROM relation_table WHERE trackId = :trackId AND playlistId = :playlistId")
    suspend fun removeTrackFromPlaylist(trackId: Long, playlistId: Long)
}