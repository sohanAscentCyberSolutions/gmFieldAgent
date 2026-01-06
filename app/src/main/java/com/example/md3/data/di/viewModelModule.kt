package com.example.md3.data.di;




import com.example.md3.view.commissioning.CommissioningViewModel
import com.example.md3.view.auth.viewmodel.AuthViewModel
import com.example.md3.view.breakdown.BreakdownViewModel
import com.example.md3.view.home.viewmodel.HomeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * Use the [viewModelModule] to creating viewModel instance
 **/

val viewModelModule = module {
    viewModel { HomeViewModel(get()) }
    viewModel { AuthViewModel(get()) }
    viewModel { CommissioningViewModel(get()) }
    viewModel { BreakdownViewModel(get()) }
}