package com.mobikasa.samplemvvm.viewmodels

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mobikasa.samplemvvm.utils.SingleLiveEvent

class VPViewModel : ViewModel() {
    private val singleLiveEvent = SingleLiveEvent<TYPE>()
    val mData: LiveData<TYPE>
        get() = singleLiveEvent

    private val uriLiveData = MutableLiveData<Uri>()
    val liveDataURI: LiveData<Uri>
        get() = uriLiveData


    fun updateImageURI(uri: Uri?) {
        uri?.let {
            uriLiveData.value = it
        }
    }

    fun updateEvent(type: TYPE) {
        singleLiveEvent.setValue(type)
    }

    enum class TYPE {
        CAMERAPREVIEW,
        PHOTOVIEW
    }
}