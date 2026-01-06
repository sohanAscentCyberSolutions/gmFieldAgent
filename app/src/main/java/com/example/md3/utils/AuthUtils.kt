package com.example.md3.utils

import android.content.Context
import com.example.md3.data.model.auth.AuthData
import com.example.md3.data.model.user_profile.UserDetails
import com.example.md3.data.preferences.SharedPrefs

object AuthUtils {
    private lateinit var sharedPrefs: SharedPrefs

    fun init(context: Context) {
        sharedPrefs = SharedPrefs(context)
    }

    fun setAuthTokensAndAuthData(authData: AuthData) {
        sharedPrefs.accessToken = authData.accessToken
        sharedPrefs.refreshToken = authData.refreshToken
        authData.organisationList[0].apply {
            sharedPrefs.organisationId = id
            sharedPrefs.isOwner = isOwner
            sharedPrefs.name = name
            sharedPrefs.logo = logo
            sharedPrefs.email = email
            sharedPrefs.phone = phone
            sharedPrefs.sector = sector
            sharedPrefs.brand = brand
            sharedPrefs.information = information
            sharedPrefs.owner = owner
        }
    }


    fun setOrganisationUserId(userDetails : UserDetails?){
        sharedPrefs.organisationUserId = userDetails?.id
        sharedPrefs.organisationUserName = userDetails?.name
        sharedPrefs.organisationUserContact = userDetails?.phone

    }


    fun clearAuthDate(){
        sharedPrefs.clearPrefs()
    }
}
