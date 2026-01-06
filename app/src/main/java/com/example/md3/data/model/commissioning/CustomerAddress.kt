package com.example.md3.data.model.commissioning


import com.example.md3.data.model.visit.VisitAddress
import com.google.gson.annotations.SerializedName

data class CustomerAddress(
    @SerializedName("city")
    val city: String?,
    @SerializedName("complete_address")
    val completeAddress: String?,
    @SerializedName("country")
    val country: String?,
    @SerializedName("country_details")
    val countryDetails: CountryDetails?,
    @SerializedName("district")
    val district: String?,
    @SerializedName("google_maps_link")
    val googleMapsLink: String?,
    @SerializedName("latitude")
    val latitude: String?,
    @SerializedName("line1")
    val line1: String?,
    @SerializedName("line2")
    val line2: String?,
    @SerializedName("longitude")
    val longitude: String?,
    @SerializedName("state")
    val state: String?,
    @SerializedName("zip")
    val zip: String?
){

    fun toVisitAddress(): VisitAddress {
        return VisitAddress(
            city = this.city,
            completeAddress = this.completeAddress,
            country = country,
            countryDetails = countryDetails,
            district = this.district,
            googleMapsLink = googleMapsLink,
            latitude = latitude,
            line1 = this.line1,
            line2 = this.line2,
            longitude = longitude,
            state = this.state,
            zip = this.zip
        )
    }
}