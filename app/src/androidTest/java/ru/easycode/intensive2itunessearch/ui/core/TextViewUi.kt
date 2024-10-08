package ru.easycode.intensive2itunessearch.ui.core

import android.view.View
import android.widget.TextView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import androidx.test.espresso.matcher.ViewMatchers.withId
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf

class TextViewUi(
    rootId: Matcher<View>,
    rootClass: Matcher<View>,
    resId: Int,
) : AbstractViewUi(
    onView(
        allOf(
            withId(resId),
            isAssignableFrom(TextView::class.java),
            rootId,
            rootClass
        )
    )
)