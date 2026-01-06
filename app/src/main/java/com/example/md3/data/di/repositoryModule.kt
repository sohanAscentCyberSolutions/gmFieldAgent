package com.example.md3.data.di;

import com.example.md3.data.source.auth.AuthRepo
import com.example.md3.data.source.home.BreakdownRepo
import com.example.md3.data.source.home.CommissioningRepo
import com.example.md3.data.source.home.HomeRepo
import org.koin.dsl.module


/**
 * Use the [repositoryModule] to creating repository instance
 **/
val repositoryModule = module {


    single { HomeRepo(get(),get()) }
    single { AuthRepo(get(),get()) }
    single { CommissioningRepo(get(),get()) }
    single { BreakdownRepo(get(),get()) }

}