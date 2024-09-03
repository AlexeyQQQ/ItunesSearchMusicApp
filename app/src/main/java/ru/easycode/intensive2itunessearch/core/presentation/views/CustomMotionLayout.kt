package ru.easycode.intensive2itunessearch.core.presentation.views

import android.content.Context
import android.os.Parcelable
import android.util.AttributeSet
import androidx.constraintlayout.motion.widget.MotionLayout

class CustomMotionLayout : MotionLayout, UpdateMotionLayout {

    constructor(context: Context) : super(context)
    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet)
    constructor(context: Context, attributeSet: AttributeSet, defStyleAttrs: Int) : super(
        context,
        attributeSet,
        defStyleAttrs
    )

    override fun changeVisibility(visibility: Int) {
        this.visibility = visibility
    }

    override fun onSaveInstanceState(): Parcelable? {
        return super.onSaveInstanceState()?.let {
            val savedState = CustomMotionLayoutSavedState(it)
            savedState.save(this.visibility)
            return savedState
        }
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        val restored = state as CustomMotionLayoutSavedState
        super.onRestoreInstanceState(restored.superState)
        changeVisibility(restored.restore())
    }
}

interface UpdateMotionLayout {

    fun changeVisibility(visibility: Int)
}