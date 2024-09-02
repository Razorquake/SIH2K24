package com.razorquake.sih2k24.data.local

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@ProvidedTypeConverter
class LocalDateTimeConvertor {
    private val formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)
    @TypeConverter
    fun toLocalDateTime(value: String?): LocalDateTime? {
        return value?.let { LocalDateTime.parse(it, formatter) }
    }
    @TypeConverter
    fun fromLocalDateTime(date: LocalDateTime?): String? {
        return date?.format(formatter)
    }
}