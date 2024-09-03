package ru.easycode.intensive2itunessearch.core.data.cache

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PlaylistDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(playlist: PlaylistCache)

    @Query("SELECT * FROM playlists_table")
    suspend fun playlists(): List<PlaylistCache>

    @Query("DELETE FROM playlists_table WHERE id = :playlistId")
    suspend fun deletePlaylist(playlistId: Long)

    @Query("UPDATE playlists_table SET name = :name WHERE id = :playlistId")
    suspend fun update(playlistId: Long, name: String)
}