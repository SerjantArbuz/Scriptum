package sgtmelon.scriptum.data.room.entity

import org.junit.Assert.assertEquals
import org.junit.Test
import sgtmelon.scriptum.ParentTest
import sgtmelon.scriptum.domain.model.data.DbData.RollVisible.Default

/**
 * Test for [RollVisibleEntity].
 */
class RollVisibleEntityTest : ParentTest() {

    @Test fun defaultValues() {
        with(RollVisibleEntity()) {
            assertEquals(Default.ID, id)
            assertEquals(Default.NOTE_ID, noteId)
            assertEquals(Default.VALUE, value)
        }
    }
}