package com.example.md3.data.model.commissioning.open_cases


import com.google.gson.annotations.SerializedName

data class OpenCasesResponse(
    @SerializedName("count")
    val count: Int,
    @SerializedName("next")
    val next: String?,
    @SerializedName("previous")
    val previous: String,
    @SerializedName("results")
    val results: List<OpenCasesResult>
)