package ru.easycode.intensive2itunessearch.ui.details

import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withParent
import org.hamcrest.Matcher
import ru.easycode.intensive2itunessearch.R
import ru.easycode.intensive2itunessearch.ui.core.ImageButtonUi
import ru.easycode.intensive2itunessearch.ui.core.RecyclerUi
import ru.easycode.intensive2itunessearch.ui.core.TextViewUi

class PlaylistDetailsPage {

    private val rootId: Matcher<View> =
        withParent(withId(R.id.playlistDetailsLayout))
    private val rootClass: Matcher<View> =
        withParent(isAssignableFrom(ConstraintLayout::class.java))

    private val backButtonUi = ImageButtonUi(R.id.backButton, R.drawable.ic_back)
    private val titleUi = TextViewUi(rootId, rootClass, R.id.titleTextView)
    private val recyclerUi = RecyclerUi(rootId, rootClass)

    fun checkTrackVisible(position: Int, title: String, subtitle: String) {
        recyclerUi.checkTrackStateStop(
            position = position,
            title = title,
            subTitle = subtitle
        )
    }

    fun checkPlaylistName(playlistName: String) {
        titleUi.checkText(playlistName)
    }

    fun clickBackButton() {
        backButtonUi.click()
    }

    fun checkNotExist() {
        backButtonUi.checkNotExist()
        titleUi.checkNotExist()
    }

    fun clickDots(position: Int) {
        recyclerUi.clickDots(position)
    }
}