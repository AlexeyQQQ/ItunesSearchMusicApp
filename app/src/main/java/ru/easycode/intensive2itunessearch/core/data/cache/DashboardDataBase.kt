package ru.easycode.intensive2itunessearch.core.data.cache

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [
        TrackCache::class,
        PlaylistCache::class,
        RelationCache::class,
    ],
    version = 1,
    exportSchema = false
)
abstract class DashboardDataBase : RoomDatabase() {

    abstract fun trackDao(): TracksDao
    abstract fun playlistDao(): PlaylistDao
    abstract fun relationDao(): RelationDao
}