package com.mobikasa.samplemvvm.rooms

import androidx.room.TypeConverter
import java.util.*

class Converters {

    @TypeConverter
    fun fromDateToLong(obj: Date): Long {
        return obj.time
    }

    @TypeConverter
    fun fromLongToDate(value: Long): Date {
        return Date(value)
    }
}