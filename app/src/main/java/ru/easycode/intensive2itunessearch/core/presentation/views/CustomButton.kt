package ru.easycode.intensive2itunessearch.core.presentation.views

import android.content.Context
import android.os.Parcelable
import android.util.AttributeSet
import com.google.android.material.button.MaterialButton

class CustomButton : MaterialButton, UpdateButton {

    constructor(context: Context) : super(context)
    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet)
    constructor(context: Context, attributeSet: AttributeSet, defStyleAttrs: Int) : super(
        context,
        attributeSet,
        defStyleAttrs
    )

    private var savedVisibility: Int = this.visibility
    private var savedEnabled: Boolean = this.isEnabled

    override fun changeVisibility(visibility: Int) {
        this.savedVisibility = visibility
        this.visibility = visibility
    }

    override fun changeEnabled(enabled: Boolean) {
        this.savedEnabled = enabled
        this.isEnabled = enabled
    }

    override fun onSaveInstanceState(): Parcelable {
        super.onSaveInstanceState().let {
            val savedState = CustomButtonSavedState(it)
            savedState.saveVisibility(savedVisibility)
            savedState.saveEnabled(savedEnabled)
            return savedState
        }
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        val restored = state as CustomButtonSavedState
        super.onRestoreInstanceState(restored.superState)
        changeVisibility(restored.restoreVisibility())
        changeEnabled(restored.restoreEnabled())
    }
}

interface UpdateButton {

    fun changeVisibility(visibility: Int)
    fun changeEnabled(enabled: Boolean)
}