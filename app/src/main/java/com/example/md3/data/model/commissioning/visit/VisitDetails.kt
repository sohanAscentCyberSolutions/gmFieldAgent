package com.example.md3.data.model.commissioning.visit


import com.example.md3.data.model.visit.TimeElapsed
import com.google.gson.annotations.SerializedName

data class VisitDetails(
    @SerializedName("assigned_engineer")
    val assignedEngineer: String,
    @SerializedName("assigned_engineer_details")
    val assignedEngineerDetails: AssignedEngineerDetails,
    @SerializedName("commissioning")
    val commissioning: String,
    @SerializedName("service_request_details")
    val commissioningDetails: CommissioningDetails,
    @SerializedName("commissioning_status")
    val commissioningStatus: String,
    @SerializedName("created")
    val created: String,
    @SerializedName("end_scheduled_work_date")
    val endScheduledWorkDate: String,
    @SerializedName("end_scheduled_work_time")
    val endScheduledWorkTime: String,
    @SerializedName("explain_status")
    val explainStatus: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("is_engineer_known")
    val isEngineerKnown: String,
    @SerializedName("is_next_visit_required")
    val isNextVisitRequired: String,
    @SerializedName("latest_journey_details")
    val latestJourneyDetails: LatestJourneyDetails,
    @SerializedName("modified")
    val modified: String,
    @SerializedName("next_visit_date")
    val nextVisitDate: String,
    @SerializedName("next_visit_time")
    val nextVisitTime: String,
    @SerializedName("organisation")
    val organisation: String,
    @SerializedName("remark")
    val remark: String,
    @SerializedName("route")
    val route: List<Any>,
    @SerializedName("scheduled_work_date")
    val scheduledWorkDate: String,
    @SerializedName("scheduled_work_time")
    val scheduledWorkTime: String,
    @SerializedName("status")
    val status: String,
    @SerializedName("summary")
    val summary: String,
    @SerializedName("time_elapsed")
    val timeElapsed: TimeElapsed,
    @SerializedName("type")
    val type: String,
    @SerializedName("visit_address")
    val visitAddress: VisitAddress,
    @SerializedName("visit_count")
    val visitCount: String
)