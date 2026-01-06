package com.example.md3.data.model.amc


import com.google.gson.annotations.SerializedName

data class CategoryDetailsX(
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("parent")
    val parent: String
)