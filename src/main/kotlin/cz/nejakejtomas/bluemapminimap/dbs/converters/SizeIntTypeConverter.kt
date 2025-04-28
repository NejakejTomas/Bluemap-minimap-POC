package cz.nejakejtomas.bluemapminimap.dbs.converters

import androidx.room.TypeConverter
import cz.nejakejtomas.bluemapminimap.common.Size

class SizeIntTypeConverter {
    @TypeConverter
    fun fromSize(value: Size<Int>): Long {
        val foo = (value.width.toLong() shl 32) or value.height.toLong()

        return foo
    }

    @TypeConverter
    fun toSize(value: Long): Size<Int> {
        val height = value and 0xFFFFFFFF
        val width = value ushr 32

        return Size(width.toInt(), height.toInt())
    }
}