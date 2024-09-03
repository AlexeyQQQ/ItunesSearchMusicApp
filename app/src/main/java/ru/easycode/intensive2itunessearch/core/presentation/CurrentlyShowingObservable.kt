package ru.easycode.intensive2itunessearch.core.presentation

import ru.easycode.intensive2itunessearch.dashboard.presentation.adapter.DashboardUi

/**
 *  Когда отображается фрагмент на onResume устанавливаем слушатель в контроллер
 *  Когда onPause очищаем
 */
interface CurrentlyShowingObservable {

    fun notifyIsNowPlayingTrackId(trackId: Long)

    fun notifyStopPlaying()

    object Empty : CurrentlyShowingObservable {
        override fun notifyIsNowPlayingTrackId(trackId: Long) = Unit
        override fun notifyStopPlaying() = Unit
    }
}


/**
 *  Нужен для плейлиста в моушн лайауте активити
 */
interface CurrentlyPlayingPlaylistObservable : CurrentlyShowingObservable {

    fun updateList(list: List<DashboardUi>)

    object Empty : CurrentlyPlayingPlaylistObservable {
        override fun updateList(list: List<DashboardUi>) = Unit
        override fun notifyIsNowPlayingTrackId(trackId: Long) = Unit
        override fun notifyStopPlaying() = Unit
    }
}