package com.example.md3.data.model.commissioning.visit


import com.example.md3.data.model.commissioning.ServiceRequestDetails
import com.example.md3.data.model.visit.TimeElapsed
import com.google.gson.annotations.SerializedName

data class VisitResult(

    // previously

    @SerializedName("assigned_engineer_details")
    val assignedEngineerDetails: AssignedEngineerDetails?,
    @SerializedName("commissioning")
    val commissioning: String,
    @SerializedName("service_request_details")
    val servicerequestdetails: ServiceRequestDetails,
    @SerializedName("commissioning_status")
    val commissioningStatus: String,
    @SerializedName("end_scheduled_work_date")
    val endScheduledWorkDate: String,
    @SerializedName("end_scheduled_work_time")
    val endScheduledWorkTime: String,
    @SerializedName("explain_status")
    val explainStatus: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("is_engineer_known")
    val isEngineerKnown: Boolean,
    @SerializedName("is_next_visit_required")
    val isNextVisitRequired: Boolean,
    @SerializedName("next_visit_date")
    val nextVisitDate: String,
    @SerializedName("next_visit_time")
    val nextVisitTime: String,
    @SerializedName("remark")
    val remark: String,
    @SerializedName("scheduled_work_date")
    val scheduledWorkDate: String,
    @SerializedName("scheduled_work_time")
    val scheduledWorkTime: String,
    @SerializedName("status")
    val status: String,
    @SerializedName("summary")
    val summary: String?,
    @SerializedName("visit_address")
    val visitAddress: VisitAddress,
    @SerializedName("visit_count")
    val visitCount: Int,
    @SerializedName("time_elapsed")
    val timeElapsed: TimeElapsed,
    @SerializedName("latest_journey_details")
    val latestJourneyDetails: LatestJourneyDetails?,


    // now

//    @SerializedName("id") val id: String,
//    @SerializedName("visit_address") val visitAddress: VisitAddress,
//    @SerializedName("assigned_engineer_details") val assignedEngineerDetails: EngineerDetails,
//    @SerializedName("time_elapsed") val timeElapsed: TimeElapsed,
//    @SerializedName("latest_journey_details") val latestJourneyDetails: LatestJourneyDetails?, // Replace Any with the appropriate type
//    @SerializedName("status") val status: String,
//    @SerializedName("explain_status") val explainStatus: String?, // Change type accordingly
//    @SerializedName("remark") val remark: String,
//    @SerializedName("scheduled_work_date") val scheduledWorkDate: String,
//    @SerializedName("scheduled_work_time") val scheduledWorkTime: String,
//    @SerializedName("end_scheduled_work_date") val endScheduledWorkDate: String?, // Change type accordingly
//    @SerializedName("end_scheduled_work_time") val endScheduledWorkTime: String?, // Change type accordingly
//    @SerializedName("is_next_visit_required") val isNextVisitRequired: Boolean,
//    @SerializedName("is_engineer_known") val isEngineerKnown: Boolean,
//    @SerializedName("next_visit_date") val nextVisitDate: String?, // Change type accordingly
//    @SerializedName("next_visit_time") val nextVisitTime: String?, // Change type accordingly
//    @SerializedName("visit_commissioning_status") val visitCommissioningStatus : String?, // Replace Any with the appropriate type
//    @SerializedName("summary") val summary: String?, // Replace Any with the appropriate type
//    @SerializedName("visit_count") val visitCount: Int,
//    @SerializedName("service_request") val serviceRequest: String

)