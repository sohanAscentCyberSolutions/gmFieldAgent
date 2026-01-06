package com.example.md3.data.model.amc


import com.google.gson.annotations.SerializedName

data class LinkedContractDetails(
    @SerializedName("charges")
    val charges: List<Charge>,
    @SerializedName("contract_template")
    val contractTemplate: String,
    @SerializedName("contract_template_details")
    val contractTemplateDetails: ContractTemplateDetails,
    @SerializedName("contract_type")
    val contractType: String,
    @SerializedName("created")
    val created: String,
    @SerializedName("customer")
    val customer: String,
    @SerializedName("customer_details")
    val customerDetails: CustomerDetails,
    @SerializedName("customer_product")
    val customerProduct: String,
    @SerializedName("customer_product_details")
    val customerProductDetails: CustomerProductDetails,
    @SerializedName("duration")
    val duration: Int,
    @SerializedName("end_date")
    val endDate: String,
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
    val parts: List<PartX>,
    @SerializedName("product_age")
    val productAge: Int,
    @SerializedName("services")
    val services: List<ServiceX>,
    @SerializedName("start_date")
    val startDate: String,
    @SerializedName("total_cost")
    val totalCost: Double
)