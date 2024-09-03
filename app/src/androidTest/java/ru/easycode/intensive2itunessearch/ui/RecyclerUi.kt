package ru.easycode.intensive2itunessearch.ui

import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import ru.easycode.intensive2itunessearch.R
import ru.easycode.intensive2itunessearch.core.DrawableMatcher
import ru.easycode.intensive2itunessearch.core.RecyclerViewMatcher

class RecyclerUi(
    rootId: Matcher<View>,
    rootClass: Matcher<View>,
) {

    private val recyclerViewMatcher = RecyclerViewMatcher(R.id.tracksRecyclerView)

    private val recyclerRootInteraction = onView(
        allOf(
            rootId,
            rootClass,
            withId(R.id.tracksRecyclerView)
        )
    )

    fun checkTrackStateStop(position: Int, title: String, subTitle: String) {
        recyclerRootInteraction.check(matches(isDisplayed()))

        onView(
            allOf(
                isAssignableFrom(ImageButton::class.java),
                recyclerViewMatcher.atPosition(position, R.id.playImageButton)
            )
        ).check(matches(DrawableMatcher(R.drawable.ic_play_track)))

        onView(
            allOf(
                isAssignableFrom(TextView::class.java),
                recyclerViewMatcher.atPosition(position, R.id.trackNameTextView)
            )
        ).check(matches(withText(title)))

        onView(
            allOf(
                isAssignableFrom(TextView::class.java),
                recyclerViewMatcher.atPosition(position, R.id.artistNameTextView)
            )
        ).check(matches(withText(subTitle)))

        onView(
            allOf(
                isAssignableFrom(ImageView::class.java),
                recyclerViewMatcher.atPosition(position, R.id.coverImageView)
            )
        ).check(matches(DrawableMatcher(R.mipmap.ic_launcher)))

        onView(
            allOf(
                isAssignableFrom(ImageButton::class.java),
                recyclerViewMatcher.atPosition(position, R.id.infoImageButton)
            )
        ).check(matches(DrawableMatcher(R.drawable.ic_info)))
    }

    fun checkTrackStatePlay(position: Int) {
        recyclerRootInteraction.check(matches(isDisplayed()))

        onView(
            allOf(
                isAssignableFrom(ImageButton::class.java),
                recyclerViewMatcher.atPosition(position, R.id.playImageButton)
            )
        ).check(matches(DrawableMatcher(R.drawable.ic_stop)))
    }

    fun clickTrack(position: Int) {
        onView(
            allOf(
                isAssignableFrom(ImageButton::class.java),
                recyclerViewMatcher.atPosition(position, R.id.playImageButton)
            )
        ).perform(click())
    }

    fun clickDots(position: Int) {
        onView(
            allOf(
                isAssignableFrom(ImageButton::class.java),
                recyclerViewMatcher.atPosition(position, R.id.infoImageButton)
            )
        ).perform(click())
    }
}
