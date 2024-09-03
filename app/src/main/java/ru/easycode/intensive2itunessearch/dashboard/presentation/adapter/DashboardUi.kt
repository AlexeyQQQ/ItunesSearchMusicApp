package ru.easycode.intensive2itunessearch.dashboard.presentation.adapter

import android.net.Uri
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import ru.easycode.intensive2itunessearch.R
import ru.easycode.intensive2itunessearch.core.presentation.ClickActions
import ru.easycode.intensive2itunessearch.databinding.ItemErrorBinding
import ru.easycode.intensive2itunessearch.databinding.ItemTrackBinding
import java.io.Serializable

interface DashboardUi : Serializable {

    fun type(): DashboardUiType

    fun isPlaying() = false

    fun changePlaying(): DashboardUi = this
    fun play(): DashboardUi = this
    fun stop(): DashboardUi = this

    fun trackUrl(): String = throw RuntimeException("trackUrl cannot exist")

    fun show(binding: ItemErrorBinding) = Unit
    fun show(binding: ItemTrackBinding) = Unit

    fun playOrStop(clickActions: ClickActions) = Unit
    fun id(): String
    fun coverUrl(): String = ""
    fun trackName(): String = ""
    fun artistName(): String = ""
    fun playlistId(): String = ""

    object Progress : DashboardUi {

        override fun id(): String = javaClass.simpleName

        override fun type() = DashboardUiType.Progress
    }

    data class Error(private val message: String) : DashboardUi {

        override fun id(): String = javaClass.simpleName + message

        override fun type() = DashboardUiType.Error

        override fun show(binding: ItemErrorBinding) {
            binding.errorTextView.text = message
        }
    }

    data class Track(
        private val playlistId: String,
        private val trackId: Long,
        private val trackUrl: String,
        private val coverUrl: String,
        private val trackName: String,
        private val artistName: String,
        private val playing: Boolean,
    ) : DashboardUi {

        override fun id(): String = trackId.toString()

        override fun show(binding: ItemTrackBinding) = with(binding) {
            trackNameTextView.text = trackName
            artistNameTextView.text = artistName
            playImageButton.setImageResource(if (playing) R.drawable.ic_stop else R.drawable.ic_play_track)
            coverImageView.updateImageUrl(coverUrl)
        }

        override fun playOrStop(clickActions: ClickActions) = with(clickActions) {
            if (playing)
                stop()
            else
                play(this@Track)
        }

        override fun type() = DashboardUiType.Track

        override fun isPlaying(): Boolean {
            return playing
        }

        override fun changePlaying(): DashboardUi {
            return this.copy(playing = !playing)
        }

        override fun play(): DashboardUi {
            return this.copy(playing = true)
        }

        override fun stop(): DashboardUi {
            return this.copy(playing = false)
        }

        override fun trackUrl(): String = trackUrl
        override fun coverUrl(): String = coverUrl
        override fun trackName(): String = trackName
        override fun artistName(): String = artistName
        override fun playlistId(): String = playlistId

        fun mapToMediaItem(): MediaItem {
            val metadata =
                MediaMetadata.Builder()
                    .setTitle(trackName)
                    .setArtist(artistName)
                    .setArtworkUri(Uri.parse(coverUrl))
                    .build()
            return MediaItem.Builder()
                .setMediaId(trackId.toString())
                .setUri(Uri.parse(trackUrl))
                .setMediaMetadata(metadata)
                .build()
        }
    }
}