package com.example.md3.data.model.commissioning


import com.google.gson.annotations.SerializedName

data class AssignedEngineerDetails(
    @SerializedName("email")
    val email: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("phone")
    val phone: Any
)