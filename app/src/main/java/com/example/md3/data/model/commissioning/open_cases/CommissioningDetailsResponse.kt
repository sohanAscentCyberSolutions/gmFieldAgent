package com.example.md3.data.model.commissioning.open_cases


import com.example.md3.data.model.commissioning.ServiceRequestDetails
import com.google.gson.annotations.SerializedName

data class CommissioningDetailsResponse(
    @SerializedName("accepted_rejected_on")
    val acceptedRejectedOn: Any,
    @SerializedName("assigned_engineer")
    val assignedEngineer: String,
    @SerializedName("assigned_engineer_details")
    val assignedEngineerDetails: AssignedEngineerDetails,
    @SerializedName("commissioning")
    val commissioning: String,
    @SerializedName("service_request_details")
    val serviceRequestDetails: ServiceRequestDetails,
    @SerializedName("created")
    val created: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("modified")
    val modified: String,
    @SerializedName("reject_reasons")
    val rejectReasons: List<String>,
    @SerializedName("status")
    val status: String,
    @SerializedName("total_visits")
    val totalVisits: List<Any>
)