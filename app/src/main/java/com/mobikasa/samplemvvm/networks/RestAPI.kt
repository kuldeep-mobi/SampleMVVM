package com.mobikasa.samplemvvm.networks

import com.mobikasa.samplemvvm.data.ApiServiceResponse
import com.mobikasa.samplemvvm.data.ServiceResponse
import com.mobikasa.samplemvvm.utils.NetworkResponse
import com.mobikasa.samplemvvm.utils.APIError
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface RestAPI {
    @GET("popular")
    suspend fun getAllMovies(
        @Query("page") page: Int?,
        @Query("api_key") api_key: String = "5370eed075b93dd79855f1c429b03ad8",
    ): ServiceResponse?

    @GET("popular")
    suspend fun getMovies(
        @Query("page") page: Long,
        @Query("api_key") api_key: String = "5370eed075b93dd79855f1c429b03ad8",
    ): Response<ServiceResponse>

    @GET("commonData")
    suspend fun getCommonData(): ApiServiceResponse?

    @GET("commonData")
    suspend fun getData(): NetworkResponse<ApiServiceResponse, APIError>

    @GET("getQuestionnaire")
    suspend fun getQuestion(): NetworkResponse<ApiServiceResponse, APIError>

    @GET("popular")
    suspend fun getMoviesPopular(
        @Query("page") page: Long,
        @Query("api_key") api_key: String = "5370eed075b93dd79855f1c429b03ad8",
    ): NetworkResponse<ServiceResponse, APIError>


}