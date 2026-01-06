package com.example.md3.data.model.amc


import com.google.gson.annotations.SerializedName

data class PartX(
    @SerializedName("max_replacement_allowed")
    val maxReplacementAllowed: Int,
    @SerializedName("product")
    val product: String,
    @SerializedName("product_details")
    val productDetails: ProductDetailsXXX
)