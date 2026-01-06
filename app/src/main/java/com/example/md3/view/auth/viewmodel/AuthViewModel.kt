package com.example.md3.view.auth.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.md3.data.model.auth.AuthData
import com.example.md3.data.model.auth.OtpResponse
import com.example.md3.data.model.auth.RefreshTokenResponse
import com.example.md3.data.model.user_profile.UserDetails
import com.example.md3.data.source.auth.AuthRepo
import com.example.md3.utils.network.Resource
import kotlinx.coroutines.launch


class AuthViewModel(private val authRepo: AuthRepo) : ViewModel() {

    private val _loginResult = MutableLiveData<Resource<AuthData>>()
    val loginResult: LiveData<Resource<AuthData>>
        get() = _loginResult

    private val _otpLoginResult = MutableLiveData<Resource<OtpResponse>>()
    val getOtpLoginResult: LiveData<Resource<OtpResponse>>
        get() = _otpLoginResult

    private val _verifyOtpResult = MutableLiveData<Resource<AuthData>>()
    val verifyOtpResult: LiveData<Resource<AuthData>>
        get() = _verifyOtpResult

    private val _refreshTokenResult = MutableLiveData<Resource<RefreshTokenResponse>>()
    val refreshTokenResult: LiveData<Resource<RefreshTokenResponse>>
        get() = _refreshTokenResult

    fun loginWithEmailAndPassword(email: String, password: String) {
        _loginResult.postValue(Resource.loading(null))
        viewModelScope.launch {
            _loginResult.postValue(authRepo.loginWithEmailAndPassword(email, password))
        }
    }

    fun loginWithOtp(username: String) {
        _otpLoginResult.postValue(Resource.loading(null))
        viewModelScope.launch {
            _otpLoginResult.postValue(authRepo.loginWithOtp(username))
        }
    }

    fun verifyOtp(otp: String, otpToken: String) {
        _verifyOtpResult.postValue(Resource.loading(null))
        viewModelScope.launch {
            _verifyOtpResult.postValue(authRepo.verifyOtp(otp, otpToken))
        }
    }

    fun refreshToken(refreshToken: String) {
        _refreshTokenResult.postValue(Resource.loading(null))
        viewModelScope.launch {
            _refreshTokenResult.postValue(authRepo.refreshToken(refreshToken))
        }
    }



    private val _userProfileMutableLiveData = MutableLiveData<Resource<UserDetails>>()
    val getUserProfileLiveData: LiveData<Resource<UserDetails>>
        get() = _userProfileMutableLiveData


    fun getUserProfile() {
        _userProfileMutableLiveData.postValue(Resource.loading(null))
        viewModelScope.launch {
            _userProfileMutableLiveData.postValue(authRepo.getUserProfile())
        }
    }
}


