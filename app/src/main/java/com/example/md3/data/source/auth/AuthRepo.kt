package com.example.md3.data.source.auth

import android.util.Log
import com.example.md3.data.model.auth.AuthData
import com.example.md3.data.model.auth.OtpResponse
import com.example.md3.data.model.auth.RefreshTokenResponse
import com.example.md3.data.model.user_profile.UserDetails
import com.example.md3.data.network.auth.AuthApi
import com.example.md3.data.preferences.SharedPrefs
import com.example.md3.utils.network.Resource
import com.example.md3.utils.network.ResponseHandler
import org.koin.java.KoinJavaComponent

class AuthRepo(private val api : AuthApi, private val responseHandler: ResponseHandler) {

    private  val TAG = "AuthRepo"


    private val sharedPrefs: SharedPrefs by KoinJavaComponent.inject<SharedPrefs>(SharedPrefs::class.java)


    suspend fun getUserProfile(): Resource<UserDetails> {
        return try {
            responseHandler.handleSuccess(api.requestForGetUserProfile(sharedPrefs.organisationId))
        }catch (e : Exception){
            responseHandler.handleException(e)
        }
    }

    suspend fun loginWithEmailAndPassword(email : String , password :  String) : Resource<AuthData> {
         return try {
             responseHandler.handleSuccess(api.loginWithEmailAndPassword(email, password))
        }catch (e : Exception){
             Log.d(TAG, "loginWithEmailAndPassword: " + e.localizedMessage )
            responseHandler.handleException(e)
        }
    }


    suspend fun refreshToken(refreshToken: String): Resource<RefreshTokenResponse> {
        return try {
            responseHandler.handleSuccess(api.refreshAccessToken(refreshToken))
        } catch (e: Exception) {
            responseHandler.handleException(e)
        }
    }

    suspend fun loginWithOtp(username: String): Resource<OtpResponse> {
        return try {
            responseHandler.handleSuccess(api.loginWithOtp(username))
        } catch (e: Exception) {
            responseHandler.handleException(e)
        }
    }


    suspend fun verifyOtp(otp: String, otpToken: String): Resource<AuthData> {
        return try {
            responseHandler.handleSuccess(api.verifyOtp(otp, otpToken))
        } catch (e: Exception) {
            responseHandler.handleException(e)
        }
    }

}
