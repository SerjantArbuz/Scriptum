package sgtmelon.scriptum.data.room.converter.type

import androidx.annotation.IntRange
import androidx.room.TypeConverter

/**
 * Converter from number to boolean value and vice versa
 */
class BoolConverter {

    @TypeConverter fun toInt(value: Boolean) = if (value) trueValue else falseValue

    @TypeConverter fun toBool(@IntRange(from = 0, to = 1) value: Int) = value == trueValue

    private companion object {
        const val trueValue = 1
        const val falseValue = 0
    }

}