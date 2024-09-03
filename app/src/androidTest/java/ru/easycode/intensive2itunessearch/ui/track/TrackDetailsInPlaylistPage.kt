package ru.easycode.intensive2itunessearch.ui.track

import androidx.test.espresso.matcher.ViewMatchers
import ru.easycode.intensive2itunessearch.R
import ru.easycode.intensive2itunessearch.ui.core.AbstractPage
import ru.easycode.intensive2itunessearch.ui.core.ButtonUi

class TrackDetailsInPlaylistPage :
    AbstractPage(ViewMatchers.withParent(ViewMatchers.withId(R.id.trackActionsLayout))) {

    private val removeUi =
        ButtonUi(rootId, rootClass, R.id.removeButton, "Remove track from current playlist")

    fun clickRemove() {
        removeUi.click()
    }
}