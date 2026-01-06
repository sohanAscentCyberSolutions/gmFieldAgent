package com.example.md3.data.model.amc


import com.google.gson.annotations.SerializedName

data class CategoryDetails(
    @SerializedName("created")
    val created: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("is_active")
    val isActive: Boolean,
    @SerializedName("modified")
    val modified: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("parent")
    val parent: String,
    @SerializedName("vertical")
    val vertical: Any
)