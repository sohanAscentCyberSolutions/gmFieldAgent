package com.example.md3.data.model.visit


import com.example.md3.data.model.commissioning.AssignedEngineerDetails
import com.example.md3.data.model.commissioning.ServiceRequestDetails
import com.google.gson.annotations.SerializedName

data class CreateVisitResponse(
    @SerializedName("assigned_engineer")
    val assignedEngineer: String,
    @SerializedName("assigned_engineer_details")
    val assignedEngineerDetails: AssignedEngineerDetails,
    @SerializedName("commissioning")
    val commissioning: String,
    @SerializedName("service_request_details")
    val serviceRequestDetails: ServiceRequestDetails,
    @SerializedName("commissioning_status")
    val commissioningStatus: String,
    @SerializedName("created")
    val created: String,
    @SerializedName("end_scheduled_work_date")
    val endScheduledWorkDate: Any,
    @SerializedName("end_scheduled_work_time")
    val endScheduledWorkTime: Any,
    @SerializedName("explain_status")
    val explainStatus: Any,
    @SerializedName("id")
    val id: String,
    @SerializedName("is_engineer_known")
    val isEngineerKnown: Boolean,
    @SerializedName("is_next_visit_required")
    val isNextVisitRequired: Boolean,
    @SerializedName("modified")
    val modified: String,
    @SerializedName("next_visit_date")
    val nextVisitDate: Any,
    @SerializedName("next_visit_time")
    val nextVisitTime: Any,
    @SerializedName("organisation")
    val organisation: String,
    @SerializedName("remark")
    val remark: String,
    @SerializedName("route")
    val route: List<Any>,
    @SerializedName("scheduled_work_date")
    val scheduledWorkDate: Any,
    @SerializedName("scheduled_work_time")
    val scheduledWorkTime: Any,
    @SerializedName("status")
    val status: String,
    @SerializedName("summary")
    val summary: Any,
    @SerializedName("time_elapsed")
    val timeElapsed: TimeElapsed,
    @SerializedName("type")
    val type: Any,
    @SerializedName("visit_address")
    val visitAddress: VisitAddress,
    @SerializedName("visit_count")
    val visitCount: Int
)