package com.example.md3.data.model.amc


import com.google.gson.annotations.SerializedName

data class ContractTemplateDetails(
    @SerializedName("category")
    val category: String,
    @SerializedName("category_details")
    val categoryDetails: CategoryDetails,
    @SerializedName("charges")
    val charges: List<Charge>,
    @SerializedName("contract_type")
    val contractType: String,
    @SerializedName("created")
    val created: String,
    @SerializedName("duration")
    val duration: Int,
    @SerializedName("flag_onsite_service")
    val flagOnsiteService: Boolean,
    @SerializedName("id")
    val id: String,
    @SerializedName("modified")
    val modified: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("organisation")
    val organisation: String,
    @SerializedName("parts")
    val parts: List<Part>,
    @SerializedName("product_age")
    val productAge: Int,
    @SerializedName("products")
    val products: List<Product>,
    @SerializedName("services")
    val services: List<Service>,
    @SerializedName("total_cost")
    val totalCost: Double
)