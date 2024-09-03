package ru.easycode.intensive2itunessearch.core.data.cache

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlists_table")
data class PlaylistCache(

    @PrimaryKey
    @ColumnInfo("id")
    val id: Long,
    @ColumnInfo("name")
    val name: String,
)