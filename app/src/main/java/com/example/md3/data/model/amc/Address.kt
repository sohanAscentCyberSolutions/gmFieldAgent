package com.example.md3.data.model.amc


import com.google.gson.annotations.SerializedName

data class Address(
    @SerializedName("city")
    val city: String,
    @SerializedName("complete_address")
    val completeAddress: String,
    @SerializedName("country")
    val country: String,
    @SerializedName("country_details")
    val countryDetails: CountryDetails,
    @SerializedName("created")
    val created: String,
    @SerializedName("district")
    val district: String,
    @SerializedName("google_maps_link")
    val googleMapsLink: Any,
    @SerializedName("id")
    val id: String,
    @SerializedName("latitude")
    val latitude: Any,
    @SerializedName("line1")
    val line1: String,
    @SerializedName("line2")
    val line2: String,
    @SerializedName("longitude")
    val longitude: Any,
    @SerializedName("modified")
    val modified: String,
    @SerializedName("state")
    val state: String,
    @SerializedName("zip")
    val zip: String
)