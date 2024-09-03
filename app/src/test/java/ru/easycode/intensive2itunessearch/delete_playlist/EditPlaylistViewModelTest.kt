package ru.easycode.intensive2itunessearch.delete_playlist

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import ru.easycode.intensive2itunessearch.add.presentation.PlaylistUi
import ru.easycode.intensive2itunessearch.core.presentation.RunAsync
import ru.easycode.intensive2itunessearch.core.presentation.UpdateUi
import ru.easycode.intensive2itunessearch.delete_playlist.data.EditPlaylistRepository
import ru.easycode.intensive2itunessearch.delete_playlist.presentation.EditPlaylistUiObservable
import ru.easycode.intensive2itunessearch.delete_playlist.presentation.EditPlaylistUiState
import ru.easycode.intensive2itunessearch.delete_playlist.presentation.EditPlaylistViewModel

class EditPlaylistViewModelTest {

    private lateinit var repository: FakeEditPlaylistRepository
    private lateinit var runAsync: FakeEditRunAsync
    private lateinit var uiObservable: FakeEditPlaylistUiObservable
    private lateinit var viewModel: EditPlaylistViewModel

    @Before
    fun beforeTest() {
        repository = FakeEditPlaylistRepository()
        runAsync = FakeEditRunAsync()
        uiObservable = FakeEditPlaylistUiObservable()

        viewModel = EditPlaylistViewModel(
            repository = repository,
            runAsync = runAsync,
            uiObservable = uiObservable
        )
    }

    @Test
    fun testRenamePlaylist() {

        val playlistUi = PlaylistUi(id = 100L, name = "playlist1")
        viewModel.init(playlist = playlistUi)

        assertEquals(0, uiObservable.uiStateCallList.size)

        viewModel.checkUserInput("playlist1")
        assertEquals(EditPlaylistUiState.SameValue, uiObservable.uiStateCallList[0])

        viewModel.checkUserInput("playlist12")
        assertEquals(EditPlaylistUiState.NotSameValue, uiObservable.uiStateCallList[1])

        viewModel.renamePlaylist("playlist12")
        assertEquals(EditPlaylistUiState.Close, uiObservable.uiStateCallList[2])
        assertEquals(100L, repository.calledArgid)
        assertEquals("playlist12", repository.calledArgName)

    }

    @Test
    fun testDeletePlaylist() {
        val playlistUi = PlaylistUi(id = 100L, name = "playlist1")
        viewModel.init(playlist = playlistUi)

        assertEquals(0, uiObservable.uiStateCallList.size)

        viewModel.deletePlaylist()
        assertEquals(EditPlaylistUiState.Close, uiObservable.uiStateCallList[0])
        assertEquals(100L, repository.deletedPlaylistId)
    }
}

class FakeEditRunAsync : RunAsync {
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

class FakeEditPlaylistUiObservable : EditPlaylistUiObservable {

    val uiStateCallList = mutableListOf<EditPlaylistUiState>()

    override fun updateUiState(uiState: EditPlaylistUiState) {
        uiStateCallList.add(uiState)
    }

    override fun updateObserver(observer: UpdateUi<EditPlaylistUiState>) = Unit
    override fun clearObserver() = Unit
    override fun clear() = Unit
}

class FakeEditPlaylistRepository : EditPlaylistRepository {

    var calledArgid = 0L
    var calledArgName = ""
    var deletedPlaylistId = 0L

    override suspend fun deletePlaylist(playlistId: Long) {
        deletedPlaylistId = playlistId
    }

    override suspend fun renamePlaylist(playlistId: Long, name: String) {
        calledArgid = playlistId
        calledArgName = name
    }
}
