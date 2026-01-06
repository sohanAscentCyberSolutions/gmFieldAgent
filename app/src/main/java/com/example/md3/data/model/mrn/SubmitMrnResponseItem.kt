package com.example.md3.data.model.mrn

import com.google.gson.annotations.SerializedName


data class SubmitMrnResponseItem(
    @SerializedName("product_details")
    val productDetails: ProductDetails,
    @SerializedName("quantity")
    val quantity: Double
)