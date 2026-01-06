package com.example.md3.data.model.visit


import com.google.gson.annotations.SerializedName

data class CreateVisitSubmitResponse(
    @SerializedName("assigned_engineer")
    val assignedEngineer: String,
    @SerializedName("service_request")
    val serviceRequest: String,
    @SerializedName("remark")
    val remark: String,
    @SerializedName("scheduled_work_date")
    val scheduledDate: String,
    @SerializedName("scheduled_work_time")
    val scheduledTime: String,
    @SerializedName("type")
    val type: String,
    @SerializedName("visit_address")
    val visitAddress: VisitAddress
)