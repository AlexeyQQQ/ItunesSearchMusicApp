package ru.easycode.intensive2itunessearch.core

import android.view.View
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.core.graphics.drawable.toBitmap
import org.hamcrest.Description
import org.hamcrest.TypeSafeMatcher

class DrawableMatcher(@DrawableRes private val id: Int) : TypeSafeMatcher<View>() {
    override fun describeTo(description: Description) {
        description.appendText("ImageView with drawable same as drawable with id $id")
    }

    override fun matchesSafely(item: View): Boolean {
        val expectedBitmap = item.context.getDrawable(id)!!.toBitmap()
        return (item as ImageView).drawable.toBitmap().sameAs(expectedBitmap)
    }
}