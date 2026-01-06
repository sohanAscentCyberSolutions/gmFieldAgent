package com.example.md3.data.model


import com.google.gson.annotations.SerializedName

data class Path(
    @SerializedName("latitude")
    val latitude: Double,
    @SerializedName("longitude")
    val longitude: Double
)