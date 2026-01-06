package com.example.md3.data.model.commissioning


import com.google.gson.annotations.SerializedName

data class CommissionCheckStatusResponse(
    @SerializedName("error_message")
    val message: String
)