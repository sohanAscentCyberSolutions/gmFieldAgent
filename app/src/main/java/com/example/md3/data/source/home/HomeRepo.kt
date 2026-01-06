package com.example.md3.data.source.home

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.example.md3.data.model.home.RandomResult
import com.example.md3.data.model.user_profile.UserDetails
import com.example.md3.data.network.home.HomeApi
import com.example.md3.data.preferences.SharedPrefs
import com.example.md3.utils.network.Resource
import com.example.md3.utils.network.ResponseHandler
import org.koin.java.KoinJavaComponent
import retrofit2.Response

class HomeRepo(private val api : HomeApi, private val responseHandler: ResponseHandler) {

    private val sharedPrefs: SharedPrefs by KoinJavaComponent.inject<SharedPrefs>(SharedPrefs::class.java)


    suspend fun getUserProfile(): Resource<UserDetails> {
        return try {
            responseHandler.handleSuccess(api.requestForGetUserProfile(sharedPrefs.organisationId))
        }catch (e : Exception){
            responseHandler.handleException(e)
        }
    }

}