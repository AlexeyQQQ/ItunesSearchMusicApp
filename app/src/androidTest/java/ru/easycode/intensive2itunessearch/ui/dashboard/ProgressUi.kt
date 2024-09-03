package ru.easycode.intensive2itunessearch.ui.dashboard

import android.widget.ProgressBar
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import androidx.test.espresso.matcher.ViewMatchers.withId
import org.hamcrest.Matchers.allOf
import ru.easycode.intensive2itunessearch.R
import ru.easycode.intensive2itunessearch.ui.core.AbstractViewUi

class ProgressUi : AbstractViewUi(
    onView(
        allOf(
            withId(R.id.progressBar),
            isAssignableFrom(ProgressBar::class.java)
        )
    )
)
