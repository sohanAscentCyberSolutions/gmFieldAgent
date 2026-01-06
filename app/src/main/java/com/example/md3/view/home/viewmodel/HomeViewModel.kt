package com.example.md3.view.home.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.md3.data.model.auth.OtpResponse
import com.example.md3.data.model.user_profile.UserDetails
import com.example.md3.data.source.home.HomeRepo
import com.example.md3.utils.network.Resource
import kotlinx.coroutines.launch

class HomeViewModel(private val homeRepo: HomeRepo) : ViewModel() {

    private val _userProfileMutableLiveData = MutableLiveData<Resource<UserDetails>>()
    val getUserProfileLiveData: LiveData<Resource<UserDetails>>
        get() = _userProfileMutableLiveData


    fun getUserProfile() {
        _userProfileMutableLiveData.postValue(Resource.loading(null))
        viewModelScope.launch {
            _userProfileMutableLiveData.postValue(homeRepo.getUserProfile())
        }
    }
}