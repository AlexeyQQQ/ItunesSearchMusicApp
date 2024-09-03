package ru.easycode.intensive2itunessearch.core.data.cache

import android.net.Uri
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tracks_table")
data class TrackCache(

    @ColumnInfo("term")
    val term: String,
    @PrimaryKey
    @ColumnInfo("trackId")
    val trackId: Long,
    @ColumnInfo("artistName")
    val artistName: String,
    @ColumnInfo("trackName")
    val trackName: String,
    @ColumnInfo("previewUrl")
    val previewUrl: String,
    @ColumnInfo("artworkUrl100")
    val artworkUrl: String,
    @ColumnInfo("customOrder")
    val customOrder: Long = 0,
) {

    fun mapToMediaItem(): MediaItem {
        val metadata =
            MediaMetadata.Builder()
                .setTitle(trackName)
                .setArtist(artistName)
                .setArtworkUri(Uri.parse(artworkUrl))
                .build()
        return MediaItem.Builder()
            .setMediaId(trackId.toString())
            .setUri(Uri.parse(previewUrl))
            .setMediaMetadata(metadata)
            .build()
    }
}
