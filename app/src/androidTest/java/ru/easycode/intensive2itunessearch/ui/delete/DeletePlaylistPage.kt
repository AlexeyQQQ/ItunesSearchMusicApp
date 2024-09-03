package ru.easycode.intensive2itunessearch.ui.delete

import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withParent
import org.hamcrest.Matcher
import ru.easycode.intensive2itunessearch.R
import ru.easycode.intensive2itunessearch.ui.ButtonUi
import ru.easycode.intensive2itunessearch.ui.InputUi
import ru.easycode.intensive2itunessearch.ui.TextViewUi

class EditPlaylistPage {
    private val rootId: Matcher<View> =
        withParent(withId(R.id.editPlaylistLayout))
    private val rootClass: Matcher<View> =
        withParent(isAssignableFrom(ConstraintLayout::class.java))


    private val playlistNameUi = TextViewUi(rootId, rootClass, R.id.playlistNameTextView)
    private val inputUi = InputUi(rootId, rootClass, R.id.playlistNameEditText)
    private val deleteUi = ButtonUi(rootId, rootClass, R.id.deleteButton, "Delete")
    private val renameUi = ButtonUi(rootId, rootClass, R.id.renameButton, "Rename")

    fun checkNotExist() {
        playlistNameUi.checkNotExist()
    }

    fun checkSameValueState(playlistName: String) {
        playlistNameUi.checkText(playlistName)
        renameUi.checkNotEnabled()
        deleteUi.checkEnabled()
        inputUi.checkText(text = playlistName)
    }

    fun checkNotSameValueState(playlistName: String) {
        renameUi.checkEnabled()
        deleteUi.checkEnabled()
        inputUi.checkText(text = playlistName)
    }

    fun clickRename() {
        renameUi.click()
    }

    fun clickDelete() {
        deleteUi.click()
    }

    fun inputText(text: String) {
        inputUi.inputText(text)
    }
}