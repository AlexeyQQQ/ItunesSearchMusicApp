package ru.easycode.intensive2itunessearch

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import ru.easycode.intensive2itunessearch.add.data.Playlist
import ru.easycode.intensive2itunessearch.add.presentation.PlaylistUi
import ru.easycode.intensive2itunessearch.core.presentation.RunAsync
import ru.easycode.intensive2itunessearch.core.presentation.UpdateUi
import ru.easycode.intensive2itunessearch.playlists.data.PlaylistsRepository
import ru.easycode.intensive2itunessearch.playlists.presentation.PlaylistsUiObservable
import ru.easycode.intensive2itunessearch.playlists.presentation.PlaylistsUiState
import ru.easycode.intensive2itunessearch.playlists.presentation.PlaylistsViewModel

class PlaylistsViewModelTest {

    @Test
    fun testCase() {
        val repository = FakePlaylistsRepository()
        val runAsync = FakePlayListsRunAsync()
        val uiObservable = FakePlaylistsUiObservable()

        val viewModel = PlaylistsViewModel(
            repository = repository,
            runAsync = runAsync,
            uiObservable = uiObservable
        )

        viewModel.init()
        val playlistUi = PlaylistUi(id = 0L, name = "playlist1")
        var expectedState: PlaylistsUiState = PlaylistsUiState.Base(listOf(playlistUi))
        var actualState = uiObservable.uiStateCallList[0]
        assertEquals(expectedState, actualState)

        viewModel.clickPlaylist(playlistUi)
        expectedState = PlaylistsUiState.NavigateToPlaylistDetails(playlistUi)
        actualState = uiObservable.uiStateCallList[1]
        assertEquals(expectedState, actualState)
    }
}

private class FakePlaylistsUiObservable : PlaylistsUiObservable {

    val uiStateCallList = mutableListOf<PlaylistsUiState>()

    override fun updateUiState(uiState: PlaylistsUiState) {
        uiStateCallList.add(uiState)
    }

    override fun updateObserver(observer: UpdateUi<PlaylistsUiState>) = Unit
    override fun clearObserver() = Unit
    override fun clear() = Unit
}

private class FakePlaylistsRepository : PlaylistsRepository {

    private val playlists: MutableList<Playlist> = mutableListOf(
        Playlist(id = 0L, name = "playlist1")
    )

    override suspend fun playlists(): List<Playlist> {
        return playlists
    }
}

private class FakePlayListsRunAsync : RunAsync {

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