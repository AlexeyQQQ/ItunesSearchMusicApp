package ru.easycode.intensive2itunessearch.ui

import androidx.test.espresso.Espresso
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import ru.easycode.intensive2itunessearch.core.waiting
import ru.easycode.intensive2itunessearch.main.presentation.MainActivity
import ru.easycode.intensive2itunessearch.ui.create.CreatePlaylistPage
import ru.easycode.intensive2itunessearch.ui.dashboard.DashboardPage
import ru.easycode.intensive2itunessearch.ui.delete.EditPlaylistPage
import ru.easycode.intensive2itunessearch.ui.details.PlaylistDetailsPage
import ru.easycode.intensive2itunessearch.ui.main.MainPage
import ru.easycode.intensive2itunessearch.ui.playlist.PlaylistsPage
import ru.easycode.intensive2itunessearch.ui.track.TrackActions
import ru.easycode.intensive2itunessearch.ui.track.TrackDetailsInPlaylistPage

@RunWith(AndroidJUnit4::class)
class ScenarioTest {

    @get:Rule
    var activityScenarioRule = ActivityScenarioRule(MainActivity::class.java)

    /**
     * TestCase 1
     *
     * 1. Input text
     * Check Progress State
     *
     * 2. Wait error
     * Error State
     *
     * 3. Click Retry Button
     * Check Progress State
     *
     * 4. Wait loading
     * Check Successful State
     *
     * 5. Click Play Image Button (track 1)
     * Check Playing State (track 1)
     *
     * 6. Wait until finish playing
     * Check Playing State (track 2)
     *
     * 7. Wait until finish playing
     * Check Successful State
     *
     * 8. Click Play Image Button (track 1)
     * Check Playing State (track 1)
     *
     * 9. Click Stop Image Button (track 1)
     * Check Successful State
     */
    @Test
    fun testCase() = with(DashboardPage()) {

        inputText(text = "search for music")
        checkProgressState()
        waiting(1500)
        checkErrorState(message = "No internet connection")

        clickRetry()
        checkProgressState()

        waiting(1500)
        checkSuccessfulState()

        clickTrack(position = 0)
        checkPlaying(position = 0)

        waiting(2100)
        checkPlaying(position = 1)

        waiting(2100)
        checkSuccessfulState()

        clickTrack(position = 0)
        waiting()
        checkPlaying(position = 0)
        clickTrack(position = 0)
        checkSuccessfulState()
    }

    /**TestCase 2

    1. Input text (Progress State)
    2. Wait error (Error State)
    3. Click Retry Button (Progress State)
    4. Wait loading (Successful State)

    5. Click three dots (InitialState)
    Check add visible

    6. Click create playlist button (EmptyInputState)
    Check input visible and empty, save disabled, cancel visible

    7. Input playlist name (NotEmptyInputState)
    Check save enabled, input not empty

    8. Click cancel (InitialState)
    Check create visible, add visible

    9. Click create playlist button (EmptyInputState)
    Check input visible and empty, save disabled

    10. Input playlist name (NotEmptyInputState)
    Check save enabled, input not empty

    11. Click save playlist button (ListState)
    Check list not empty, create visible

    12. Click create playlist (ListEmptyInputState)
    Check input empty, save disabled

    13. Input playlist name (ListNotEmptyInputState)
    Check input not empty, save enabled

    14. Click cancel (ListState)
    Check create visible, new playlist in addPage visible

    15. Click playlist in bottom sheet (bottom sheet closed)
    Check add not visible

    16. Click playlist icon in bottom navigation (PlayLists StateInitial)
    Check new playlist visible in list

    17. Click first playlist in PlayListsPage (PlayList StateDetails)
    Check track is visible
     */
    @Test
    fun testCase2() {

        val mainPage = MainPage()
        val dashboardPage = DashboardPage()
        val trackActions = TrackActions()
        val playListsPage = PlaylistsPage()
        val playlistDetailsPage = PlaylistDetailsPage()

        with(dashboardPage) {
            // 1
            inputText(text = "search for music")
            checkProgressState()
            // 2
            waiting(1500)
            checkErrorState(message = "No internet connection")
            // 3
            clickRetry()
            checkProgressState()
            // 4
            waiting(1500)
            checkSuccessfulState()
            // 5
            clickDots(position = 0)
        }

        with(trackActions) {
            checkInitialState()
            // 6
            clickCreate()
            checkEmptyInputState()
            // 7
            inputPlaylist(name = "playlist name")
            checkNotEmptyInputState(text = "playlist name")
            // 8
            clickCancel()
            checkInitialState()
            // 9
            clickCreate()
            checkEmptyInputState()
            // 10
            inputPlaylist(name = "playlist1")
            checkNotEmptyInputState(text = "playlist1")
            // 11
            clickSave()
            waiting()
            checkListState(position = 0, name = "playlist1")
            // 12
            clickCreate()
            checkListEmptyInputState()
            // 13
            inputPlaylist(name = "playlist name")
            checkListNotEmptyInputState(text = "playlist name")
            // 14
            clickCancel()
            checkListState(position = 0, name = "playlist1")
            // 15
            clickPlayList(position = 0)
            waiting()
            checkNotExist()
        }
        // 16
        mainPage.clickPlaylistIcon()
        waiting()
        playListsPage.checkPlaylist(position = 0, name = "playlist1")
        // 17
        playListsPage.clickPlaylist(position = 0)
        waiting()
        playlistDetailsPage.checkTrackVisible(position = 0, title = "testTitle1", subtitle = "testSubTitle1")
    }

