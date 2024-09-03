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
import ru.easycode.intensive2itunessearch.dashboard.presentation.DashboardUiObservable
import ru.easycode.intensive2itunessearch.dashboard.presentation.adapter.DashboardUi
import ru.easycode.intensive2itunessearch.main.data.MainRepository
import ru.easycode.intensive2itunessearch.main.presentation.MainObservable
import ru.easycode.intensive2itunessearch.main.presentation.MainUiState
import ru.easycode.intensive2itunessearch.main.presentation.MainViewModel

class MainViewModelTest {

    @Test
    fun test() {
        val mainUiObservable = FakeMainObservable()
        val dashboardUiObservable = FakeDashboardUiObservable()
        val mediaPlayer = FakeMainMediaPlayer()
        val repository = FakeMainRepository()
        val runAsync = FakeMainRunAsync()

        val viewModel = MainViewModel(
            mainObservable = mainUiObservable,
            dashboardUiObservable = dashboardUiObservable,
            mediaPlayer = mediaPlayer,
            repository = repository,
            runAsync = runAsync,
        )

        val mainObserver: UpdateUi<MainUiState> = object : UpdateUi<MainUiState> {
            override fun updateUiState(uiState: MainUiState) = Unit
        }
        val dashboardObserver: UpdateUi<List<DashboardUi>> = object : UpdateUi<List<DashboardUi>> {
            override fun updateUiState(uiState: List<DashboardUi>) = Unit
        }
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

        // Step 1 Start updates
        viewModel.startUpdates(mainObserver, dashboardObserver)
        assertEquals(mainObserver, mainUiObservable.observer)
        assertEquals(dashboardObserver, dashboardUiObservable.observer)
        assertEquals(viewModel, mediaPlayer.playingObservable)

        // Step 2 Update list from player
        viewModel.startController()
        assertEquals(
            listOf(track1, track2),
            dashboardUiObservable.updateUiCallList[0]
        )

        // step 3 state playing (track 1)
        viewModel.play(track = track1)
        assertEquals(
            listOf(
                track1.copy(playing = true),
                track2,
            ),
            dashboardUiObservable.updateUiCallList[1]
        )
        assertEquals(
            MainUiState.ShowPlayer(
                track1.trackName(),
                track1.artistName(),
                track1.coverUrl(),
                true,
            ),
            mainUiObservable.updateUiCallList[0]
        )
        assertEquals(track1, mediaPlayer.cachedTrack)


        // step 3 state playing (track 2)
        viewModel.play(track = track2)
        assertEquals(
            listOf(
                track1,
                track2.copy(playing = true),
            ),
            dashboardUiObservable.updateUiCallList[2]
        )
        assertEquals(
            MainUiState.ShowPlayer(
                track2.trackName(),
                track2.artistName(),
                track2.coverUrl(),
                true,
            ),
            mainUiObservable.updateUiCallList[1]
        )
        assertEquals(track2, mediaPlayer.cachedTrack)

        // step 4 stop playing
        viewModel.stop()
        assertEquals(
            listOf(
                track1,
                track2,
            ),
            dashboardUiObservable.updateUiCallList[3]
        )
        assertEquals(
            MainUiState.ShowPlayer(
                track2.trackName(),
                track2.artistName(),
                track2.coverUrl(),
                false,
            ),
            mainUiObservable.updateUiCallList[2]
        )
        assertEquals(false, mediaPlayer.playing)

        // step 5 play in player
        viewModel.playOrStop()
        assertEquals(
            listOf(
                track1,
                track2.copy(playing = true),
            ),
            dashboardUiObservable.updateUiCallList[4]
        )
        assertEquals(
            MainUiState.ShowPlayer(
                track2.trackName(),
                track2.artistName(),
                track2.coverUrl(),
                true,
            ),
            mainUiObservable.updateUiCallList[3]
        )
        assertEquals(true, mediaPlayer.playing)

        // step 6 stop in player
        viewModel.playOrStop()
        assertEquals(
            listOf(
                track1,
                track2,
            ),
            dashboardUiObservable.updateUiCallList[5]
        )
        assertEquals(
            MainUiState.ShowPlayer(
                track2.trackName(),
                track2.artistName(),
                track2.coverUrl(),
                false,
            ),
            mainUiObservable.updateUiCallList[4]
        )
        assertEquals(false, mediaPlayer.playing)

        // step 7 previous track
        viewModel.previousTrack()
        assertEquals(
            MainUiState.ShowPlayer(
                track1.trackName(),
                track1.artistName(),
                track1.coverUrl(),
                false,
            ),
            mainUiObservable.updateUiCallList[5]
        )

        // step 8 play previous track
        viewModel.playOrStop()
        assertEquals(
            listOf(
                track1.copy(playing = true),
                track2,
            ),
            dashboardUiObservable.updateUiCallList[6]
        )
        assertEquals(
            MainUiState.ShowPlayer(
                track1.trackName(),
                track1.artistName(),
                track1.coverUrl(),
                true,
            ),
            mainUiObservable.updateUiCallList[6]
        )
        assertEquals(true, mediaPlayer.playing)

        // step 8 next track
        viewModel.nextTrack()
        assertEquals(
            listOf(
                track1,
                track2.copy(playing = true),
            ),
            dashboardUiObservable.updateUiCallList[7]
        )
        assertEquals(
            MainUiState.ShowPlayer(
                track2.trackName(),
                track2.artistName(),
                track2.coverUrl(),
                true,
            ),
            mainUiObservable.updateUiCallList[7]
        )

        // step 9 stop updates
        viewModel.stopUpdates()
        assertEquals(null, mainUiObservable.observer)
        assertEquals(null, dashboardUiObservable.observer)
        assertEquals(CurrentlyPlayingPlaylistObservable.Empty, mediaPlayer.playingObservable)

        viewModel.notifyObserved()
        assertEquals(null, mainUiObservable.cache)
    }
}

