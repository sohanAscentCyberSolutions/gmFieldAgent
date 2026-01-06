package com.example.md3.data.model.home


import com.google.gson.annotations.SerializedName

data class CombinedDetails(
    @SerializedName("accepted_case")
    val acceptedCase: Int,
    @SerializedName("closed_case")
    val closedCase: Int,
    @SerializedName("completed_visits")
    val completedVisits: Int,
    @SerializedName("computed_journey_hours")
    val computedJourneyHours: Double,
    @SerializedName("computed_work_hours")
    val computedWorkHours: Double,
    @SerializedName("missed_visits")
    val missedVisits: Int,
    @SerializedName("new_case")
    val newCase: Int,
    @SerializedName("ongoing_visits")
    val ongoingVisits: Int,
    @SerializedName("rejected_case")
    val rejectedCase: Int,
    @SerializedName("total_journey_hours")
    val totalJourneyHours: Int,
    @SerializedName("total_journey_minutes")
    val totalJourneyMinutes: Int,
    @SerializedName("total_journey_time_string")
    val totalJourneyTimeString: String,
    @SerializedName("total_visits")
    val totalVisits: Int,
    @SerializedName("total_work_hours")
    val totalWorkHours: Int,
    @SerializedName("total_work_minutes")
    val totalWorkMinutes: Int,
    @SerializedName("total_work_time_string")
    val totalWorkTimeString: String,
    @SerializedName("upcoming_visits")
    val upcomingVisits: Int
)