package com.example.md3.data.model.product


import com.google.gson.annotations.SerializedName

data class CategoryDetails(
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String
)