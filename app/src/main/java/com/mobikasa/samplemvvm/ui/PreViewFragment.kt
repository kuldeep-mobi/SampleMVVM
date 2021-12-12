package com.mobikasa.samplemvvm.ui

import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.mobikasa.samplemvvm.R
import com.mobikasa.samplemvvm.base.BaseFragment
import com.mobikasa.samplemvvm.databinding.FragementPreviewBinding
import com.mobikasa.samplemvvm.viewmodels.VPViewModel

class PreViewFragment : BaseFragment<FragementPreviewBinding>() {
    private val vpViewModel: VPViewModel by activityViewModels()
    override fun getLayoutId() = R.layout.fragement_preview

    override fun initView() {
        val binding = getDataBinding()
        vpViewModel.liveDataURI.observe(this, {
            Glide.with(requireActivity()).load(it).into(binding.imageView)
        })
    }
}