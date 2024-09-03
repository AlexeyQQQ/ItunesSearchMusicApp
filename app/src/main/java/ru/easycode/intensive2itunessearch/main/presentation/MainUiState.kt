package ru.easycode.intensive2itunessearch.main.presentation

import android.view.View
import ru.easycode.intensive2itunessearch.R
import ru.easycode.intensive2itunessearch.core.presentation.views.UpdateBottomNav
import ru.easycode.intensive2itunessearch.core.presentation.views.UpdateImageButton
import ru.easycode.intensive2itunessearch.core.presentation.views.UpdateImageUrl
import ru.easycode.intensive2itunessearch.core.presentation.views.UpdateMotionLayout
import ru.easycode.intensive2itunessearch.core.presentation.views.UpdateTextView

interface MainUiState {

    fun update(
        playImageButton: UpdateImageButton,
        trackNameTextView: UpdateTextView,
        artistNameTextView: UpdateTextView,
        coverImageView: UpdateImageUrl,
        playerLayout: UpdateMotionLayout,
    ) = Unit

    fun update(playImageButton: UpdateImageButton) = Unit

    fun update(playerLayout: UpdateMotionLayout) = Unit

    fun show(bottomNav: UpdateBottomNav) = Unit

    object ShowBottomNav : MainUiState {
        override fun show(bottomNav: UpdateBottomNav) {
            bottomNav.changeVisibility(View.VISIBLE)
        }
    }

    object HideBottomNav : MainUiState {
        override fun show(bottomNav: UpdateBottomNav) {
            bottomNav.changeVisibility(View.GONE)
        }
    }

    data class ShowPlayer(
        private val trackName: String,
        private val artistName: String,
        private val coverUrl: String,
        private val isPlaying: Boolean,
    ) : MainUiState {

        override fun update(
            playImageButton: UpdateImageButton,
            trackNameTextView: UpdateTextView,
            artistNameTextView: UpdateTextView,
            coverImageView: UpdateImageUrl,
            playerLayout: UpdateMotionLayout,
        ) {
            coverImageView.updateImageUrl(coverUrl)
            playImageButton.changeImageResource(
                if (isPlaying) R.drawable.ic_stop else R.drawable.ic_play_track
            )
            trackNameTextView.changeText(trackName)
            artistNameTextView.changeText(artistName)
            playerLayout.changeVisibility(View.VISIBLE)
        }
    }

    object HidePlayer : MainUiState {
        override fun update(playerLayout: UpdateMotionLayout) {
            playerLayout.changeVisibility(View.GONE)
        }
    }
}