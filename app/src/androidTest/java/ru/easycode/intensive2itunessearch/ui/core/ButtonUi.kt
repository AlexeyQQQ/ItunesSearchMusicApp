package ru.easycode.intensive2itunessearch.ui.core

import android.view.View
import android.widget.Button
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf

class ButtonUi(
    rootId: Matcher<View>,
    rootClass: Matcher<View>,
    resId: Int,
    text: String,
) : AbstractViewUi(
    onView(
        allOf(
            withId(resId),
            withText(text),
            isAssignableFrom(Button::class.java),
            rootId,
            rootClass,
        )
    )
)