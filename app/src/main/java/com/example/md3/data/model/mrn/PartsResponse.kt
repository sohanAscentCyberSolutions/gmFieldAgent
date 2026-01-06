package com.example.md3.data.model.mrn

import com.google.gson.annotations.SerializedName


data class PartsResponse(
    @SerializedName("items")
    val items: List<PartResultItem>
)


data class PartResultItem(
    @SerializedName("bom_item_details")
    val bomItemDetails: BomItemDetails,
    @SerializedName("warranty_details")
    val warrantyDetails: WarrantyDetails,
    @SerializedName("quantity")
    val quantity: Int
)
