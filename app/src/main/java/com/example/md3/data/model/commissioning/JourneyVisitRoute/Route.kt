package com.example.md3.data.model.commissioning.JourneyVisitRoute


import com.google.gson.annotations.SerializedName

data class Route(
    @SerializedName("latitude")
    val latitude: String,
    @SerializedName("longitude")
    val longitude: String
)