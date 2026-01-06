package com.example.md3.data.model.commissioning.new_case

import com.google.gson.annotations.SerializedName



data class NewCasesResponse (
    @SerializedName("count")
    val count: Int,
    @SerializedName("next")
    val next: String?,
    @SerializedName("previous")
    val previous: String,
    @SerializedName("results")
    val results: List<NewCasesResult>
)