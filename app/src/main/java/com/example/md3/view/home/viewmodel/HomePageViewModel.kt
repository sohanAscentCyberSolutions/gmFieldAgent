package com.example.md3.view.home.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.md3.data.model.auth.OtpResponse
import com.example.md3.data.model.user_profile.UserDetails
import com.example.md3.data.source.home.HomePageRepo
import com.example.md3.data.source.home.HomeRepo
import com.example.md3.utils.network.Resource
import kotlinx.coroutines.launch

class HomePageViewModel(private val homeRepo: HomePageRepo) : ViewModel() {


}