    /**TestCase 3

    1. Click playlist icon in bottom nav (PlayLists StateInitial)
    Check Playlists screen visible (CreateButton visible)

    2. Click CreatePlayList (CreateNewListPage EmptyState)
    CreateNewPlaylistTextView visible, save disabled

    3. Click Cancel (PlayLists StateInitial)
    Check Playlists screen (CreateButton visible)

    4. Click Create PlayList (EmptyState)
    CreateNewPlaylistTextView visible, Save disabled, Input empty

    5. Input text (NotEmptyState)
    Check Save Enabled, Input not empty

    6. Click Save (PlayLists StateNotEmpty)
    Check Main visible (Bottom Nav visible),
    PlayLists screen visible (CreateNewPlaylistButton visible),
    CreateNewListPage not visible (CreateNewPlaylistTextView not visible),
    Playlist is first in list

    7. Click playlist at position 0 (PlayList Details)
    PlayLists not visible (Create Button not visible)
    Check list of tracks is empty

    8. Click back icon (PlayLists)
    Check playlist screen visible with 1 item
    PlayLists screen visible (Create Button visible),

    9. Click search icon in bottom nav (DashboardPage InitialState)
    Check input empty

    10. Input text
    Check items visible

    11. Click 3 dots on first track (AddTrackToPlaylistPage)
    Check bottom sheet visible
    Check playlist first is visible

    12. Click playlist
    Check bottom sheet not visible

    13. Click playlist icon in bottom nav
    Check playlist is visible

    14. Click playlist
    Check track visible
     */
    @Test
    fun testCase3() {

        val playListsPage = PlaylistsPage()
        val mainPage = MainPage()
        val createPlaylistPage = CreatePlaylistPage()
        val playlistDetailsPage = PlaylistDetailsPage()
        val dashboardPage = DashboardPage()
        val trackActions = TrackActions()
        // 1
        mainPage.clickPlaylistIcon()
        playListsPage.checkVisible()
        // 2
        playListsPage.clickCreate()
        createPlaylistPage.checkEmptyState()
        // 3
        createPlaylistPage.clickCancel()
        createPlaylistPage.checkNotExist()
        playListsPage.checkVisible()
        // 4
        playListsPage.clickCreate()
        createPlaylistPage.checkEmptyState()
        // 5
        createPlaylistPage.inputText(text = "playlist1")
        createPlaylistPage.checkNotEmptyState(text = "playlist1")
        // 6
        createPlaylistPage.clickSave()
        waiting(1000)
        playListsPage.checkPlaylist(position = 0, name = "playlist1")
        // 7
        playListsPage.clickPlaylist(position = 0)
        playListsPage.checkNotExist()
        playlistDetailsPage.checkPlaylistName(playlistName = "playlist1")
        // 8
        playlistDetailsPage.clickBackButton()
        waiting()
        playListsPage.checkPlaylist(position = 0, name = "playlist1")
        playlistDetailsPage.checkNotExist()
        // 9
        mainPage.clickSearchIcon()
        dashboardPage.checkInitialState()
        // 10 Success
        dashboardPage.inputText(text = "search for music")
        dashboardPage.checkProgressState()
        waiting(1500)
        dashboardPage.checkErrorState(message = "No internet connection")
        dashboardPage.clickRetry()
        dashboardPage.checkProgressState()
        waiting(1500)
        dashboardPage.checkSuccessfulState()
        // 11
        dashboardPage.clickDots(position = 0)
        trackActions.checkListState(position = 0, name = "playlist1")
        // 12
        trackActions.clickPlayList(position = 0)
        waiting()
        trackActions.checkNotExist()
        // 13
        mainPage.clickPlaylistIcon()
        waiting()
        playListsPage.checkPlaylist(position = 0, name = "playlist1")
        // 14
        playListsPage.clickPlaylist(position = 0)
        waiting()
        playlistDetailsPage.checkTrackVisible(position = 0, title = "testTitle1", subtitle = "testSubTitle1")
    }

