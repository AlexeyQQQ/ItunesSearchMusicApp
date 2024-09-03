package ru.easycode.intensive2itunessearch.ui.track

import android.widget.TextView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import androidx.test.espresso.matcher.ViewMatchers.withText
import org.hamcrest.Matchers.allOf
import ru.easycode.intensive2itunessearch.R
import ru.easycode.intensive2itunessearch.core.RecyclerViewMatcher

class PlaylistRecyclerUi {

    private val recyclerViewMatcher = RecyclerViewMatcher(R.id.playListRecyclerView)

    fun checkListState(position: Int, name: String) {
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
}

