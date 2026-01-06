package com.example.md3.data.model.commissioning.visit


import com.example.md3.data.model.commissioning.CustomerDetails
import com.example.md3.data.model.commissioning.Product
import com.example.md3.data.model.commissioning.TypeDetails
import com.google.gson.annotations.SerializedName

data class CommissioningDetails(
    @SerializedName("case_id")
    val caseId: String,
    @SerializedName("closing_date")
    val closingDate: String,
    @SerializedName("custom_fields")
    val customFields: List<CustomField>,
    @SerializedName("customer")
    val customer: String,
    @SerializedName("customer_details")
    val customerDetails: CustomerDetails,
    @SerializedName("description")
    val description: String,
    @SerializedName("flag_create_new_visit")
    val flagCreateNewVisit: Boolean,
    @SerializedName("id")
    val id: String,
    @SerializedName("internal_case_id")
    val internalCaseId: String,
    @SerializedName("is_converted_to_installed_base")
    val isConvertedToInstalledBase: Boolean,
    @SerializedName("kpi")
    val kpi: Kpi,
    @SerializedName("name")
    val name: String,
    @SerializedName("planned_date")
    val plannedDate: String,
    @SerializedName("prefix")
    val prefix: String,
    @SerializedName("product")
    val product: Product,
    @SerializedName("sequence_number")
    val sequenceNumber: Int,
    @SerializedName("status")
    val status: String,
    @SerializedName("type_details")
    val typeDetails: TypeDetails
)