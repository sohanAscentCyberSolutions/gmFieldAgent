package com.example.md3.data.model.commissioning.all_cases


import com.example.md3.data.model.commissioning.ServiceRequestDetails
import com.google.gson.annotations.SerializedName

data class AllCasesResponse(
    @SerializedName("count")
    val count: Int,
    @SerializedName("next")
    val next: String,
    @SerializedName("previous")
    val previous: Any,
    @SerializedName("results")
    val results: List<ServiceRequestDetails>
)