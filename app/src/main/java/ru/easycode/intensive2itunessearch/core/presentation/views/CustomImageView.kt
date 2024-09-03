package ru.easycode.intensive2itunessearch.core.presentation.views

import android.content.Context
import android.os.Parcelable
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import ru.easycode.intensive2itunessearch.core.presentation.PicEngine

class CustomImageView : AppCompatImageView, UpdateImageUrl {

    constructor(context: Context) : super(context)
    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet)
    constructor(context: Context, attributeSet: AttributeSet, defStyleAttrs: Int) : super(
        context,
        attributeSet,
        defStyleAttrs
    )

    private var url: String = ""

    private val engine by lazy {
        (context.applicationContext as ProvidePicEngine).engine()
    }

    override fun updateImageUrl(url: String) {
        this.url = url
        engine.show(this, url)
    }

    override fun onSaveInstanceState(): Parcelable? {
        return super.onSaveInstanceState()?.let {
            val savedUrl = CustomImageViewSavedState(it)
            savedUrl.save(url)
            return savedUrl
        }
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        val restored = state as CustomImageViewSavedState
        super.onRestoreInstanceState(restored.superState)
        updateImageUrl(restored.restore())
    }
}

interface ProvidePicEngine {
    fun engine(): PicEngine
}

interface UpdateImageUrl {
    fun updateImageUrl(url: String)
}