package ru.easycode.intensive2itunessearch.core.data.cache

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(
    tableName = "relation_table",
    primaryKeys = ["trackId", "playlistId"]
)
data class RelationCache(

    @ColumnInfo("id")
    val id: Long = 0,
    @ColumnInfo("trackId")
    val trackId: Long,
    @ColumnInfo("playlistId")
    val playlistId: Long
)