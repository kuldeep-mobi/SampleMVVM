package com.mobikasa.samplemvvm.data

import com.mobikasa.samplemvvm.data.Results

data class ServiceResponse(
    var results: List<Results>,
    val page: Int,
    val total_pages: Int,
    val total_results: Int
)