    /**
     * TestCase 4 (Delete playlist testcase)
     *
     * 1. Click playlist icon in bottom nav
     * Check MyPlaylists screen visible
     *
     * 2. Click CreatePlayList
     * Check EmptyState in createPlaylistPage
     *
     * 3. Input text “playlist1”
     * Check NotEmptyState in createPlaylistPage
     *
     * 4. Click Button Save PlayList
     * CheckNotExist bottom nav
     * Check playlist1 is first in list
     *
     * 5. Click CreatePlayList
     * Check EmptyState in createPlaylistPage
     *
     * 6. Input text “playlist2”
     * Check NotEmptyState in createPlaylistPage
     *
     * 7. Click Button Save PlayList
     * CheckNotExist bottom nav
     * Check playlist2 is second in list
     *
     * 8. Click search icon in bottom nav
     * Check dashboard page visible
     *
     * 9. Input text
     * Check Progress State
     *
     * 10. Wait error
     * Check Error State
     *
     * 11. Click Retry Button
     * Check Progress State
     *
     * 12. Wait loading
     * Check Successful State
     *
     * 13. Click three dots
     * Check playlist1 is first in list
     * Check playlist2 is second in list
     *
     * 14. Click back
     * CheckNotExist AddToPlaylist page
     *
     * 15. Click playlist icon in bottom nav
     * Check playlist1 is first in list
     * Check playlist2 is second in list
     *
     * 16. Click three dots in playlist1
     * Check PlaylistBottomSheet InitialState
     *
     * 17. Click back
     * CheckNotExist PlaylistBottomSheet
     *
     * 18. Click three dots in playlist1
     * Check PlaylistBottomSheet InitialState
     *
     * 19. Click Yes Button
     * CheckNotExist PlaylistBottomSheet
     * Check playlist2 is first in list
     *
     * 20. Click search icon in bottom nav
     * Check dashboard page Successful State
     *
     * 21. Click three dots
     * Check AddToPlaylist page visible
     * Check playlist2 is first in list
     *
     */
    @Test
    fun testCase4() {
        val playListsPage = PlaylistsPage()
        val mainPage = MainPage()
        val createPlaylistPage = CreatePlaylistPage()
        val dashboardPage = DashboardPage()
        val trackActions = TrackActions()
        val editPlaylistPage = EditPlaylistPage()

        // 1
        mainPage.clickPlaylistIcon()
        playListsPage.checkVisible()

        // 2
        playListsPage.clickCreate()
        createPlaylistPage.checkEmptyState()

        // 3
        createPlaylistPage.inputText(text = "playlist1")
        createPlaylistPage.checkNotEmptyState(text = "playlist1")

        // 4
        createPlaylistPage.clickSave()
        waiting(1000)
        createPlaylistPage.checkNotExist()
        playListsPage.checkPlaylist(position = 0, name = "playlist1")

        // 5
        playListsPage.clickCreate()
        createPlaylistPage.checkEmptyState()

        // 6
        createPlaylistPage.inputText(text = "playlist2")
        createPlaylistPage.checkNotEmptyState(text = "playlist2")

        // 7
        createPlaylistPage.clickSave()
        waiting(1000)
        createPlaylistPage.checkNotExist()
        playListsPage.checkPlaylist(position = 1, name = "playlist2")

        // 8
        mainPage.clickSearchIcon()
        dashboardPage.checkInitialState()

        // 9
        dashboardPage.inputText(text = "search for music")
        dashboardPage.checkProgressState()

        // 10
        waiting(1500)
        dashboardPage.checkErrorState(message = "No internet connection")

        // 11
        dashboardPage.clickRetry()
        dashboardPage.checkProgressState()

        // 12
        waiting(1500)
        dashboardPage.checkSuccessfulState()

        // 13
        dashboardPage.clickDots(position = 0)
        trackActions.checkListState(position = 0, name = "playlist1")
        trackActions.checkListState(position = 1, name = "playlist2")

        // 14
        Espresso.pressBack()
        trackActions.checkNotExist()

        // 15
        mainPage.clickPlaylistIcon()
        playListsPage.checkPlaylist(position = 0, name = "playlist1")
        playListsPage.checkPlaylist(position = 1, name = "playlist2")

        // 16
        playListsPage.clickDots(position = 0)
        editPlaylistPage.checkSameValueState(playlistName = "playlist1")

        // 17
        Espresso.pressBack()
        editPlaylistPage.checkNotExist()

        // 18
        playListsPage.clickDots(position = 0)
        editPlaylistPage.checkSameValueState(playlistName = "playlist1")

        // 19
        editPlaylistPage.clickDelete()
        waiting(1000)
        editPlaylistPage.checkNotExist()
        playListsPage.checkPlaylist(position = 0, name = "playlist2")

        // 20
        mainPage.clickSearchIcon()
        waiting()
        dashboardPage.checkSuccessfulState()

        // 21
        dashboardPage.clickDots(position = 0)
        trackActions.checkListState(position = 0, name = "playlist2")
    }

