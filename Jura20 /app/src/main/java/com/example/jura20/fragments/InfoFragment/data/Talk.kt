package com.example.jura20.fragments.InfoFragment.data

import android.os.Parcelable
import androidx.annotation.DrawableRes
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Talk (
    @DrawableRes val image_talk: Int,
    val title_talk : String
): Parcelable