package ru.easycode.intensive2itunessearch.create_playlist

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import ru.easycode.intensive2itunessearch.core.presentation.RunAsync
import ru.easycode.intensive2itunessearch.core.presentation.UpdateUi
import ru.easycode.intensive2itunessearch.create_playlist.data.CreatePlaylistRepository
import ru.easycode.intensive2itunessearch.create_playlist.presentation.CreatePlaylistUiObservable
import ru.easycode.intensive2itunessearch.create_playlist.presentation.CreatePlaylistUiState
import ru.easycode.intensive2itunessearch.create_playlist.presentation.CreatePlaylistViewModel

class CreatePlaylistViewModelTest {

    @Test
    fun testCreatePlaylist() {
        val repository = FakePlayListRepository()
        val runAsync = FakeRunAsync()
        val uiObservable = FakeUiObservable()

        val viewModel = CreatePlaylistViewModel(
            repository = repository,
            runAsync = runAsync,
            uiObservable = uiObservable
        )

        val playlistName = "New Playlist"

        // 1
        viewModel.checkUserInput(text = "a")
        assertEquals(CreatePlaylistUiState.NotEmptyInput, uiObservable.uiStateCallList[0])
        // 2
        viewModel.checkUserInput(text = "")
        assertEquals(CreatePlaylistUiState.EmptyInput, uiObservable.uiStateCallList[1])
        // 3
        viewModel.checkUserInput(text = "a")
        assertEquals(CreatePlaylistUiState.NotEmptyInput, uiObservable.uiStateCallList[2])
        viewModel.savePlaylist(playlistName)
        assertEquals(playlistName, repository.playlistName)
        // 4
        assertEquals(CreatePlaylistUiState.Close, uiObservable.uiStateCallList[3])
    }
}

class FakeUiObservable : CreatePlaylistUiObservable {

    val uiStateCallList = mutableListOf<CreatePlaylistUiState>()
    override fun updateObserver(observer: UpdateUi<CreatePlaylistUiState>) = Unit

    override fun clearObserver() = Unit

    override fun clear() = Unit

    override fun updateUiState(uiState: CreatePlaylistUiState) {
        uiStateCallList.add(uiState)
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

private class FakePlayListRepository : CreatePlaylistRepository {

    var playlistName: String? = null

    override suspend fun createPlaylist(playlistName: String) {
        this.playlistName = playlistName
    }
}
