package com.mobikasa.samplemvvm.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.mobikasa.samplemvvm.CommonDataModel
import com.mobikasa.samplemvvm.base.BaseViewModel
import com.mobikasa.samplemvvm.data.ApiServiceResponse
import com.mobikasa.samplemvvm.data.HomeRepository
import com.mobikasa.samplemvvm.data.ServiceResponse
import com.mobikasa.samplemvvm.sealed.*
import com.mobikasa.samplemvvm.utils.APIError
import com.mobikasa.samplemvvm.utils.GsonManagerUtils
import com.mobikasa.samplemvvm.utils.NetworkResponse
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class HomeViewModel : BaseViewModel() {

    private val homeRepository = HomeRepository()


    private val mLocalData = MutableLiveData<DataResource<ServiceResponse>>()
    val mSharedData: LiveData<DataResource<ServiceResponse>> = mLocalData

    private val mCommonData = MutableLiveData<DataResource<CommonDataModel>>()
    val mCommonShareData: LiveData<DataResource<CommonDataModel>> = mCommonData

    private val test = MutableLiveData<NetworkResponse<ApiServiceResponse, APIError>>()
    val mTest: LiveData<NetworkResponse<ApiServiceResponse, APIError>>
        get() = test


    fun getHomeData() = liveData {
        emit(NetworkResponse.Loading(null))
        delay(1000)
        emit(homeRepository.getPopular())
    }

    private fun fetchCommonData() {
        viewModelScope.launch {
            mCommonData.value = Loading(1)
            try {
                val response = homeRepository.getCommonData()
                if (response?.code == 200) {
                    val commonData =
                        GsonManagerUtils.fromJson(GsonManagerUtils.toJson(response.data),
                            CommonDataModel::class.java)
                    mCommonData.value = Success(data = commonData)
                } else {
                    mCommonData.value =
                        HttpErrorCodes(responseCode = response?.code)
                }
            } catch (exp: HttpException) {
                mCommonData.value = Error(exp.message())
            } catch (io: IOException) {
                mCommonData.value = Error(io.message)
            }
        }
    }


    private fun fetchDataWithStatus() {
        viewModelScope.launch {
            try {
                mLocalData.postValue(Loading(1))
                val data = homeRepository.getData(81)
                delay(1000)
                mLocalData.postValue(Success(data = data))
            } catch (exp: HttpException) {
                mLocalData.postValue(Error("Something went wrong"))
            } catch (io: IOException) {
                mLocalData.postValue(Error(io.message))
            }
        }
    }

}