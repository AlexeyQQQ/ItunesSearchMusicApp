package ru.easycode.intensive2itunessearch.core.presentation.views

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

class CustomTextView : AppCompatTextView, UpdateTextView {

    constructor(context: Context) : super(context)
    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet)
    constructor(context: Context, attributeSet: AttributeSet, defStyleAttrs: Int) : super(
        context,
        attributeSet,
        defStyleAttrs
    )

    override fun changeText(text: String) {
        this.text = text
    }
}

interface UpdateTextView {

    fun changeText(text: String)
}