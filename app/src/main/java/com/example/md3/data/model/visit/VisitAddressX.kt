package com.example.md3.data.model.visit


import com.google.gson.annotations.SerializedName

data class VisitAddressX(
    @SerializedName("city")
    val city: String,
    @SerializedName("country")
    val country: String,
    @SerializedName("district")
    val district: String,
    @SerializedName("line1")
    val line1: String,
    @SerializedName("line2")
    val line2: String,
    @SerializedName("state")
    val state: String,
    @SerializedName("zip")
    val zip: String
)