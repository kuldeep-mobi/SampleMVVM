package com.mobikasa.samplemvvm.ui

import android.util.Log
import androidx.activity.viewModels
import com.mobikasa.samplemvvm.viewmodels.MainViewModel
import com.mobikasa.samplemvvm.R
import com.mobikasa.samplemvvm.base.BaseActivity
import com.mobikasa.samplemvvm.databinding.ActivityDetailsBinding

class DetailsActivity : BaseActivity<ActivityDetailsBinding>(R.layout.activity_details) {

    private val mainViewModel: MainViewModel by viewModels()

    override fun onActivityCreated(dataBinder: ActivityDetailsBinding) {
        val fragment = supportFragmentManager.findFragmentByTag("TAG")
        if (fragment == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.frameContainer, HomeFragment(), "TAG")
                .commit()
        }

        val fragment2 = supportFragmentManager.findFragmentByTag("MyTAG")
        if (fragment2 == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.frameContainer2, MyFragment(), "TAG")
                .commit()
        }
        mainViewModel.liveData.observe(this) {
            Log.d("TAG", it)
        }
    }

    override fun onRetryClick(apiName: String) {
    }

}

