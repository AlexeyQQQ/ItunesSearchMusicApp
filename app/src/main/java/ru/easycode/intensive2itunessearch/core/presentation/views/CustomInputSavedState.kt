package ru.easycode.intensive2itunessearch.core.presentation.views

import android.os.Parcel
import android.os.Parcelable
import android.view.View

class CustomInputSavedState : View.BaseSavedState {

    private var savedVisibility: Int = View.VISIBLE

    constructor(superState: Parcelable) : super(superState)

    private constructor(parcelIn: Parcel) : super(parcelIn) {
        savedVisibility = parcelIn.readInt()
    }

    override fun writeToParcel(out: Parcel, flags: Int) {
        super.writeToParcel(out, flags)
        out.writeInt(savedVisibility)
    }

    fun restore(): Int = savedVisibility

    fun save(visibility: Int) {
        savedVisibility = visibility
    }

    override fun describeContents() = 0

    companion object CREATOR : Parcelable.Creator<CustomInputSavedState> {
        override fun createFromParcel(parcel: Parcel): CustomInputSavedState =
            CustomInputSavedState(parcel)

        override fun newArray(size: Int): Array<CustomInputSavedState?> =
            arrayOfNulls(size)
    }
}
