package com.mobikasa.samplemvvm.ui

import android.content.Intent
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.mobikasa.samplemvvm.R
import com.mobikasa.samplemvvm.base.BaseActivity
import com.mobikasa.samplemvvm.databinding.ActivityMainBinding
import com.mobikasa.samplemvvm.utils.LocationObserver
import com.mobikasa.samplemvvm.utils.NetworkResponse
import com.mobikasa.samplemvvm.viewmodels.HomeViewModel
import com.vmadalin.easypermissions.EasyPermissions
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlin.time.ExperimentalTime

class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main),
    EasyPermissions.PermissionCallbacks {

    private val mViewModel: HomeViewModel by viewModels()
    private lateinit var mLocationObserver: LocationObserver
    private val mAdapter = MainAdapter(emptyList()) {
        startActivity(Intent(this, DetailsActivity::class.java))
    }

    @ExperimentalTime
    override fun onActivityCreated(dataBinder: ActivityMainBinding) {
        dataBinder.adapter = mAdapter
        dataBinder.homeViewModel = mViewModel
        dataBinder.lifecycleOwner = this
        mLocationObserver = LocationObserver(this, lifecycle) {
        }
        // mLocationObserver.enable()
        // lifecycle.addObserver(mLocationObserver)
//       val d =  measureTime {
//            lifecycleScope.launch {
//                funcOne()
//            }
//            lifecycleScope.launch {
//                funcTwo()
//            }
//        }
//        Log.d("TAG", " $d")

        lifecycleScope.launch {
            funcOne()
        }
        Log.d("TAG", "Kuldeep Kp")
        val handler = CoroutineExceptionHandler { ctx, thr ->
            {

            }
        }
        val scope = CoroutineScope(Job() + Dispatchers.Main + CoroutineName("ABC"))

        val name: (Int, Int) -> Int = { a, b -> a + b }

        runBlocking {

        }

        val list = listOf("A", "B", "C", "D", "E", "F", "G", "H")
        val itr = list.iterator()
        while (itr.hasNext()) {
            Log.d("TAG", "Value : ${itr.next()}")
        }
        lifecycleScope.launch {
            test().collect {

            }
        }


        mViewModel.getHomeData().observe(this) {
            when (it) {
                is NetworkResponse.ApiError -> showError()
                is NetworkResponse.Loading -> showProgress()
                is NetworkResponse.NetworkError -> showError()
                is NetworkResponse.Success -> {
                    hideProgress()
                    mAdapter.setData(it.body?.results)
                }
                is NetworkResponse.UnknownError -> showError()
            }
        }
    }


    fun test() = flow {
        emit("Kuldeep")
        emit("Sandeep")
        emit("Awadhesh")
        emit("Akhilesh")
    }

    override fun onRetryClick(apiName: String) {
        Log.d("TAG", "Retry Click")
    }

    override fun onPermissionsDenied(requestCode: Int, perms: List<String>) {
    }

    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {
        mLocationObserver.enable()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    suspend fun funcOne() {
        // delay(1000)
        for (i in 1..10) {
            Log.d("TAG", "FuncOne $i")
            delay(100)
        }
        Log.d("TAG", "TaskOne Completed")
    }

    suspend fun funcTwo() {
        for (i in 1..10) {
            Log.d("TAG", "FuncTwo $i")
            delay(500)
        }
        Log.d("TAG", "TaskTwo Completed")
    }
}