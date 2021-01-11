package com.example.jura20.fragments.HomeFragment.data

import android.os.Parcelable
import androidx.annotation.DrawableRes
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Item (
    val id : Int,
    val level: String,
    val title: String,
    @DrawableRes val image: Int,
    val text: String,
): Parcelable