package com.example.md3.data.model.commissioning.close_cases

import com.example.md3.data.model.commissioning.open_cases.OpenCasesResult
import com.google.gson.annotations.SerializedName

data class ClosedCasesResponse (
    @SerializedName("count")
    val count: Int,
    @SerializedName("next")
    val next: String,
    @SerializedName("previous")
    val previous: String,
    @SerializedName("results")
    val results: List<ClosedCasesResults>
)