    /**
     * TestCase 5 (Rename playlist testcase)
     *
     * 1. Click playlist icon in bottom nav (MainPage)
     * Check PlaylistsPage visible
     *
     * 2. Click CreatePlayList
     * Check CreatePlaylistPage EmptyState
     *
     * 3. Input text (“playlist1”)
     * Check CreatePlaylistPage NotEmptyState
     *
     * 4. Click Button Save PlayList
     * Check CreatePlaylistPage not exist
     * Check PlayListsPage “playlist1” is first in list
     *
     * 5. Click search icon in bottom nav (MainPage)
     * Check dashboardPage visible (InitialState)
     *
     * 6. Input text
     * Check DashboardPage ProgressState
     *
     * 7. Wait, Error
     * Check DashboardPage ErrorState
     *
     * 8. Click Retry Button
     * Check DashboardPage ProgressState
     *
     * 9. Wait, Successful
     * Check DashboardPage SuccessfulState
     *
     * 10. Click dots position 0 (DashboardPage)
     * Check AddTrackToPlaylistPage playlist1 is first in list
     *
     * 11. Click “playlist1”
     * Check AddTrackToPlaylistPage not exist
     *
     * 12. Click playlist icon in bottom nav
     * Check PlayListsPage playlist1 is first in list
     *
     * 13. Click position 0 (PlayListsPage)
     * Check PlayListsDetailsPage playlistName = “playlist1”
     * Check added track is first in list
     *
     * 14. Click Back
     * Check PlayListsDetailsPage not exist
     * Check PlayListsPage playlist1 is first in list
     *
     * 15. Click three dots in “playlist1”
     * Check EditPlaylistPage SameValueState
     *
     * 16. Click back
     * Check EditPlaylistPage not exist
     *
     * 17. Click three dots in “playlist1”
     * Check EditPlaylistPage SameValueState
     *
     * 18. Input text “playlist2”
     * Check NotSameValueState(“playlist2”) in EditPlaylistPage
     *
     * 19. Click Rename Button
     * Check EditPlaylistPage not exist
     * Check PlayListPage “playlist2” in first pos
     *
     * 20. Click playlist position 0 (PlayListsPage)
     * Check PlayListsDetailsPage playlistName = “playlist2”
     * Check added track is first in list
     *
     * 21. Click search icon in bottom nav (MainPage)
     * Check dashboard page Successful State
     *
     * 22. Click three dots position 1 (DashboardPage)
     * Check AddTrackToPlaylistPage ListState "playlist2" in first pos
     *
     */
    @Test
    fun testCase5() {
        val playListsPage = PlaylistsPage()
        val mainPage = MainPage()
        val createPlaylistPage = CreatePlaylistPage()
        val dashboardPage = DashboardPage()
        val trackActions = TrackActions()
        val playlistDetailsPage = PlaylistDetailsPage()
        val editPlaylistPage = EditPlaylistPage()

        // 1
        mainPage.clickPlaylistIcon()
        playListsPage.checkVisible()

        // 2
        playListsPage.clickCreate()
        createPlaylistPage.checkEmptyState()

        // 3
        createPlaylistPage.inputText(text = "playlist1")
        createPlaylistPage.checkNotEmptyState(text = "playlist1")

        // 4
        createPlaylistPage.clickSave()
        waiting(1000)
        createPlaylistPage.checkNotExist()
        playListsPage.checkPlaylist(position = 0, name = "playlist1")

        // 5
        mainPage.clickSearchIcon()
        dashboardPage.checkInitialState()

        // 6
        dashboardPage.inputText(text = "search for music")
        dashboardPage.checkProgressState()

        // 7
        waiting(1500)
        dashboardPage.checkErrorState(message = "No internet connection")

        // 8
        dashboardPage.clickRetry()
        dashboardPage.checkProgressState()

        // 9
        waiting(1500)
        dashboardPage.checkSuccessfulState()

        // 10
        dashboardPage.clickDots(position = 0)
        trackActions.checkListState(position = 0, name = "playlist1")

        // 11
        trackActions.clickPlayList(position = 0)
        waiting()
        trackActions.checkNotExist()

        // 12
        mainPage.clickPlaylistIcon()
        playListsPage.checkPlaylist(position = 0, name = "playlist1")

        // 13
        playListsPage.clickPlaylist(position = 0)
        waiting()
        playlistDetailsPage.checkPlaylistName(playlistName = "playlist1")
        playlistDetailsPage.checkTrackVisible(position = 0, title = "testTitle1", subtitle = "testSubTitle1")

        // 14
        Espresso.pressBack()
        waiting()
        playlistDetailsPage.checkNotExist()
        playListsPage.checkPlaylist(position = 0, name = "playlist1")

        // 15
        playListsPage.clickDots(position = 0)
        editPlaylistPage.checkSameValueState(playlistName = "playlist1")

        // 16
        Espresso.pressBack()
        editPlaylistPage.checkNotExist()

        // 17
        playListsPage.clickDots(position = 0)
        editPlaylistPage.checkSameValueState(playlistName = "playlist1")

        // 18
        editPlaylistPage.inputText(text = "playlist2")
        editPlaylistPage.checkNotSameValueState(playlistName = "playlist2")

        // 19
        editPlaylistPage.clickRename()
        waiting(1000)
        editPlaylistPage.checkNotExist()
        playListsPage.checkPlaylist(position = 0, name = "playlist2")

        // 20
        playListsPage.clickPlaylist(position = 0)
        waiting()
        playlistDetailsPage.checkPlaylistName(playlistName = "playlist2")
        playlistDetailsPage.checkTrackVisible(position = 0, title = "testTitle1", subtitle = "testSubTitle1")

        // 21
        Espresso.pressBack()
        mainPage.clickSearchIcon()
        waiting()
        dashboardPage.checkSuccessfulState()

        // 22
        dashboardPage.clickDots(position = 1)
        trackActions.checkListState(position = 0, name = "playlist2")
    }

