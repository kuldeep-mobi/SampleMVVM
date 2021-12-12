package com.mobikasa.samplemvvm.base

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Window
import android.view.WindowManager
import android.widget.ProgressBar
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.mobikasa.samplemvvm.databinding.CustomErrorLayoutBinding
import com.mobikasa.samplemvvm.databinding.CustomProgressBinding


abstract class BaseActivity<in T>(@LayoutRes private val layoutResId: Int?) :
    AppCompatActivity() where T : ViewDataBinding {

    private var progressDialog: Dialog? = null
    private var progressBar: ProgressBar? = null
    private var mErrorDialog: Dialog? = null
    abstract fun onActivityCreated(dataBinder: T)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initProgress()
        initErrorLayout()
        initial()
    }

    private fun initial() {
        layoutResId?.let { layoutId ->
            val dataBinder = DataBindingUtil.setContentView<T>(this, layoutId)
            this.onActivityCreated(dataBinder)
        }
    }

    private fun initProgress() {
        val binding = CustomProgressBinding.inflate(LayoutInflater.from(this))
        progressDialog = Dialog(this)
        progressDialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        progressDialog?.setContentView(binding.root)
        progressDialog?.setCancelable(false)
        progressDialog?.window!!.setBackgroundDrawable(
            ColorDrawable(Color.TRANSPARENT)
        )
        progressDialog?.window!!.setDimAmount(0.0f)
        progressBar = binding.progressBar
    }

    private fun initErrorLayout() {
        val binding = CustomErrorLayoutBinding.inflate(LayoutInflater.from(this))
        mErrorDialog = Dialog(this)
        mErrorDialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        mErrorDialog?.setContentView(binding.root)
        mErrorDialog?.setCancelable(false)
        mErrorDialog?.window!!.setBackgroundDrawable(
            ColorDrawable(Color.TRANSPARENT)
        )
        binding.retry.setOnClickListener {
            mErrorDialog?.dismiss()
            onRetryClick("ABC")
        }
        val layoutParams = WindowManager.LayoutParams()
        layoutParams.copyFrom(mErrorDialog?.window?.attributes)
        layoutParams.width = (resources.displayMetrics.widthPixels / 1.2).toInt()
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT
        mErrorDialog?.window?.attributes = layoutParams
    }

    override fun onStop() {
        super.onStop()
        hideError()
        progressBar = null
        mErrorDialog = null
    }


    protected fun showError() {
        hideProgress()
        mErrorDialog?.show()
    }

    private fun hideError() {
        hideProgress()
        mErrorDialog?.dismiss()
    }

    protected fun showProgress() {
        progressDialog?.show()
    }

    protected fun hideProgress() {
        progressDialog?.dismiss()
    }

    protected abstract fun onRetryClick(apiName: String)
}
