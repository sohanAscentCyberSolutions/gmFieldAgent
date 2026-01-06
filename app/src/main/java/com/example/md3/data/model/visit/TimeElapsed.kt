package com.example.md3.data.model.visit


import com.google.gson.annotations.SerializedName

data class TimeElapsed(
    @SerializedName("hours")
    val hours: Int,
    @SerializedName("minutes")
    val minutes: Int
)



data class TotalJourneyHours(
    @SerializedName("hours")
    val hours: Int,
    @SerializedName("minutes")
    val minutes: Int
)