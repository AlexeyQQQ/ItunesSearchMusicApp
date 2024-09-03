package ru.easycode.intensive2itunessearch.core.presentation.views

import android.content.Context
import android.util.AttributeSet
import androidx.annotation.DrawableRes
import androidx.appcompat.widget.AppCompatImageButton

class CustomImageButton : AppCompatImageButton, UpdateImageButton {

    constructor(context: Context) : super(context)
    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet)
    constructor(context: Context, attributeSet: AttributeSet, defStyleAttrs: Int) : super(
        context,
        attributeSet,
        defStyleAttrs
    )

    override fun changeImageResource(resId: Int) {
        this.setImageResource(resId)
    }
}

interface UpdateImageButton {

    fun changeImageResource(@DrawableRes resId: Int)
}