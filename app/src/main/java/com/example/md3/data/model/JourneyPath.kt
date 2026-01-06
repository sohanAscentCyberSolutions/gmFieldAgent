package com.example.md3.data.model


import com.google.gson.annotations.SerializedName

data class JourneyPath(
    @SerializedName("path")
    val path: List<Path>
)