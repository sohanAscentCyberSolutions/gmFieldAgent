package com.example.md3.utils.tokenAuthenticator;


import Status
import android.content.Intent
import android.util.Log
import com.example.md3.baseApplication.BaseApplication.Companion.context
import com.example.md3.data.model.auth.RefreshTokenResponse
import com.example.md3.data.preferences.SharedPrefs
import com.example.md3.data.source.auth.AuthRepo
import com.example.md3.utils.network.Resource
import com.example.md3.view.auth.AuthActivity
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import okio.Buffer
import okio.BufferedSource
import org.koin.java.KoinJavaComponent.inject
import java.io.IOException
import java.nio.charset.Charset

//class TokenAuthenticator() : Authenticator {
//
//    private val TAG = "TokenAuthenticator"
//
//    private val sharedPrefs: SharedPrefs by inject<SharedPrefs>(SharedPrefs::class.java)
//    private val responseHandler: ResponseHandler by inject(ResponseHandler::class.java)
//    private val tokenApi: AuthApi by inject(AuthApi::class.java)
//
//    override fun authenticate(route: Route?, response: Response): Request {
//        return runBlocking {
//            val tokenResponse = getRefreshTokenApi()
//            when (tokenResponse.status) {
//                Status.SUCCESS -> {
//                    val data = tokenResponse.data
//                    sharedPrefs.accessToken = data?.access_token
//                    sharedPrefs.refreshToken = data?.refresh_token
//                    response.request.newBuilder()
//                        .header("Authorization", "Bearer ${sharedPrefs.accessToken}")
//                        .build()
//                }
//
//                Status.LOADING -> {
//
//                }
//
//                Status.ERROR -> {
//                    // Redirect user to AuthActivity
//                    // You need to replace "context" with the context you have access to.
//                    val intent = Intent(context, AuthActivity::class.java)
//                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//                    context?.startActivity(intent)
//
//                    // Remove access token and refresh token
//                    sharedPrefs.accessToken = null
//                    sharedPrefs.refreshToken = null
//
//                    // Remove Authorization header
//                    response.request.newBuilder()
//                        .removeHeader("Authorization")
//                        .build()
//                }
//
//            }
//        } as Request
//
//    }
//
//
//    suspend fun getRefreshTokenApi(): Resource<RefreshTokenResponse> {
//        return try {
//            responseHandler.handleSuccess(tokenApi.refreshAccessToken(sharedPrefs.refreshToken))
//        } catch (e: Exception) {
//            responseHandler.handleException(e)
//        }
//    }
//}

class TokenAuthenticator : Authenticator {
    private val TAG = "TokenAuthenticator"
    private var responseBody: Resource<RefreshTokenResponse>? = null
    private val sharedPref: SharedPrefs by inject(SharedPrefs::class.java)
    private val refreshTokenRepository: AuthRepo by inject(AuthRepo::class.java)

    @Throws(IOException::class)
    override fun authenticate(route: Route?, response: Response): Request? {
        Log.d(TAG, "URL--- ${response.request}")
        Log.d(TAG, "URL---${response.code}")

        val final: String? = if (response.request.header("Authorization") != null) {
            val accessToke = response.request.header("Authorization")!!
            accessToke.removePrefix("Bearer ")
        } else {
            null
        }



        synchronized(this) {
            val errorMessage = getErrorCode(response)
            Log.d(TAG, "Error message ==${errorMessage}")
            if (errorMessage != null && errorMessage == "user_not_found") {
                sharedPref.clearPrefs()
                val intent = Intent(context!!, AuthActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                context!!.startActivity(intent)
                return null
            } else {
                return if (sharedPref.accessToken != null && final != sharedPref.accessToken) {
                    // extra case.
                    response.request.newBuilder()
                        .header("Authorization", "Bearer ${sharedPref.accessToken}").build()
                } else {
                    runBlocking {
                        launch {
                            if (sharedPref.refreshToken != null) {
                                responseBody =
                                    refreshTokenRepository.refreshToken(sharedPref.refreshToken!!)
                                Log.d(TAG, "authenticate:RefreshTokenResponse " + responseBody)
                            }
                        }
                    }
                    return if (sharedPref.refreshToken == null) {
                        null
                    } else if (responseBody?.data != null && responseBody?.data?.access_token != null) {
                        Log.d(TAG, "authenticate: " + responseBody?.data)
                        sharedPref.accessToken = responseBody?.data?.access_token!!
                        sharedPref.refreshToken = responseBody?.data?.refresh_token!!
                        val accessToken = responseBody?.data?.access_token!!
                        response.request.newBuilder()
                            .header("Authorization", "Bearer $accessToken")
                            .build()
                    } else {
                        if(responseBody?.status == Status.ERROR){
                            sharedPref.clearPrefs()
                            val intent = Intent(context!!, AuthActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            context!!.startActivity(intent)
                            null
                        }else{
                            null
                        }
                    }
                }
            }
        }
    }








    private fun getErrorCode(response: Response): String? {
        var errorMessage: String? = null
        var errorCode: Int? = null
        try {
            if (response.body != null) {
                val source: BufferedSource = response.body!!.source()
                source.request(Long.MAX_VALUE)
                val buffer: Buffer = source.buffer
                val charset: Charset =
                    response.body!!.contentType()!!.charset(Charset.forName("UTF-8"))!!
                val json: String = buffer.clone().readString(charset)
                val obj: JsonElement = JsonParser().parse(json)

                if (obj is JsonObject && obj.has("code")) {
                    errorMessage = obj.get("code").asString
                }
            }
        } catch (e: java.lang.Exception) {

        }
        // Check if status has an error code then throw and exception so retrofit can trigger the onFailure callback method.
        // Anything above 400 is treated as a server error.
        // Check if status has an error code then throw and exception so retrofit can trigger the onFailure callback method.
        // Anything above 400 is treated as a server error.

        return errorMessage
    }
}






