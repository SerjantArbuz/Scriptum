package sgtmelon.scriptum.room.converter.type

import org.junit.Assert.*
import org.junit.Test

/**
 * Test for [BoolConverter]
 */
class BoolConverterTest {

    private val converter = BoolConverter()

    @Test fun toInt() {
        assertEquals(0, converter.toInt(false) )
        assertEquals(1, converter.toInt(true) )
    }

    @Test fun toBool() {
        assertTrue(converter.toBool(1))
        assertFalse(converter.toBool(0))
        assertFalse(converter.toBool(-1))
    }

}