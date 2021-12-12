package com.mobikasa.samplemvvm.data

data class Results(
    val title: String,
    val id: Long,
    val poster_path: String?,
    var isFavorite: Boolean? = false
)
