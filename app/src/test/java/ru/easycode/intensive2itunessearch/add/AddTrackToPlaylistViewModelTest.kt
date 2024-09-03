package ru.easycode.intensive2itunessearch.add

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import ru.easycode.intensive2itunessearch.add.data.AddTrackRepository
import ru.easycode.intensive2itunessearch.add.data.Playlist
import ru.easycode.intensive2itunessearch.add.presentation.AddTrackToPlaylistUiState
import ru.easycode.intensive2itunessearch.add.presentation.AddTrackToPlaylistViewModel
import ru.easycode.intensive2itunessearch.add.presentation.AddTrackUiObservable
import ru.easycode.intensive2itunessearch.add.presentation.PlaylistUi
import ru.easycode.intensive2itunessearch.core.presentation.RunAsync
import ru.easycode.intensive2itunessearch.core.presentation.UpdateUi
import ru.easycode.intensive2itunessearch.dashboard.data.Track
import ru.easycode.intensive2itunessearch.dashboard.presentation.adapter.DashboardUi

class AddTrackToPlaylistViewModelTest {

    @Test
    fun testCase() {
        val repository = FakeAddTrackRepository()
        val runAsync = FakeRunAsync()
        val uiObservable = FakeUiObservable()

        val viewModel = AddTrackToPlaylistViewModel(
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

        viewModel.init(track = track)
        assertEquals(
            AddTrackToPlaylistUiState.Initial(list = listOf<PlaylistUi>()),
            uiObservable.uiStateCallList[0]
        )
        assertEquals(
            Track(
                trackId = 100L,
                trackUrl = "trackUrl1",
                coverUrl = "coverUrl1",
                trackName = "testTitle1",
                artistName = "testSubTitle1",
            ),
            repository.playlistsTrackCalledArg
        )

        viewModel.createPlaylist()
        assertEquals(AddTrackToPlaylistUiState.EmptyInput, uiObservable.uiStateCallList[1])

        viewModel.checkUserInput(text = "a")
        assertEquals(AddTrackToPlaylistUiState.NotEmptyInput, uiObservable.uiStateCallList[2])

        viewModel.cancel()
        assertEquals(
            AddTrackToPlaylistUiState.ListState,
            uiObservable.uiStateCallList[3]
        )

        viewModel.createPlaylist()
        assertEquals(AddTrackToPlaylistUiState.EmptyInput, uiObservable.uiStateCallList[4])

        viewModel.checkUserInput(text = "a")
        assertEquals(AddTrackToPlaylistUiState.NotEmptyInput, uiObservable.uiStateCallList[5])
        viewModel.checkUserInput(text = "ad")
        assertEquals(AddTrackToPlaylistUiState.NotEmptyInput, uiObservable.uiStateCallList[6])
        viewModel.checkUserInput(text = "add")
        assertEquals(AddTrackToPlaylistUiState.NotEmptyInput, uiObservable.uiStateCallList[7])

        val playlist = PlaylistUi(id = 100L, name = "add")
        viewModel.savePlaylist(name = "add")
        assertEquals(
            AddTrackToPlaylistUiState.Initial(list = listOf(playlist)),
            uiObservable.uiStateCallList[8]
        )
        assertEquals("add", repository.playlistNameCalledArg)

        viewModel.clickPlaylist(playlist = playlist)
        assertEquals(
            AddTrackToPlaylistUiState.Close,
            uiObservable.uiStateCallList[9]
        )
        assertEquals(
            Playlist(id = 100L, name = "add"),
            repository.playlistCalledArg
        )
        assertEquals(
            Track(
                trackId = 100L,
                trackUrl = "trackUrl1",
                coverUrl = "coverUrl1",
                trackName = "testTitle1",
                artistName = "testSubTitle1",
            ),
            repository.trackCalledArg
        )
    }
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

private class FakeUiObservable : AddTrackUiObservable {

    val uiStateCallList = mutableListOf<AddTrackToPlaylistUiState>()

    override fun updateUiState(uiState: AddTrackToPlaylistUiState) {
        uiStateCallList.add(uiState)
    }

    override fun updateObserver(observer: UpdateUi<AddTrackToPlaylistUiState>) = Unit

    override fun clearObserver() = Unit

    override fun clear() = Unit
}

private class FakeAddTrackRepository : AddTrackRepository {

    private val playlists = mutableListOf<Playlist>()

    var playlistNameCalledArg: String = ""
    var playlistCalledArg: Playlist? = null
    var trackCalledArg: Track? = null
    var playlistsTrackCalledArg: Track? = null

    override suspend fun savePlaylist(name: String) {
        playlists.add(Playlist(id = 100L, name = name))
        playlistNameCalledArg = name
    }

    override suspend fun playlists(track: Track): List<Playlist> {
        playlistsTrackCalledArg = track
        return playlists
    }

    override suspend fun addTrackToPlaylist(playlist: Playlist, track: Track) {
        playlistCalledArg = playlist
        trackCalledArg = track
    }
}