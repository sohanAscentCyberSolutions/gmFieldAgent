package com.example.md3.data.network.home

import com.example.md3.Urls
import com.example.md3.data.model.home.RandomRes
import com.example.md3.data.model.user_profile.UserDetails
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface HomeApi  {

    @GET(Urls.GET_USER_PROFILE)
    suspend fun requestForGetUserProfile(
        @Query("organisation_id") organisationID : String,
    ): UserDetails






}