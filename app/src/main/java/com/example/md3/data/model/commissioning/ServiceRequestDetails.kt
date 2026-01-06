package com.example.md3.data.model.commissioning


import com.example.md3.data.model.visit.TimeElapsed
import com.google.gson.annotations.SerializedName


data class ServiceRequestDetails(
    @SerializedName("id")
    val id: String,
    @SerializedName("customer_details")
    val customerDetails: CustomerDetails,
    @SerializedName("name")
    val name: String,
    @SerializedName("flag_all_visit_completed")
    val flagAllVisitCompleted: Boolean,
    @SerializedName("case_id")
    val caseId: String,
    @SerializedName("status")
    val status: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("planned_date")
    val plannedDate: String, // Assuming this is a date, change type accordingly
    @SerializedName("closing_date")
    val closingDate: String?, // Assuming this is a date, change type accordingly
    @SerializedName("prefix")
    val prefix: String,
    @SerializedName("sequence_number")
    val sequenceNumber: Int,
    @SerializedName("internal_case_id")
    val internalCaseId: String,
    @SerializedName("flag_create_new_visit")
    val flagCreateNewVisit: Boolean,
    @SerializedName("product_name")
    val productName: String,
    @SerializedName("product_model_number")
    val productModelNumber: String,
    @SerializedName("product_brand")
    val productBrand: String,
    @SerializedName("product_serial_number")
    val productSerialNumber: String,
    @SerializedName("commissioning_type")
    val commissioningType: Any?, // Replace Any with the appropriate type
    @SerializedName("customer")
    val customer: String,
    @SerializedName("product")
    val product: String,
    @SerializedName("time_elapsed")
    val timeElapsed: TimeElapsed?,
    @SerializedName("commissioning_type_details")
    val commissioningTypeDetails: TypeDetails?,
    @SerializedName("customer_address")
    val customerAddress: CustomerAddress,
)