package com.mobikasa.samplemvvm.data

import com.mobikasa.samplemvvm.networks.RetrofitClient
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow

class HomeRepository {
    private val api = RetrofitClient.getRestApi()

    suspend fun getData(page: Int?) = api.getAllMovies(page)
    suspend fun getCommonData() = api.getCommonData()

    suspend fun getData() = api.getData()
    suspend fun getPopular() = api.getMoviesPopular(1)

    val list = ArrayList<String>().apply {
        add("ABC")
        add("ABC")
        add("ABC")
        add("ABC")
    }

    init {
        test(list)
        test1(list)
    }


    private fun <T> test(live: ArrayList<out T>) {

    }

    private fun <T> test1(live: ArrayList<in T>) {
    }


    val latestMovie = flow {
        while (true) {
            val api = api.getMoviesPopular(1)
            emit(api)
            delay(5000)
        }
    }
}
