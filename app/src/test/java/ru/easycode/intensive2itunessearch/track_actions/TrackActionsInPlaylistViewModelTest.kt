package ru.easycode.intensive2itunessearch.track_actions

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test
import ru.easycode.intensive2itunessearch.add.data.Playlist
import ru.easycode.intensive2itunessearch.add.presentation.AddTrackToPlaylistUiState
import ru.easycode.intensive2itunessearch.add.presentation.AddTrackUiObservable
import ru.easycode.intensive2itunessearch.add.presentation.PlaylistUi
import ru.easycode.intensive2itunessearch.core.presentation.RunAsync
import ru.easycode.intensive2itunessearch.core.presentation.UpdateUi
import ru.easycode.intensive2itunessearch.dashboard.data.Track
import ru.easycode.intensive2itunessearch.dashboard.presentation.adapter.DashboardUi
import ru.easycode.intensive2itunessearch.track_actions.data.TrackActionsRepository
import ru.easycode.intensive2itunessearch.track_actions.presentation.TrackActionsInPlaylistViewModel


class TrackActionsInPlaylistViewModelTest {

    @Test
    fun testCase() {
        val repository = FakeTrackActionsInPlaylistRepository()
        val runAsync = FakeRunAsync()
        val uiObservable = FakeUiObservable()

        val viewModel = TrackActionsInPlaylistViewModel(
            uiObservable = uiObservable,
            repository = repository,
            runAsync = runAsync,
        )

        val track = DashboardUi.Track(
            playlistId = "100L",
            trackId = 100L,
            trackUrl = "trackUrl1",
            coverUrl = "coverUrl1",
            trackName = "testTitle1",
            artistName = "testSubTitle1",
            playing = false,
        )

        val playlist = PlaylistUi(id = 100L, name = "add")

        viewModel.init(track = track, playlist = playlist)
        Assert.assertEquals(
            AddTrackToPlaylistUiState.Initial(list = listOf<PlaylistUi>()),
            uiObservable.uiStateCallList[0]
        )

        viewModel.removeTrack()
        Assert.assertEquals(
            AddTrackToPlaylistUiState.Close,
            uiObservable.uiStateCallList[1]
        )

        Assert.assertEquals(
            Track(
                trackId = 100L,
                trackUrl = "trackUrl1",
                coverUrl = "coverUrl1",
                trackName = "testTitle1",
                artistName = "testSubTitle1",
            ),
            repository.trackCalledArg

        )
        Assert.assertEquals(
            Playlist(id = 100L, name = "add"),
            repository.playlistCalledArg
        )
    }
}

private class FakeTrackActionsInPlaylistRepository : TrackActionsRepository {

    var playlistCalledArg: Playlist? = null
    var trackCalledArg: Track? = null

    override suspend fun savePlaylist(name: String) = Unit

    override suspend fun playlists(track: Track): List<Playlist> = emptyList()

    override suspend fun addTrackToPlaylist(playlist: Playlist, track: Track) = Unit

    override suspend fun removeTrackFromPlaylist(track: Track, playlist: Playlist) {
        playlistCalledArg = playlist
        trackCalledArg = track
    }
}

private class FakeUiObservable : AddTrackUiObservable {

    val uiStateCallList = mutableListOf<AddTrackToPlaylistUiState>()

    override fun updateUiState(uiState: AddTrackToPlaylistUiState) {
        uiStateCallList.add(uiState)
    }

    override fun updateObserver(observer: UpdateUi<AddTrackToPlaylistUiState>) = Unit

    override fun clearObserver() = Unit

    override fun clear() = Unit
}

private class FakeRunAsync : RunAsync {

    override fun <T : Any> runAsync(
        coroutineScope: CoroutineScope,
        background: suspend () -> T,
        ui: (T) -> Unit
    ) = runBlocking {
        val result = background.invoke()
        ui.invoke(result)
    }

    override fun launchUi(
        coroutineScope: CoroutineScope,
        function: suspend () -> Unit,
    ) = runBlocking {
        function.invoke()
    }
}