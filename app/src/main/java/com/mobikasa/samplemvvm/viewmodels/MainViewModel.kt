package com.mobikasa.samplemvvm.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.mobikasa.samplemvvm.base.BaseViewModel
import com.mobikasa.samplemvvm.utils.SingleLiveEvent

class MainViewModel : BaseViewModel() {
    val liveData = MutableLiveData<String>()
    val singleLiveEvent = SingleLiveEvent<String>().apply {
        setValue("Hello World")
    }
    val rememberMe = MutableLiveData(true)
    val radio = MutableLiveData("Doctor")

    private var count = 0
    fun updateValue() {
        singleLiveEvent.setValue("Testing")
        liveData.value = "${count++}"
    }

    fun checkBoxStatus(status: Boolean) {
        Log.d("TAG", "Status $status")
        rememberMe.value = status
    }

    fun updateRadio(data: String) {
        radio.value = data
    }

}