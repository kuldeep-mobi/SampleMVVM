package com.mobikasa.samplemvvm.utils

import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GsonManagerUtils {

    companion object {
        suspend fun toJson(data: Any?): String {
            var jsonString: String
            withContext(Dispatchers.Default) {
                jsonString = Gson().toJson(data)
            }
            return jsonString
        }

        suspend fun <T> fromJson(data: String, obj: Class<T>): T {
            var mData: T
            withContext(Dispatchers.Default) {
                mData = Gson().fromJson(data, obj)
            }
            return mData
        }
    }
}