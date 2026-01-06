package com.example.md3.data.model.amc


import com.google.gson.annotations.SerializedName

data class Addresse(
    @SerializedName("address")
    val address: Address,
    @SerializedName("flag_billing_address")
    val flagBillingAddress: Boolean,
    @SerializedName("flag_default_address")
    val flagDefaultAddress: Boolean,
    @SerializedName("flag_shipping_address")
    val flagShippingAddress: Boolean
)