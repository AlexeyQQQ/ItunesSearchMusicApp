package ru.easycode.intensive2itunessearch.ui

import android.widget.ImageButton
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import androidx.test.espresso.matcher.ViewMatchers.withId
import org.hamcrest.core.AllOf.allOf
import ru.easycode.intensive2itunessearch.core.DrawableMatcher

class ImageButtonUi(
    resId: Int,
    resourceDrawable: Int,
) : AbstractViewUi(
    onView(
        allOf(
            withId(resId),
            isAssignableFrom(ImageButton::class.java),
            DrawableMatcher(resourceDrawable)
        )
    )
)