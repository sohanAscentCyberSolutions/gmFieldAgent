package com.example.md3.data.model.amc


import com.google.gson.annotations.SerializedName

data class BrandDetails(
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String
)