package com.example.md3.data.model.commissioning.JourneyVisitRoute

import com.example.md3.data.model.commissioning.ServiceRequestDetails
import com.example.md3.data.model.commissioning.visit.LatestJourneyDetails
import com.example.md3.data.model.commissioning.visit.VisitAddress
import com.google.gson.annotations.SerializedName


data class VisitDetailsResponse(
    val message : String?,
    @SerializedName("id") val id: String,
    @SerializedName("route") val route: List<Route>,
    @SerializedName("visit_address") val visitAddress: VisitAddress,
    @SerializedName("assigned_engineer_details") val assignedEngineerDetails: EngineerDetails,
    @SerializedName("service_request_details") val serviceRequestDetails: ServiceRequestDetails,
    @SerializedName("created") val created: String,
    @SerializedName("modified") val modified: String,
    @SerializedName("status") val status: String,
    @SerializedName("explain_status") val explainStatus: String,
    @SerializedName("remark") val remark: String,
    @SerializedName("scheduled_work_date") val scheduledWorkDate: String,
    @SerializedName("scheduled_work_time") val scheduledWorkTime: String,
    @SerializedName("start_work_time") val startWorkTime: String?,
    @SerializedName("end_scheduled_work_date") val endScheduledWorkDate: String,
    @SerializedName("end_scheduled_work_time") val endScheduledWorkTime: String,
    @SerializedName("is_next_visit_required") val isNextVisitRequired: Boolean,
    @SerializedName("is_engineer_known") val isEngineerKnown: Boolean,
    @SerializedName("next_visit_date") val nextVisitDate: String?,
    @SerializedName("next_visit_time") val nextVisitTime: String?,
    @SerializedName("visit_commissioning_status") val commissioningStatus: String,
    @SerializedName("summary") val summary: String,
    @SerializedName("visit_count") val visitCount: Int,
    @SerializedName("organisation") val organisation: String,
    @SerializedName("type") val type: String?, // Replace 'Type' with the actual type
    @SerializedName("service_request") val commissioning: String,
    @SerializedName("assigned_engineer") val assignedEngineer: String,
    @SerializedName("latest_journey_details")
    val latestJourneyDetails: LatestJourneyDetails?,
)


data class EngineerDetails(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("email") val email: String?,
    @SerializedName("phone") val phone: String?
)

