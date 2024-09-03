package ru.easycode.intensive2itunessearch.core.presentation.views

import android.content.Context
import android.os.Parcelable
import android.util.AttributeSet
import com.google.android.material.bottomnavigation.BottomNavigationView

class CustomBottomNav : BottomNavigationView, UpdateBottomNav {

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

    override fun onSaveInstanceState(): Parcelable {
        super.onSaveInstanceState().let {
            val savedState = CustomBottomNavSavedState(it)
            savedState.save(this.visibility)
            return savedState
        }
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        val restored = state as CustomBottomNavSavedState
        super.onRestoreInstanceState(restored.superState)
        changeVisibility(restored.restore())
    }
}

interface UpdateBottomNav {

    fun changeVisibility(visibility: Int)
}