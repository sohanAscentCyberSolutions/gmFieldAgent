package com.example.md3.data.model.mrn


import com.google.gson.annotations.SerializedName

data class BomResponse(
    @SerializedName("count")
    val count: Int,
    @SerializedName("next")
    val next: Int?,
    @SerializedName("previous")
    val previous: Int?,
    @SerializedName("results")
    val results: List<BomResult>
)