package com.example.md3.data.model.auth

import com.google.gson.annotations.SerializedName



data class Organisation(
    val id: String,
    @SerializedName("is_owner") val isOwner: Boolean,
    val name: String,
    val logo: String,
    val email: String,
    val phone: String,
    val sector: String,
    val brand: String,
    val information: String,
    val owner: String
)