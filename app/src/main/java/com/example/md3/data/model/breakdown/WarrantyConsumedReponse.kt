package com.example.md3.data.model.breakdown

import com.google.gson.annotations.SerializedName

data class WarrantyConsumedReponse(
    @SerializedName("count")
    val count: Int,
    @SerializedName("next")
    val next: Any,
    @SerializedName("previous")
    val previous: Any,
    @SerializedName("results")
    val results: List<WarrantyConsumedResult>
)