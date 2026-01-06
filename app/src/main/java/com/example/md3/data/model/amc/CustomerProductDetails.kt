package com.example.md3.data.model.amc


import com.google.gson.annotations.SerializedName

data class CustomerProductDetails(
    @SerializedName("batch_number")
    val batchNumber: Any,
    @SerializedName("created")
    val created: String,
    @SerializedName("customer")
    val customer: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("identifier")
    val identifier: String,
    @SerializedName("invoice_date")
    val invoiceDate: String,
    @SerializedName("modified")
    val modified: String,
    @SerializedName("organisation_product")
    val organisationProduct: String,
    @SerializedName("product_age")
    val productAge: String,
    @SerializedName("product_details")
    val productDetails: ProductDetailsXX,
    @SerializedName("quantity")
    val quantity: Double,
    @SerializedName("serial_number")
    val serialNumber: String
)