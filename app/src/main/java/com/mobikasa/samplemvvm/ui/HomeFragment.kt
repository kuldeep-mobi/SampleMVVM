package com.mobikasa.samplemvvm.ui

import android.util.Log
import androidx.fragment.app.activityViewModels
import com.mobikasa.samplemvvm.viewmodels.MainViewModel
import com.mobikasa.samplemvvm.R
import com.mobikasa.samplemvvm.base.BaseFragment
import com.mobikasa.samplemvvm.databinding.FragmentHomeBinding

class HomeFragment : BaseFragment<FragmentHomeBinding>() {

    private val mainViewModel: MainViewModel by activityViewModels()

    override fun getLayoutId(): Int {
        return R.layout.fragment_home
    }

    override fun initView() {
        val binding = getDataBinding()
        binding.mainViewModel = mainViewModel
        mainViewModel.singleLiveEvent.observe(this,{
            Log.d("TAG","Message Home Fragment $it")
        })
    }
}