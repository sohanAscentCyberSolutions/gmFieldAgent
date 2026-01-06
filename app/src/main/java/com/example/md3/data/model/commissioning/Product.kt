package com.example.md3.data.model.commissioning


import com.google.gson.annotations.SerializedName

data class Product(
    @SerializedName("product")
    val product: String,
    @SerializedName("product_brand")
    val productBrand: String,
    @SerializedName("product_model_number")
    val productModelNumber: String,
    @SerializedName("product_name")
    val productName: String,
    @SerializedName("product_serial_number")
    val productSerialNumber: String
)