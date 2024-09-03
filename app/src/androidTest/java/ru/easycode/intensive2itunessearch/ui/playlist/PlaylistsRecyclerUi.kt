package ru.easycode.intensive2itunessearch.ui.playlist

import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import androidx.test.espresso.matcher.ViewMatchers.withText
import org.hamcrest.Matcher
import org.hamcrest.core.AllOf.allOf
import ru.easycode.intensive2itunessearch.R
import ru.easycode.intensive2itunessearch.core.RecyclerViewMatcher

class PlaylistsRecyclerUi(
    private val rootId: Matcher<View>,
    private val rootClass: Matcher<View>
) {

    private val recyclerViewMatcher = RecyclerViewMatcher(R.id.playlistRecyclerView)

    fun checkPlaylist(position: Int, name: String) {
        onView(
            allOf(
                isAssignableFrom(TextView::class.java),
                recyclerViewMatcher.atPosition(position, R.id.playlistNameTextView)
            )
        ).check(matches(withText(name)))
    }

    fun clickPlayList(position: Int) {
        onView(recyclerViewMatcher.atPosition(position)).perform(click())
    }

    fun clickDots(position: Int) {
        onView(
            allOf(
                isAssignableFrom(ImageButton::class.java),
                recyclerViewMatcher.atPosition(position, R.id.infoButton)
            )
        ).perform(click())
    }
}
