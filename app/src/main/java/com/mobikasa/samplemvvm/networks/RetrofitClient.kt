package com.mobikasa.samplemvvm.networks

import com.mobikasa.samplemvvm.ConnectivityInterceptor
import com.mobikasa.samplemvvm.utils.CallAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitClient {

    companion object {
        private const val BASE_URL_2 = "https://allerdent.mobikasa.net/api/v1/"
        private const val BASE_URL = "https://api.themoviedb.org/3/movie/"
        fun getRestApi(): RestAPI {
            return Retrofit.Builder().baseUrl(BASE_URL)
                .addCallAdapterFactory(CallAdapterFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .client(OkHttpClient.Builder().addInterceptor { chain ->
                    val req = chain.request().newBuilder().addHeader("app_version", "1.0").build()
                    chain.proceed(req)
                }.addInterceptor(ConnectivityInterceptor()).build())
                .build().create(RestAPI::class.java)
        }
    }
}