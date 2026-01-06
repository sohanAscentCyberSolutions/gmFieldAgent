package com.example.md3.data.network.auth

import com.example.md3.Urls
import com.example.md3.data.model.auth.AuthData
import com.example.md3.data.model.auth.OtpResponse
import com.example.md3.data.model.auth.RefreshTokenResponse
import com.example.md3.data.model.user_profile.UserDetails
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface AuthApi  {

    @FormUrlEncoded
    @POST(Urls.REFRESH_TOKEN)
    suspend fun refreshAccessToken(@Field("refresh_token") refreshToken : String?) : RefreshTokenResponse


    @FormUrlEncoded
    @POST(Urls.LOGIN_WITH_EMAIL)
    suspend fun loginWithEmailAndPassword(@Field("email") email : String , @Field("password") password : String) : AuthData


    @FormUrlEncoded
    @POST(Urls.LOGIN_WITH_OTP)
    suspend fun loginWithOtp(@Field("username") username : String) : OtpResponse

    @FormUrlEncoded
    @POST(Urls.VERIFY_OTP)
    suspend fun verifyOtp(@Field("otp") otp : String , @Field("otp_token") otpToken : String) : AuthData


    @GET(Urls.GET_USER_PROFILE)
    suspend fun requestForGetUserProfile(
        @Query("organisation_id") organisationID : String,
    ): UserDetails







}