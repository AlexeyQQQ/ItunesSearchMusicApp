package ru.easycode.intensive2itunessearch.playlist_details

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import ru.easycode.intensive2itunessearch.core.presentation.CurrentlyPlayingPlaylistObservable
import ru.easycode.intensive2itunessearch.core.presentation.CurrentlyShowingObservable
import ru.easycode.intensive2itunessearch.core.presentation.RunAsync
import ru.easycode.intensive2itunessearch.core.presentation.UpdateUi
import ru.easycode.intensive2itunessearch.core.presentation.exo_player.MediaPlayer
import ru.easycode.intensive2itunessearch.dashboard.data.Track
import ru.easycode.intensive2itunessearch.dashboard.presentation.DashboardUiObservable
import ru.easycode.intensive2itunessearch.dashboard.presentation.adapter.DashboardUi
import ru.easycode.intensive2itunessearch.main.presentation.MainObservable
import ru.easycode.intensive2itunessearch.main.presentation.MainUiState
import ru.easycode.intensive2itunessearch.playlist_details.data.PlaylistDetailsRepository
import ru.easycode.intensive2itunessearch.playlist_details.presentation.PlaylistDetailsViewModel

class PlaylistDetailsViewModelTest {

    @Test
    fun testCase() {
        val mediaPlayer = FakeMediaPlayer()
        val repository = FakePlaylistDetailsRepository()
        val mainObservable = FakeMainObservable()
        val uiObservable = FakeUiObservable()
        val runAsync = FakeRunAsync()

        val viewModel = PlaylistDetailsViewModel(
            mediaPlayer = mediaPlayer,
            repository = repository,
            mainObservable = mainObservable,
            uiObservable = uiObservable,
            runAsync = runAsync,
        )

        val track1 = DashboardUi.Track(
            playlistId = "100",
            trackId = 100L,
            trackUrl = "trackUrl1",
            coverUrl = "coverUrl1",
            trackName = "testTitle1",
            artistName = "testSubTitle1",
            playing = false
        )
        val track2 = DashboardUi.Track(
            playlistId = "100",
            trackId = 200L,
            trackUrl = "trackUrl2",
            coverUrl = "coverUrl2",
            trackName = "testTitle2",
            artistName = "testSubTitle2",
            playing = false
        )

        // step 1 init
        viewModel.init(playlistId = 100L)
        assertEquals(100L, repository.playlistIdCalledArg)
        assertEquals(listOf(track1, track2), uiObservable.updateUiCallList[0])
        assertEquals(MainUiState.HideBottomNav, mainObservable.updateUiCallList[0])

        // step 2 exit
        viewModel.showBottomNav()
        assertEquals(MainUiState.ShowBottomNav, mainObservable.updateUiCallList[1])
    }
}

private class FakeMediaPlayer : MediaPlayer {

    var playing: Boolean = false
    var cachedTrack: DashboardUi? = null
    var showingObservable: CurrentlyShowingObservable = CurrentlyShowingObservable.Empty

    override suspend fun play(track: DashboardUi) {
        cachedTrack = track
        playing = true
    }

    override fun stop() {
        playing = false
    }

    override fun isPlaying(): Boolean = playing

    override fun updateCurrentlyShowingObservable(
        currentlyShowingObservable: CurrentlyShowingObservable
    ) {
        showingObservable = currentlyShowingObservable
    }

    override fun updateCurrentlyPlayingObservable(
        currentlyPlayingPlaylistObservable: CurrentlyPlayingPlaylistObservable
    ) = Unit

    override suspend fun startController() = Unit

    override fun release() = Unit
}

private class FakePlaylistDetailsRepository : PlaylistDetailsRepository {

    private val currentList = listOf(
        Track(
            trackId = 100L,
            trackUrl = "trackUrl1",
            coverUrl = "coverUrl1",
            trackName = "testTitle1",
            artistName = "testSubTitle1",
        ),
        Track(
            trackId = 200L,
            trackUrl = "trackUrl2",
            coverUrl = "coverUrl2",
            trackName = "testTitle2",
            artistName = "testSubTitle2",
        )
    )

    var playlistIdCalledArg: Long = -1L
    var currentTrackId: Long = 0L
    var currentPlaylistId: String = ""

    override suspend fun tracks(playlistId: Long): List<Track> {
        playlistIdCalledArg = playlistId
        return currentList
    }

    override fun readCurrentlyPlayingId(): Long = currentTrackId

    override fun saveCurrentlyPlayingId(id: Long) {
        currentTrackId = id
    }

    override fun saveCurrentPlayingPlaylist(id: String) {
        currentPlaylistId = id
    }
}

private class FakeMainObservable : MainObservable {

    val updateUiCallList = mutableListOf<MainUiState>()

    override fun updateUiState(uiState: MainUiState) {
        updateUiCallList.add(uiState)
    }

    override fun updateObserver(observer: UpdateUi<MainUiState>) = Unit

    override fun clearObserver() = Unit

    override fun clear() = Unit
}

private class FakeUiObservable : DashboardUiObservable {

    val updateUiCallList = mutableListOf<List<DashboardUi>>()
    private var currentIndex: Int = 0

    override fun updateUiState(uiState: List<DashboardUi>) {
        updateUiCallList.add(uiState)
    }

    override fun updateObserver(observer: UpdateUi<List<DashboardUi>>) = Unit

    override fun clearObserver() = Unit

    override fun clear() = Unit

    override fun currentlyPlayingId(): String {
        return ""
    }

    override fun nextTrack(): DashboardUi.Track {
        currentIndex += 1
        return updateUiCallList.last()[currentIndex] as DashboardUi.Track
    }

    override fun play(track: DashboardUi) {
        val lastList = updateUiCallList.last()
        val copyList = lastList.toMutableList()

        lastList.find {
            it.isPlaying()
        }?.let {
            val currentlyPlayingIndex = lastList.indexOf(it)
            val notPlaying = lastList[currentlyPlayingIndex].changePlaying()
            copyList[currentlyPlayingIndex] = notPlaying
        }

        currentIndex = lastList.indexOf(track)
        copyList[currentIndex] = track.changePlaying()
        updateUiState(copyList)
    }

    override fun currentTrackIndex(): Int = currentIndex

    override fun stop() {
        val lastList = updateUiCallList.last()
        val copyList = lastList.toMutableList()

        lastList.find {
            it.isPlaying()
        }?.let {
            val currentlyPlayingIndex = lastList.indexOf(it)
            val notPlaying = lastList[currentlyPlayingIndex].changePlaying()
            copyList[currentlyPlayingIndex] = notPlaying
        }

        updateUiState(copyList)
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