package ru.easycode.intensive2itunessearch.core.data.cache

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface TracksDao {

    @Query("SELECT * from tracks_table where term = :term order by customOrder")
    suspend fun tracks(term: String): List<TrackCache>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(list: List<TrackCache>)
}