package com.example.md3.data.model.user_profile


import com.google.gson.annotations.SerializedName

data class UserDetails(
    val id: String,
    @SerializedName("reports_to_detail")
    val reportsToDetail: ReportsToDetail,
    @SerializedName("assigned_location_details")
    val assignedLocationDetails: List<AssignedLocationDetail>,
    @SerializedName("assigned_department_details")
    val assignedDepartmentDetails: List<AssignedDepartmentDetail>,
    @SerializedName("groups_details")
    val groupsDetails: List<GroupDetail>,
    val created: String,
    val modified: String,
    val title: String,
    val designation: String,
    val name: String,
    val email: String,
    val phone: String,
    @SerializedName("is_admin")
    val isAdmin: Boolean,
    @SerializedName("is_engineer")
    val isEngineer: Boolean,
    val organisation: String,
    val user: String,
    @SerializedName("reports_to")
    val reportsTo: String,
    val groups: List<String>,
    val permissions: List<Permission>,
    @SerializedName("assigned_location")
    val assignedLocation: List<String>,
    @SerializedName("assigned_department")
    val assignedDepartment: List<String>
)

data class ReportsToDetail(
    val title: String,
    val designation: String,
    val name: String,
    val email: String
)

data class AssignedLocationDetail(
    val id: String,
    val name: String
)

data class AssignedDepartmentDetail(
    val id: String,
    val name: String
)

data class GroupDetail(
    val id: String,
    val name: String
)

data class Permission(
    val module: String,
    @SerializedName("is_view")
    val isView: Boolean,
    @SerializedName("is_create")
    val isCreate: Boolean,
    @SerializedName("is_edit")
    val isEdit: Boolean,
    @SerializedName("is_delete")
    val isDelete: Boolean
)
