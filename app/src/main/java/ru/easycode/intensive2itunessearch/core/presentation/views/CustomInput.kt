package ru.easycode.intensive2itunessearch.core.presentation.views

import android.content.Context
import android.os.Parcelable
import android.util.AttributeSet
import com.google.android.material.textfield.TextInputEditText

class CustomInput : TextInputEditText, UpdateInput {

    constructor(context: Context) : super(context)
    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet)
    constructor(context: Context, attributeSet: AttributeSet, defStyleAttrs: Int) : super(
        context,
        attributeSet,
        defStyleAttrs
    )

    private var savedVisibility: Int = this.visibility

    override fun changeVisibility(visibility: Int) {
        this.savedVisibility = visibility
        this.visibility = visibility
    }

    override fun clearText() {
        this.text?.clear()
    }

    override fun onSaveInstanceState(): Parcelable? {
        return super.onSaveInstanceState()?.let {
            val savedState = CustomInputSavedState(it)
            savedState.save(savedVisibility)
            return savedState
        }
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        val restored = state as CustomInputSavedState
        super.onRestoreInstanceState(restored.superState)
        changeVisibility(restored.restore())
    }
}

interface UpdateInput {

    fun changeVisibility(visibility: Int)
    fun clearText()
}