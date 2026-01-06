package com.example.md3.data.model.auth

import com.google.gson.annotations.SerializedName


data class AuthData(
    @SerializedName("access_token") val accessToken: String,
    @SerializedName("refresh_token") val refreshToken: String,
    @SerializedName("organisation_list") val organisationList: List<Organisation>
)
