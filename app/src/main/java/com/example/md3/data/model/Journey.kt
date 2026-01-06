package com.example.md3.data.model


import com.google.gson.annotations.SerializedName

data class Journey(
    @SerializedName("created")
    val created: String,
    @SerializedName("end_time")
    val endTime: String,
    @SerializedName("hours")
    val hours: Double,
    @SerializedName("id")
    val id: String,
    @SerializedName("is_active")
    val isActive: Boolean,
    @SerializedName("modified")
    val modified: String,
    @SerializedName("start_time")
    val startTime: String,
    @SerializedName("visit")
    val visit: String
)








