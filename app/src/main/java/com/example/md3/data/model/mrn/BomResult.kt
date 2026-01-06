package com.example.md3.data.model.mrn


import com.google.gson.annotations.SerializedName

data class BomResult(
    @SerializedName("bom_item_details")
    val bomItemDetails: BomItemDetails,
    @SerializedName("id")
    val id: String,
    @SerializedName("quantity")
    val quantity: Int
)