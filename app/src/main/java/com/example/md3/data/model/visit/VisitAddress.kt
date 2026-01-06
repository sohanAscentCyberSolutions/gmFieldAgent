package com.example.md3.data.model.visit


import com.google.gson.annotations.SerializedName

data class VisitAddress(
    @SerializedName("city")
    val city: String?,
    @SerializedName("complete_address")
    val completeAddress: String?,
    @SerializedName("country")
    val country: String?,
    @SerializedName("country_details")
    val countryDetails: Any?,
    @SerializedName("district")
    val district: String?,
    @SerializedName("google_maps_link")
    val googleMapsLink: Any?,
    @SerializedName("latitude")
    val latitude: Any?,
    @SerializedName("line1")
    val line1: String?,
    @SerializedName("line2")
    val line2: String?,
    @SerializedName("longitude")
    val longitude: Any?,
    @SerializedName("state")
    val state: String?,
    @SerializedName("zip")
    val zip: String?
)
