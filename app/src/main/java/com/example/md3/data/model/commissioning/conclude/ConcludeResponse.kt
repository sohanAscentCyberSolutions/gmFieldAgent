package com.example.md3.data.model.commissioning.conclude


import com.google.gson.annotations.SerializedName

data class ConcludeResponse(
    @SerializedName("id")
    val id: String,
    @SerializedName("created")
    val created: String,
    @SerializedName("modified")
    val modified: String?,
    @SerializedName("name")
    val name: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("phone")
    val phone: String,
    @SerializedName("designation")
    val designation: String,
    @SerializedName("customer_feedback")
    val customer_feedback: String,
    @SerializedName("customer_signature")
    val customer_signature: String,
    @SerializedName("customer_remark")
    val customer_remark: String,
    @SerializedName("commissioning")
    val commissioning: String
)