package com.example.md3.data.model.commissioning.visit


import com.google.gson.annotations.SerializedName

data class LatestJourneyDetails(
    @SerializedName("commissioning_visit")
    val commissioningVisit: String,
    @SerializedName("created")
    val created: String,
    @SerializedName("end_time")
    val endTime: Any,
    @SerializedName("id")
    val id: String,
    @SerializedName("is_active")
    val isActive: Boolean,
    @SerializedName("modified")
    val modified: String,
    @SerializedName("start_time")
    val startTime: String
)