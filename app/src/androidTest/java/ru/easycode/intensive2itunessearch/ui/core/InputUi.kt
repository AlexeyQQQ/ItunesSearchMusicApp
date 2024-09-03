package ru.easycode.intensive2itunessearch.ui.core

import android.view.View
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.google.android.material.textfield.TextInputEditText
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf

class InputUi(
    rootId: Matcher<View>,
    rootClass: Matcher<View>,
    resId: Int,
) : AbstractViewUi(
    onView(
        allOf(
            withId(resId),
            isAssignableFrom(TextInputEditText::class.java),
            rootId,
            rootClass
        )
    )
) {

    fun inputText(text: String) {
        viewInteraction.perform(replaceText(text), closeSoftKeyboard())
    }
}