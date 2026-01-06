package com.example.md3.data.model.commissioning.Details


import com.example.md3.data.model.commissioning.CustomerAddress
import com.example.md3.data.model.commissioning.CustomerDetails
import com.example.md3.data.model.commissioning.TypeDetails
import com.example.md3.data.model.commissioning.visit.Kpi
import com.google.gson.annotations.SerializedName

data class CommissioningDetailsResponse(
//    @SerializedName("case_id")
//    val caseId: String,
//    @SerializedName("closing_date")
//    val closingDate: String,
//    @SerializedName("created")
//    val created: String,
//    @SerializedName("custom_fields")
//    val customFields: List<CustomField>,
//    @SerializedName("customer")
//    val customer: String,
//    @SerializedName("customer_address")
//    val customerAddress: CustomerAddress,
//    @SerializedName("customer_details")
//    val customerDetails: CustomerDetails,
//    @SerializedName("description")
//    val description: String,
//    @SerializedName("flag_create_new_visit")
//    val flagCreateNewVisit: Boolean,
//    @SerializedName("id")
//    val id: String,
//    @SerializedName("internal_case_id")
//    val internalCaseId: String,
//    @SerializedName("is_converted_to_installed_base")
//    val isConvertedToInstalledBase: Boolean,
//    @SerializedName("kpi")
//    val kpi: Kpi,
//    @SerializedName("modified")
//    val modified: String,
//    @SerializedName("name")
//    val name: String,
//    @SerializedName("organisation")
//    val organisation: String,
//    @SerializedName("planned_date")
//    val plannedDate: String,
//    @SerializedName("prefix")
//    val prefix: String,
//    @SerializedName("product")
//    val product: Product,
//    @SerializedName("sequence_number")
//    val sequenceNumber: Int,
//    @SerializedName("status")
//    val status: String,
//    @SerializedName("type")
//    val type: String,
//    @SerializedName("commissioning_type_details")
//    val typeDetails: TypeDetails,


    @SerializedName("id") val id: String,
    @SerializedName("customer_details") val customerDetails: CustomerDetails,
    @SerializedName("commissioning_type_details") val commissioningTypeDetails: TypeDetails,
    @SerializedName("customer_address") val customerAddress: CustomerAddress,
    @SerializedName("name") val name: String,
    @SerializedName("flag_all_visit_completed") val flagAllVisitCompleted: Boolean,
    @SerializedName("kpi") val kpi: Kpi,
    @SerializedName("created") val created: String,
    @SerializedName("modified") val modified: String,
    @SerializedName("type") val type: String,
    @SerializedName("case_id") val caseId: String,
    @SerializedName("status") val status: String,
    @SerializedName("description") val description: String,
    @SerializedName("planned_date") val plannedDate: String,
    @SerializedName("closing_date") val closingDate: String?,
    @SerializedName("prefix") val prefix: String,
    @SerializedName("sequence_number") val sequenceNumber: Int,
    @SerializedName("internal_case_id") val internalCaseId: String,
    @SerializedName("flag_create_new_visit") val flagCreateNewVisit: Boolean,
    @SerializedName("product_name") val productName: String,
    @SerializedName("product_model_number") val productModelNumber: String,
    @SerializedName("product_brand") val productBrand: String,
    @SerializedName("product_serial_number") val productSerialNumber: String,
    @SerializedName("commissioning_type") val commissioningType: String,
    @SerializedName("organisation") val organisation: String,
    @SerializedName("customer") val customer: String,
    @SerializedName("product") val product: String


)