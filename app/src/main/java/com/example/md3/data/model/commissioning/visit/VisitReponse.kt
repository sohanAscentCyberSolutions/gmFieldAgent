package com.example.md3.data.model.commissioning.visit


import com.google.gson.annotations.SerializedName

data class VisitReponse(
    @SerializedName("count")
    val count: Int,
    @SerializedName("next")
    val next: Any,
    @SerializedName("previous")
    val previous: Any,
    @SerializedName("results")
    val results: List<VisitResult>
)