package com.mobikasa.samplemvvm.ui

import android.util.Log
import androidx.fragment.app.activityViewModels
import com.mobikasa.samplemvvm.R
import com.mobikasa.samplemvvm.base.BaseFragment
import com.mobikasa.samplemvvm.databinding.FragmentMyBinding
import com.mobikasa.samplemvvm.viewmodels.MainViewModel


class MyFragment : BaseFragment<FragmentMyBinding>() {
    private val mainViewModel: MainViewModel by activityViewModels()
    override fun getLayoutId() = R.layout.fragment_my

    override fun initView() {
        val binding = getDataBinding()
        binding.viewModel = mainViewModel
        mainViewModel.singleLiveEvent.observe(this,{
            Log.d("TAG","Message My Fragment $it")
        })
    }
}