private class FakeMainObservable : MainObservable {

    var observer: UpdateUi<MainUiState>? = null
    var cache: MainUiState? = MainUiState.ShowBottomNav

    val updateUiCallList = mutableListOf<MainUiState>()

    override fun updateUiState(uiState: MainUiState) {
        updateUiCallList.add(uiState)
        this.cache = uiState
        if (observer != null) {
            observer!!.updateUiState(uiState)
        }
    }

    override fun updateObserver(observer: UpdateUi<MainUiState>) {
        this.observer = observer
        if (cache != null) {
            this.observer!!.updateUiState(cache!!)
        }
    }

    override fun clearObserver() {
        observer = null
    }

    override fun clear() {
        cache = null
    }
}

private class FakeDashboardUiObservable : DashboardUiObservable {

    var observer: UpdateUi<List<DashboardUi>>? = null

    val updateUiCallList = mutableListOf<List<DashboardUi>>()
    private var currentIndex: Int = 0

    override fun updateUiState(uiState: List<DashboardUi>) {
        updateUiCallList.add(uiState)
    }

    override fun updateObserver(observer: UpdateUi<List<DashboardUi>>) {
        this.observer = observer
    }

    override fun clearObserver() {
        observer = null
    }

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

        currentIndex = copyList.indexOf(
            copyList.find { it.id() == track.id() }
        )
        copyList[currentIndex] = track.play()
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

private class FakeMainRunAsync : RunAsync {

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

private class FakeMainRepository : MainRepository {

    var currentTrackId: Long = 0L
    var currentPlaylistId: String = ""

    override fun readCurrentlyPlayingId(): Long = currentTrackId

    override fun saveCurrentlyPlayingId(id: Long) {
        currentTrackId = id
    }

    override fun saveCurrentPlayingPlaylist(id: String) {
        currentPlaylistId = id
    }
}

private class FakeMainMediaPlayer : MediaPlayer {

    private val list = listOf(
        DashboardUi.Track(
            playlistId = "search for music",
            trackId = 100L,
            trackUrl = "trackUrl1",
            coverUrl = "coverUrl1",
            trackName = "testTitle1",
            artistName = "testSubTitle1",
            playing = false
        ),
        DashboardUi.Track(
            playlistId = "search for music",
            trackId = 200L,
            trackUrl = "trackUrl2",
            coverUrl = "coverUrl2",
            trackName = "testTitle2",
            artistName = "testSubTitle2",
            playing = false
        )
    )

    var playing: Boolean = false
    var cachedTrack: DashboardUi? = null
    var showingObservable: CurrentlyShowingObservable = CurrentlyShowingObservable.Empty
    var playingObservable: CurrentlyPlayingPlaylistObservable =
        CurrentlyPlayingPlaylistObservable.Empty

    override suspend fun play(track: DashboardUi) {
        cachedTrack = track
        playing = true
        showingObservable.notifyIsNowPlayingTrackId(track.id().toLong())
        playingObservable.notifyIsNowPlayingTrackId(track.id().toLong())
    }

    override fun stop() {
        playing = false
        showingObservable.notifyStopPlaying()
        playingObservable.notifyStopPlaying()
    }

    override fun isPlaying(): Boolean = playing

    override fun updateCurrentlyShowingObservable(
        currentlyShowingObservable: CurrentlyShowingObservable
    ) {
        showingObservable = currentlyShowingObservable
    }

    override fun updateCurrentlyPlayingObservable(
        currentlyPlayingPlaylistObservable: CurrentlyPlayingPlaylistObservable
    ) {
        playingObservable = currentlyPlayingPlaylistObservable
    }

    override suspend fun startController() {
        playingObservable.updateList(list)
    }

    override fun release() = Unit
}