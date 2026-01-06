package com.codenicely.gimbook.saudi.einvoice.data.di


import com.example.md3.data.preferences.SharedPrefs
import com.example.md3.utils.glide.GlideImageLoader
import org.koin.dsl.module

/**
 * Use the [persistenceModule] to creating shared preference instance
 **/
val persistenceModule = module {

    /**
     * Singleton for shared preference
     **/

    single { SharedPrefs(get()) }

    single { GlideImageLoader(get ()) }


}