    /**
     * TestCase 6
     *
     * 1. Input text
     * Check Progress State
     *
     * 2. Wait error
     * Check Error State
     *
     * 3. Click Retry Button
     * Check Progress State
     *
     * 4. Wait loading
     * Check Successful State
     *
     * 5. Click three dots (track position 0)
     * Check AddToPlaylist TextView visible
     *
     * 6. Click create playlist button
     * Check input visible
     * Check save playlist button disabled
     *
     * 7. Input playlist name “playlist”
     * Check save enabled
     *
     * 8 Click save playlist
     * Check “playlist” at position 0
     *
     * 9 Click playlist at position 0
     * Check bottom not visible
     *
     * 10 Click three dots (track position 1)
     * Check AddToPlaylist TextView visible
     *
     * 11 Click playlist at position 0
     * Check bottom not visible
     *
     * 12 Click playlist in bottom nav
     * Check “playlist” at position 0
     *
     * 13 Click playlist at position 0
     * Check track name “track1” position 0
     * Check track name “track2” position 1
     *
     * 14 Click 3 dots track position 0 ("track1")
     * Check bottonSheet visible
     *
     * 15 Click remove button
     * Check bottonSheet not visible
     * Check track name “track2” position 0
     */
    @Test
    fun testCase6() {

        val mainPage = MainPage()
        val dashboardPage = DashboardPage()
        val trackActions = TrackActions()
        val playListsPage = PlaylistsPage()
        val playlistDetailsPage = PlaylistDetailsPage()
        val trackDetailsInPlaylistPage = TrackDetailsInPlaylistPage()

        // 1
        dashboardPage.inputText(text = "search for music")
        dashboardPage.checkProgressState()
        // 2
        waiting(1500)
        dashboardPage.checkErrorState(message = "No internet connection")
        // 3
        dashboardPage.clickRetry()
        dashboardPage.checkProgressState()
        // 4
        waiting(1500)
        dashboardPage.checkSuccessfulState()
        // 5
        dashboardPage.clickDots(position = 0)
        trackActions.checkInitialState()
        // 6
        trackActions.clickCreate()
        trackActions.checkEmptyInputState()
        // 7
        trackActions.inputPlaylist("playlist")
        trackActions.checkNotEmptyInputState("playlist")
        // 8
        trackActions.clickSave()
        trackActions.checkListState(0, "playlist")
        // 9
        trackActions.clickPlayList(0)
        trackActions.checkNotExist()
        // 10
        dashboardPage.clickDots(position = 1)
        trackActions.checkListState(0, "playlist")
        // 11
        trackActions.clickPlayList(0)
        trackActions.checkNotExist()
        // 12
        mainPage.clickPlaylistIcon()
        playListsPage.checkPlaylist(0, "playlist")
        // 13
        playListsPage.clickPlaylist(0)
        playlistDetailsPage.checkTrackVisible(position = 0, title = "testTitle1", subtitle = "testSubTitle1")
        playlistDetailsPage.checkTrackVisible(position = 1, title = "testTitle2", subtitle = "testSubTitle2")
        // 14
        playlistDetailsPage.clickDots(position = 0)
        trackDetailsInPlaylistPage.checkInitialState()
        // 15
        trackDetailsInPlaylistPage.clickRemove()
        trackDetailsInPlaylistPage.checkNotExist()
        playlistDetailsPage.checkTrackVisible(position = 0,title = "testTitle2", subtitle = "testSubTitle2")
    }
}