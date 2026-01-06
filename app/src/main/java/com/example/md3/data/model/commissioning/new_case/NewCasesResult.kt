package com.example.md3.data.model.commissioning.new_case

import com.example.md3.data.model.commissioning.AssignedEngineerDetails
import com.example.md3.data.model.commissioning.ServiceRequestDetails
import com.example.md3.data.model.commissioning.TotalVisit
import com.google.gson.annotations.SerializedName


data class NewCasesResult(
//    @SerializedName("accepted_rejected_on")
//    val acceptedRejectedOn: String,
//    @SerializedName("assigned_engineer")
//    val assignedEngineer: String,
//    @SerializedName("assigned_engineer_details")
//    val assignedEngineerDetails: AssignedEngineerDetails,
//    @SerializedName("service_request")
//    val commissioning: String,
//    @SerializedName("service_request_details")
//    val commissioningDetails: CommissioningDetails,
//    @SerializedName("id")
//    val id: String,
//    @SerializedName("reject_reasons")
//    val rejectReasons: List<String>,
//    @SerializedName("status")
//    val status: String,
//    @SerializedName("total_visits")


    @SerializedName("id")
    val id: String,
    @SerializedName("assigned_engineer_details")
    val assignedEngineerDetails: AssignedEngineerDetails,
    @SerializedName("total_visits")
    val totalVisits: List<TotalVisit>,
    @SerializedName("service_request_details")
    val serviceRequestDetails: ServiceRequestDetails,
    @SerializedName("status")
    val status: String,
    @SerializedName("accepted_rejected_on")
    val rejectReasons: List<String>,
    @SerializedName("service_request")
    val serviceRequest: String,
    @SerializedName("assigned_engineer")
    val assignedEngineer: String


)