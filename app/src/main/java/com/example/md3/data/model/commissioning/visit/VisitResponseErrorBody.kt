package com.example.md3.data.model.commissioning.visit


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class VisitResponseErrorBody(
    @SerializedName("message")
    @Expose val message: String,
    @SerializedName("visit_details")
    val visitDetails: VisitDetails
)