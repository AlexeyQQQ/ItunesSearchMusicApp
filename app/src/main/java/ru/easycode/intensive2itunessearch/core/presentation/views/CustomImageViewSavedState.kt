package ru.easycode.intensive2itunessearch.core.presentation.views

import android.os.Parcel
import android.os.Parcelable
import android.view.View

class CustomImageViewSavedState : View.BaseSavedState {

    private lateinit var savedUrl: String

    constructor(superState: Parcelable) : super(superState)

    private constructor(parcelIn: Parcel) : super(parcelIn) {
        savedUrl = parcelIn.readString() ?: ""
    }

    override fun writeToParcel(out: Parcel, flags: Int) {
        super.writeToParcel(out, flags)
        out.writeString(savedUrl)
    }

    fun restore(): String = savedUrl

    fun save(url: String) {
        savedUrl = url
    }

    override fun describeContents() = 0

    companion object CREATOR : Parcelable.Creator<CustomImageViewSavedState> {
        override fun createFromParcel(parcel: Parcel): CustomImageViewSavedState =
            CustomImageViewSavedState(parcel)

        override fun newArray(size: Int): Array<CustomImageViewSavedState?> =
            arrayOfNulls(size)
    }
}