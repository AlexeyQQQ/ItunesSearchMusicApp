package ru.easycode.intensive2itunessearch.ui.main

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import ru.easycode.intensive2itunessearch.R

class MainPage {

    fun clickSearchIcon() {
        onView(withId(R.id.searchItem)).perform(click())
    }

    fun clickPlaylistIcon() {
        onView(withId(R.id.playlistsItem)).perform(click())
    }
}
