package ru.easycode.intensive2itunessearch.ui.core

import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import org.hamcrest.Matchers.not


abstract class AbstractViewUi(
    protected val viewInteraction: ViewInteraction
) {

    fun checkVisible() {
        viewInteraction.check(matches(isDisplayed()))
    }

    fun checkNotVisible() {
        viewInteraction.check(matches(not(isDisplayed())))
    }

    fun checkNotExist() {
        viewInteraction.check(doesNotExist())
    }

    fun checkEnabled() {
        viewInteraction.check(matches(ViewMatchers.isEnabled()))
    }

    fun checkNotEnabled() {
        viewInteraction.check(matches(not(ViewMatchers.isEnabled())))
    }

    fun checkText(text: String) {
        viewInteraction.check(matches(ViewMatchers.withText(text)))
    }

    fun checkEmpty() {
        viewInteraction.check(matches(ViewMatchers.withText("")))
    }

    fun click() {
        viewInteraction.perform(ViewActions.click())
    }
}