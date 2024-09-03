package ru.easycode.intensive2itunessearch.ui.create

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

class CreatePlaylistPage {

    private val rootId: Matcher<View> =
        withParent(withId(R.id.createPlaylistLayout))
    private val rootClass: Matcher<View> =
        withParent(isAssignableFrom(ConstraintLayout::class.java))

    private val titleUi = TextViewUi(rootId, rootClass, R.id.titleTextView)
    private val inputUi = InputUi(rootId, rootClass, R.id.inputEditText)
    private val cancelUi = ButtonUi(rootId, rootClass, R.id.cancelButton, "Cancel")
    private val saveUi = ButtonUi(rootId, rootClass, R.id.saveButton, "Save playlist")

    fun checkEmptyState() {
        inputUi.checkEmpty()
        saveUi.checkNotEnabled()
    }

    fun checkNotEmptyState(text: String) {
        inputUi.checkText(text)
        saveUi.checkEnabled()
    }

    fun inputText(text: String) {
        inputUi.inputText(text)
    }

    fun checkNotExist() {
        titleUi.checkNotExist()
    }

    fun clickCancel() {
        cancelUi.click()
    }

    fun clickSave() {
        saveUi.click()
    }
}