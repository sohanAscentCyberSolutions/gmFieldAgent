package com.example.md3.data.model.commissioning


import com.google.gson.annotations.SerializedName

data class TypeDetails(
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String
)