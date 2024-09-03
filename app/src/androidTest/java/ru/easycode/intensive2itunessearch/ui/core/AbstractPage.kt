package ru.easycode.intensive2itunessearch.ui.core

import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.test.espresso.matcher.ViewMatchers
import org.hamcrest.Matcher
import ru.easycode.intensive2itunessearch.R
import ru.easycode.intensive2itunessearch.ui.track.PlaylistRecyclerUi

abstract class AbstractPage(
    protected val rootId: Matcher<View> =
        ViewMatchers.withParent(ViewMatchers.withId(R.id.addPlayListlayout))
) {

    protected val rootClass: Matcher<View> =
        ViewMatchers.withParent(ViewMatchers.isAssignableFrom(ConstraintLayout::class.java))

    private val titleUi = TextViewUi(rootId, rootClass, R.id.titleTextView)
    private val createUi = ButtonUi(rootId, rootClass, R.id.createButton, "Create playlist")
    private val cancelUi = ButtonUi(rootId, rootClass, R.id.cancelButton, "Cancel")
    private val saveUi = ButtonUi(rootId, rootClass, R.id.saveButton, "Save playlist")
    private val inputUi = InputUi(rootId, rootClass, R.id.inputAddEditText)
    private val playListRecyclerUi = PlaylistRecyclerUi()

    fun checkInitialState() {
        createUi.checkEnabled()
        cancelUi.checkNotVisible()
        saveUi.checkNotVisible()
        inputUi.checkNotVisible()
    }

    fun checkEmptyInputState() {
        createUi.checkNotVisible()
        cancelUi.checkVisible()
        saveUi.checkNotEnabled()
        inputUi.checkEmpty()
    }

    fun checkNotEmptyInputState(text: String) {
        createUi.checkNotVisible()
        cancelUi.checkVisible()
        saveUi.checkEnabled()
        inputUi.checkText(text)
    }

    fun checkListState(position: Int, name: String) {
        createUi.checkVisible()
        cancelUi.checkNotVisible()
        saveUi.checkNotVisible()
        inputUi.checkNotVisible()
        playListRecyclerUi.checkListState(
            position = position,
            name = name
        )
    }

    fun checkListEmptyInputState() {
        createUi.checkNotVisible()
        cancelUi.checkVisible()
        saveUi.checkNotEnabled()
        inputUi.checkEmpty()
    }

    fun checkListNotEmptyInputState(text: String) {
        createUi.checkNotVisible()
        cancelUi.checkVisible()
        saveUi.checkEnabled()
        inputUi.checkText(text)
    }

    fun clickCreate() {
        createUi.click()
    }

    fun clickCancel() {
        cancelUi.click()
    }

    fun clickSave() {
        saveUi.click()
    }

    fun inputPlaylist(name: String) {
        inputUi.inputText(name)
    }

    fun clickPlayList(position: Int) {
        playListRecyclerUi.clickPlayList(position = position)
    }

    fun checkNotExist() {
        titleUi.checkNotExist()
    }
}