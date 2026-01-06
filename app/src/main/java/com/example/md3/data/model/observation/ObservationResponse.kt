package com.example.md3.data.model.observation


import com.google.gson.annotations.SerializedName

data class ObservationResponse(
    @SerializedName("commissioning_visit")
    val commissioningVisit: String,
    @SerializedName("created")
    val created: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("modified")
    val modified: String,
    @SerializedName("observation")
    val observation: String
)