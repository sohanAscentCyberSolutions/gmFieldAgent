import android.util.Log
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.example.md3.BuildConfig
import com.example.md3.Urls
import com.example.md3.baseApplication.BaseApplication.Companion.context
import com.example.md3.data.network.auth.AuthApi
import com.example.md3.data.network.auth.CommissioningApi
import com.example.md3.data.network.home.HomeApi
import com.example.md3.data.preferences.SharedPrefs
import com.example.md3.utils.network.ResponseHandler
import com.example.md3.utils.tokenAuthenticator.TokenAuthenticator
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


/**
 * Use the [apiModule] to creating Retrofit2 api service
 * for WikiEduDashboardApi
 *
 * @return retrofit.create(WikiEduDashboardApi::class.java) */
val apiModule = module {

    factory { AuthInterceptor(get()) }
    factory { NetworkInterceptor(get()) }
    factory { TokenAuthenticator() }
    factory { provideOkHttpClient(get(), get()) }
    single { provideBaseRetrofit(get()) }
    single { ResponseHandler() }


    single { provideBaseRetrofit(get()).create(HomeApi::class.java) }
    single { provideBaseRetrofit(get()).create(AuthApi::class.java) }
    single { provideBaseRetrofit(get()).create(CommissioningApi::class.java) }


}

/**
 * Use the [providerGSON] to provide GSON
 * @return GSONBuilder*/
fun providerGSON(): Gson =
    GsonBuilder()
        .setLenient()
        .serializeNulls()
        .create()

/**
 * Use the [provideInterceptor] to provide a HttpLoggingInterceptor
 * @return HttpLoggingInterceptor*/
fun provideInterceptor(): HttpLoggingInterceptor {
    val interceptor = HttpLoggingInterceptor()
    interceptor.level = HttpLoggingInterceptor.Level.BODY

    return interceptor
}

class AuthInterceptorWithNoCompanyId(sharedPrefs: SharedPrefs) : Interceptor {
    private var sharedPrefsLocal: SharedPrefs = sharedPrefs
    override fun intercept(chain: Interceptor.Chain): Response {
        var req = chain.request()
        var url = req.url

        val accessToken = sharedPrefsLocal.accessToken

        val otpToken = sharedPrefsLocal.otpToken
        if (accessToken != "") {
            Log.d("tokenAya", accessToken)
            req = req.newBuilder()
                .header("Authorization", "Bearer $accessToken")
                .url(url).build()
        }
        val response = chain.proceed(req)
        try {
//            Timber.d("response: %s", response.peekBody(2048).string())
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return response

    }

}

//class AuthInterceptor(sharedPrefs: SharedPrefs) : Interceptor {
//
//    private var sharedPrefsLocal: SharedPrefs = sharedPrefs
//
//    override fun intercept(chain: Interceptor.Chain): Response {
//        var req = chain.request()
//        var url = req.url
//
//
//
//        Log.d("urlAttacked1", url.toString());
//
//
//        val accessToken = sharedPrefsLocal.accessToken
//
//        val otpToken = sharedPrefsLocal.otpToken
//        val companyId = ""
//
//
////        if(url.toString() != "https://sandbox.veri5digital.com/video-id-kyc/api/1.0/docInfoExtract"){
//        Log.d("urlAttacked2", url.toString());
//
//
//        if (accessToken != "") {
//            Log.d("tokenAya", accessToken)
//            if (!url.toString().contains("auth"))
//                req = req.newBuilder()
//                    .header("Authorization", "Bearer $accessToken")
//                    .url(url).build()
//
//        }
//
//
//        val response = chain.proceed(req)
//
//
//
//        if (response.code == 401) {
//            req = req.newBuilder()
//                    .header("token", "$accessToken")
//                    .url(url).build()
//
//        }
//        try {
////            Timber.d("response: %s", response.peekBody(2048).string())
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//        return response
//    }
//}



class AuthInterceptor(sharedPrefs: SharedPrefs) : Interceptor {

    private var sharedPrefsLocal: SharedPrefs = sharedPrefs

    override fun intercept(chain: Interceptor.Chain): Response {
        var req = chain.request()
        var url = req.url

        try {
            Log.d("urlAttacked1", url.toString())

            val accessToken = sharedPrefsLocal.accessToken

            val otpToken = sharedPrefsLocal.otpToken
            val companyId = ""

            // Check if the request URL does not contain "auth"
            if (!url.toString().contains("auth")) {
                Log.d("urlAttacked2", url.toString())
                if (accessToken != "") {
                    Log.d("tokenAya", accessToken)
                    req = req.newBuilder()
                        .header("Authorization", "Bearer $accessToken")
                        .url(url)
                        .build()
                }
            }

            val response = chain.proceed(req)

            // Check if response code is 401
//            if (response.code == 401) {
//                req = req.newBuilder()
//                    .header("token", "$accessToken")
//                    .url(url)
//                    .build()
//            }

            return response
        } catch (e: Exception) {
            e.printStackTrace()
            throw e // Re-throw the exception to propagate it
        }
    }
}




class NetworkInterceptor(sharedPrefs: SharedPrefs) : Interceptor {

    private var sharedPrefsLocal: SharedPrefs = sharedPrefs
    override fun intercept(chain: Interceptor.Chain): Response {

        val response = chain.proceed(chain.request());
        val otpToken = response.headers.get("otp-token")

        val accessToken = response.headers.get("access-token")

        if (otpToken != null) {
            sharedPrefsLocal.otpToken = otpToken
        }

        if (accessToken != null) {


            sharedPrefsLocal.accessToken = accessToken

        }


        return response
    }


}


/**
 * Use the [provide Client] to provide a OkHttpClient
 * @return OkHttpClient*/


fun provideOkHttpClient(
    authInterceptor: AuthInterceptor,
    networkInterceptor: NetworkInterceptor,
): OkHttpClient {
    return if (BuildConfig.DEBUG) {
        OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(
                ChuckerInterceptor.Builder(context!!)
                    .collector(ChuckerCollector(context!!))
                    .maxContentLength(250000L)
                    .redactHeaders(emptySet())
                    .alwaysReadResponseBody(false)
                    .build()
            )
            .addInterceptor(provideInterceptor())
            .addInterceptor(networkInterceptor)
            .connectTimeout(5, TimeUnit.MINUTES)
            .readTimeout(5, TimeUnit.MINUTES)
            .authenticator(TokenAuthenticator())
            .build()
    } else {
        OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(provideInterceptor())
            .addInterceptor(networkInterceptor)
            .connectTimeout(5, TimeUnit.MINUTES)
            .readTimeout(5, TimeUnit.MINUTES)
            .authenticator(TokenAuthenticator())
            .build()
    }
}


/**
 * Use the [provideBaseRetrofit] to provide a Retrofit with WITH_BASE_URL instance
 * @return Retrofit*/
fun provideBaseRetrofit(okHttpClient: OkHttpClient): Retrofit =
    Retrofit.Builder()
        .baseUrl(Urls.BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create(providerGSON()))
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build()



