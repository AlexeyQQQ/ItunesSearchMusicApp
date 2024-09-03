package ru.easycode.intensive2itunessearch.ui.playlist

import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withParent
import org.hamcrest.Matcher
import ru.easycode.intensive2itunessearch.R
import ru.easycode.intensive2itunessearch.ui.ImageButtonUi
import ru.easycode.intensive2itunessearch.ui.TextViewUi

class PlaylistsPage {

    private val rootId: Matcher<View> = withParent(withId(R.id.playlistsLayout))
    private val rootClass: Matcher<View> =
        withParent(isAssignableFrom(ConstraintLayout::class.java))

    private val titleUi = TextViewUi(rootId, rootClass, R.id.playlistsTitleTextView)
    private val playlistsRecyclerUi = PlaylistsRecyclerUi(rootId, rootClass)
    private val createUi = ImageButtonUi(R.id.listCreateButton, R.drawable.ic_add_playlist)

    fun checkPlaylist(position: Int, name: String) {
        playlistsRecyclerUi.checkPlaylist(position, name)
    }

    fun clickPlaylist(position: Int) {
        playlistsRecyclerUi.clickPlayList(position)
    }

    fun checkVisible() {
        createUi.checkVisible()
    }

    fun clickCreate() {
        createUi.click()
    }

    fun checkNotExist() {
        createUi.checkNotExist()
        titleUi.checkNotExist()
    }

    fun clickDots(position: Int) {
        playlistsRecyclerUi.clickDots(position)
    }
}
