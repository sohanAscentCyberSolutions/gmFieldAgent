package com.example.md3.data.model.checksheet


import com.google.gson.annotations.SerializedName

data class SubSection(
    @SerializedName("fields")
    val fields: List<Field>,
    @SerializedName("id")
    val id: String,
    @SerializedName("title")
    val title: String
)



