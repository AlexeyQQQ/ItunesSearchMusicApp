package ru.easycode.intensive2itunessearch.ui.dashboard

import android.widget.TextView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import org.hamcrest.Matchers.allOf
import ru.easycode.intensive2itunessearch.R
import ru.easycode.intensive2itunessearch.ui.AbstractViewUi

class ErrorUi : AbstractViewUi(
    onView(
        allOf(
            withId(R.id.errorTextView),
            isAssignableFrom(TextView::class.java)
        )
    )
)
