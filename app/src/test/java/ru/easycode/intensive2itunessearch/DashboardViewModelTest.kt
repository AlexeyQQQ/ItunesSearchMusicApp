package ru.easycode.intensive2itunessearch

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import ru.easycode.intensive2itunessearch.core.presentation.CurrentlyPlayingPlaylistObservable
import ru.easycode.intensive2itunessearch.core.presentation.CurrentlyShowingObservable
import ru.easycode.intensive2itunessearch.core.presentation.RunAsync
import ru.easycode.intensive2itunessearch.core.presentation.UpdateUi
import ru.easycode.intensive2itunessearch.core.presentation.exo_player.MediaPlayer
import ru.easycode.intensive2itunessearch.dashboard.data.DashboardRepository
import ru.easycode.intensive2itunessearch.dashboard.data.LoadResult
import ru.easycode.intensive2itunessearch.dashboard.data.Track
import ru.easycode.intensive2itunessearch.dashboard.presentation.DashboardUiObservable
import ru.easycode.intensive2itunessearch.dashboard.presentation.DashboardViewModel
import ru.easycode.intensive2itunessearch.dashboard.presentation.adapter.DashboardUi

class DashboardViewModelTest {

    @Test
    fun testCase() {
        val mediaPlayer = FakeMediaPlayer()
        val repository = FakeDashboardRepository()
        val runAsync = FakeRunAsync()
        val uiObservable = FakeUiObservable()

        val viewModel = DashboardViewModel(
            mediaPlayer = mediaPlayer,
            uiObservable = uiObservable,
            repository = repository,
            runAsync = runAsync,
        )

        // step 1 state progress
        viewModel.updateCurrentlyShowingObservable()
        assertEquals(viewModel, mediaPlayer.showingObservable)

        viewModel.search(query = "search for music")
        assertEquals(
            listOf(DashboardUi.Progress),
            uiObservable.updateUiCallList[0]
        )

        // step 2 state error
        runAsync.returnResult()
        assertEquals(
            listOf(DashboardUi.Error(message = "error")),
            uiObservable.updateUiCallList[1]
        )

        // step 3 state progress
        viewModel.retry()
        assertEquals(
            listOf(DashboardUi.Progress),
            uiObservable.updateUiCallList[2]
        )

        // step 4 state successful (stop all)
        val track1 = DashboardUi.Track(
            playlistId = "search for music",
            trackId = 100L,
            trackUrl = "trackUrl1",
            coverUrl = "coverUrl1",
            trackName = "testTitle1",
            artistName = "testSubTitle1",
            playing = false
        )
        val track2 = DashboardUi.Track(
            playlistId = "search for music",
            trackId = 200L,
            trackUrl = "trackUrl2",
            coverUrl = "coverUrl2",
            trackName = "testTitle2",
            artistName = "testSubTitle2",
            playing = false
        )

        runAsync.returnResult()
        assertEquals(
            listOf(
                track1,
                track2,
            ),
            uiObservable.updateUiCallList[3]
        )

        // step 5 state playing (track 1)
        viewModel.play(track = track1)
        assertEquals(
            listOf(
                track1.copy(playing = true),
                track2,
            ),
            uiObservable.updateUiCallList[4]
        )
        assertEquals(track1, mediaPlayer.cachedTrack)

        // step 6 state playing (track 2)
        viewModel.play(track = track2)
        assertEquals(
            listOf(
                track1,
                track2.copy(playing = true),
            ),
            uiObservable.updateUiCallList[5]
        )
        assertEquals(track2, mediaPlayer.cachedTrack)

        // step 7 stop playing
        viewModel.stop()
        assertEquals(
            listOf(
                track1,
                track2,
            ),
            uiObservable.updateUiCallList[6]
        )
        assertEquals(false, mediaPlayer.playing)

        viewModel.clearCurrentlyShowingObservable()
        assertEquals(CurrentlyShowingObservable.Empty, mediaPlayer.showingObservable)
    }
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

    private var cachedResult: Any? = null
    private var cachedUi: ((Any) -> Unit) = {}

    @Suppress("UNCHECKED_CAST")
    override fun <T : Any> runAsync(
        coroutineScope: CoroutineScope,
        background: suspend () -> T,
        ui: (T) -> Unit
    ) = runBlocking {
        val result = background.invoke()
        cachedResult = result
        cachedUi = ui as (Any) -> Unit
    }

    override fun launchUi(
        coroutineScope: CoroutineScope,
        function: suspend () -> Unit,
    ) = runBlocking {
        function.invoke()
    }

    fun returnResult() {
        cachedResult?.let { cachedUi.invoke(it) }
    }
}

private class FakeMediaPlayer : MediaPlayer {

    var playing: Boolean = false
    var cachedTrack: DashboardUi? = null
    var showingObservable: CurrentlyShowingObservable = CurrentlyShowingObservable.Empty

    override suspend fun play(track: DashboardUi) {
        cachedTrack = track
        playing = true
        showingObservable.notifyIsNowPlayingTrackId(track.id().toLong())
    }

    override fun stop() {
        playing = false
        showingObservable.notifyStopPlaying()
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

private class FakeDashboardRepository : DashboardRepository {

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

    var currentTrackId: Long = 0L
    var currentPlaylistId: String = ""
    var returnSuccess = false
    var saveUserQuery: String = ""

    override suspend fun tracks(query: String): LoadResult {
        return if (returnSuccess)
            LoadResult.Success(
                list = currentList,
                query = query,
            )
        else {
            returnSuccess = true
            LoadResult.Error(message = "error")
        }
    }

    override fun saveUserQuery(value: String) {
        saveUserQuery = value
    }

    override fun readUserQuery(): String = saveUserQuery

    override fun readCurrentlyPlayingId(): Long = currentTrackId

    override fun saveCurrentlyPlayingId(id: Long) {
        currentTrackId = id
    }

    override fun saveCurrentPlayingPlaylist(id: String) {
        currentPlaylistId = id
    }
}