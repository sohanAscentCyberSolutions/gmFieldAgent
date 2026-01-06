package com.example.md3.data.model.amc


import com.google.gson.annotations.SerializedName

data class ServiceX(
    @SerializedName("duration")
    val duration: Int,
    @SerializedName("free_service_count")
    val freeServiceCount: Int,
    @SerializedName("service_type")
    val serviceType: String,
    @SerializedName("start_after_days")
    val startAfterDays: Int
)