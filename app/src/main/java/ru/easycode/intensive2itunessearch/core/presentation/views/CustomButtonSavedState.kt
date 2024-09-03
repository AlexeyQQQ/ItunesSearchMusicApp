package ru.easycode.intensive2itunessearch.core.presentation.views

import android.os.Parcel
import android.os.Parcelable
import android.view.View

class CustomButtonSavedState : View.BaseSavedState {

    private var savedVisibility: Int = View.VISIBLE
    private var savedEnabled: Boolean = true

    constructor(superState: Parcelable) : super(superState)

    private constructor(parcelIn: Parcel) : super(parcelIn) {
        savedVisibility = parcelIn.readInt()
        savedEnabled = parcelIn.readByte() != 0.toByte()
    }

    override fun writeToParcel(out: Parcel, flags: Int) {
        super.writeToParcel(out, flags)
        out.writeInt(savedVisibility)
        out.writeByte(if (savedEnabled) 1 else 0)
    }

    fun restoreVisibility(): Int = savedVisibility

    fun saveVisibility(visibility: Int) {
        savedVisibility = visibility
    }

    fun restoreEnabled(): Boolean = savedEnabled

    fun saveEnabled(enabled: Boolean) {
        savedEnabled = enabled
    }

    override fun describeContents() = 0

    companion object CREATOR : Parcelable.Creator<CustomButtonSavedState> {
        override fun createFromParcel(parcel: Parcel): CustomButtonSavedState =
            CustomButtonSavedState(parcel)

        override fun newArray(size: Int): Array<CustomButtonSavedState?> =
            arrayOfNulls(size)
    }
}