package ru.easycode.intensive2itunessearch.ui.dashboard

import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withParent
import org.hamcrest.Matcher
import ru.easycode.intensive2itunessearch.R
import ru.easycode.intensive2itunessearch.ui.ButtonUi
import ru.easycode.intensive2itunessearch.ui.InputUi
import ru.easycode.intensive2itunessearch.ui.RecyclerUi

class DashboardPage {

    private val rootId: Matcher<View> =
        withParent(withId(R.id.dashboardLayout))
    private val rootClass: Matcher<View> =
        withParent(isAssignableFrom(ConstraintLayout::class.java))

    private val inputUi = InputUi(rootId, rootClass, R.id.inputEditText)
    private val searchUi = ButtonUi(rootId, rootClass, R.id.searchButton, "Search")
    private val errorUi = ErrorUi()
    private val retry = RetryUi()
    private val progressUi = ProgressUi()
    private val recyclerUi = RecyclerUi(rootId, rootClass)


    fun checkProgressState() {
        progressUi.checkVisible()
    }

    fun checkErrorState(message: String) {
        errorUi.checkText(message)
    }

    fun checkSuccessfulState() {
        recyclerUi.checkTrackStateStop(
            position = 0,
            title = "testTitle1",
            subTitle = "testSubTitle1"
        )
        recyclerUi.checkTrackStateStop(
            position = 1,
            title = "testTitle2",
            subTitle = "testSubTitle2"
        )
    }

    fun inputText(text: String) {
        inputUi.inputText(text)
        searchUi.click()
    }

    fun clickTrack(position: Int) {
        recyclerUi.clickTrack(position)
    }

    fun checkPlaying(position: Int) {
        recyclerUi.checkTrackStatePlay(position = position)
    }

    fun clickRetry() {
        retry.click()
    }

    fun checkInitialState() {
        inputUi.checkVisible()
    }

    fun clickDots(position: Int) {
        recyclerUi.clickDots(position)
    }
}