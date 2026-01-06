package com.example.md3.data.model.observation


import com.google.gson.annotations.SerializedName

data class ObservationResult(
    @SerializedName("visit")
    val commissioningVisit: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("observation")
    val observation: String
)