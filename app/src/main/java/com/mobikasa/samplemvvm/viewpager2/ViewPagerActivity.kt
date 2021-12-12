package com.mobikasa.samplemvvm.viewpager2

import android.Manifest
import android.content.pm.PackageManager
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.mobikasa.samplemvvm.R
import com.mobikasa.samplemvvm.base.BaseActivity
import com.mobikasa.samplemvvm.databinding.ActivityViewPagerBinding
import com.mobikasa.samplemvvm.ui.CameraFragment
import com.mobikasa.samplemvvm.ui.PreViewFragment
import com.mobikasa.samplemvvm.viewmodels.VPViewModel

class ViewPagerActivity : BaseActivity<ActivityViewPagerBinding>(R.layout.activity_view_pager) {

    private lateinit var mBinding: ActivityViewPagerBinding
    private val vpViewModel: VPViewModel by viewModels()

    override fun onActivityCreated(dataBinder: ActivityViewPagerBinding) {
        mBinding = dataBinder
        mBinding.cameraCaptureButton.setOnClickListener {
            if (allPermissionsGranted()) {
               // startCamera()
                vpViewModel.updateEvent(VPViewModel.TYPE.CAMERAPREVIEW)
            } else {
                ActivityCompat.requestPermissions(this,
                    REQUIRED_PERMISSIONS,
                    REQUEST_CODE_PERMISSIONS)
            }
        }
        vpViewModel.mData.observe(this, {
            when (it) {
                VPViewModel.TYPE.CAMERAPREVIEW -> {
                    startCamera()
                }
                VPViewModel.TYPE.PHOTOVIEW -> {
                    startPreviewFragment()
                }
            }
        })

    }

    private fun startCamera() {
        val fragment = supportFragmentManager.findFragmentByTag("CAMERA")
        val ft = supportFragmentManager.beginTransaction()
        mBinding.cameraCaptureButton.showHideView(false)
        if (fragment != null) {
            ft.replace(R.id.container, fragment, "CAMERA").addToBackStack("").commit()
        } else {
            ft.replace(R.id.container, CameraFragment(), "CAMERA").addToBackStack("").commit()
        }
    }
    private fun startPreviewFragment(){
        val fragment = supportFragmentManager.findFragmentByTag("PREVIEW")
        val ft = supportFragmentManager.beginTransaction()
        mBinding.cameraCaptureButton.showHideView(false)
        if (fragment != null) {
            ft.replace(R.id.container, fragment, "PREVIEW").addToBackStack("").commit()
        } else {
            ft.replace(R.id.container, PreViewFragment(), "PREVIEW").addToBackStack("").commit()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (supportFragmentManager.backStackEntryCount < 1) {
            mBinding.cameraCaptureButton.showHideView(true)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>,
        grantResults:
        IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                startCamera()
            } else {
                Toast.makeText(this, "Permissions not granted by the user.", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }


    override fun onRetryClick(apiName: String) {

    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    companion object {
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
    }

    private fun View.showHideView(isVisible: Boolean) {
        this.visibility = if (isVisible) View.VISIBLE else View.GONE
    }
}