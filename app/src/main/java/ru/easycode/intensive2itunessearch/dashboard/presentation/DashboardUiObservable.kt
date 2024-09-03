package ru.easycode.intensive2itunessearch.dashboard.presentation

import ru.easycode.intensive2itunessearch.core.presentation.UiObservable
import ru.easycode.intensive2itunessearch.dashboard.presentation.adapter.DashboardUi

interface DashboardUiObservable : UiObservable<List<DashboardUi>> {

    fun currentlyPlayingId(): String
    fun nextTrack(): DashboardUi
    fun play(track: DashboardUi)
    fun currentTrackIndex(): Int
    fun stop()

    class Base : UiObservable.Abstract<List<DashboardUi>>(), DashboardUiObservable {

        private var currentIndex: Int = 0

        override fun nextTrack(): DashboardUi {
            super.cache?.let { cacheList ->
                currentIndex += 1
                return cacheList[currentIndex]
            }
            throw RuntimeException("Tracklist cannot exist")
        }

        private var currentlyPLayingId: String = ""

        override fun currentlyPlayingId(): String {
            return currentlyPLayingId
        }

        override fun play(track: DashboardUi) {
            currentlyPLayingId = track.id()
            super.cache?.let { cacheList ->
                val copyList = cacheList.toMutableList()

                cacheList.find {
                    it.isPlaying()
                }?.let {
                    val currentlyPlayingIndex = cacheList.indexOf(it)
                    val notPlaying = cacheList[currentlyPlayingIndex].stop()
                    copyList[currentlyPlayingIndex] = notPlaying
                }

                currentIndex = cacheList.indexOf(
                    cacheList.find { it.id() == track.id() }
                )
                copyList[currentIndex] = track.play()
                updateUiState(copyList)
            }
        }

        override fun stop() {
            currentlyPLayingId = ""
            super.cache?.let { cacheList ->
                val copyList = cacheList.toMutableList()

                cacheList.find {
                    it.isPlaying()
                }?.let {
                    val currentlyPlayingIndex = cacheList.indexOf(it)
                    val notPlaying = cacheList[currentlyPlayingIndex].stop()
                    copyList[currentlyPlayingIndex] = notPlaying
                }

                super.cache = copyList
                updateUiState(copyList)
            }
        }

        override fun currentTrackIndex(): Int = currentIndex
    }
}