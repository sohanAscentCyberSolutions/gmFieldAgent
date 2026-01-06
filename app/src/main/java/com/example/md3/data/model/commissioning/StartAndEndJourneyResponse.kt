package com.example.md3.data.model.commissioning


import com.google.gson.annotations.SerializedName

data class StartAndEndJourneyResponse(
    @SerializedName("visit")
    val commissioningVisit: String,
    @SerializedName("created")
    val created: String,
    @SerializedName("end_time")
    val endTime: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("is_active")
    val isActive: Boolean,
    @SerializedName("modified")
    val modified: String,
    @SerializedName("start_time")
    val startTime: String
)