package sgtmelon.scriptum.domain.model.item

import org.junit.Assert.*
import org.junit.Test
import sgtmelon.extension.getTime
import sgtmelon.scriptum.domain.model.annotation.Color
import sgtmelon.scriptum.domain.model.data.DbData.Alarm
import sgtmelon.scriptum.domain.model.data.DbData.Note
import sgtmelon.scriptum.domain.model.key.Complete
import sgtmelon.scriptum.domain.model.key.NoteType
import sgtmelon.scriptum.extension.getText
import sgtmelon.scriptum.parent.ParentTest
import sgtmelon.scriptum.domain.model.item.NoteItem.Roll.Companion.INDICATOR_MAX_COUNT as MAX_COUNT

/**
 * Test for [NoteItem].
 */
class NoteItemTest : ParentTest() {

    //region Data

    private val rollList = arrayListOf(
        RollItem(position = 0, text = "1"),
        RollItem(position = 1, text = "2", isCheck = true),
        RollItem(position = 2, text = "3", isCheck = true)
    )

    private val textItem = NoteItem.Text(create = "12345", color = 0)
    private val rollItem = NoteItem.Roll(create = "12345", color = 0, list = rollList)

    private val splitList = listOf("1", "2", "34")

    private val nameSpace = "1  2  3   "
    private val nameClear = "1 2 3"

    private val splitText = "1\n\n2\n34\n"

    private val changeText = "TIME"

    private val copyText = "COPY_TEXT"
    private val realText = "REAL_TEXT"
    private val copyPosition = 9
    private val realPosition = 0

    private val checkCount = 2

    //endregion

    private fun assertChangeTime(noteItem: NoteItem) = assertEquals(getTime(), noteItem.change)

    @Test fun noteType() {
        assertEquals(textItem.type, NoteType.TEXT)
        assertEquals(rollItem.type, NoteType.ROLL)
    }

    @Test fun switchStatus() {
        assertTrue(textItem.deepCopy(isStatus = false).switchStatus().isStatus)
        assertFalse(textItem.deepCopy(isStatus = true).switchStatus().isStatus)
    }

    @Test fun updateTime() = assertChangeTime(rollItem.deepCopy(change = changeText).updateTime())

    @Test fun haveRank() {
        assertFalse(rollItem.deepCopy().haveRank())
        assertFalse(rollItem.deepCopy(rankId = 0).haveRank())
        assertFalse(rollItem.deepCopy(rankPs = 0).haveRank())
        assertTrue(rollItem.deepCopy(rankId = 0, rankPs = 0).haveRank())
    }

    @Test fun haveAlarm() {
        assertFalse(rollItem.deepCopy().haveAlarm())
        assertFalse(rollItem.deepCopy(alarmId = 1).haveAlarm())
        assertFalse(rollItem.deepCopy(alarmDate = "DATE").haveAlarm())
        assertTrue(rollItem.deepCopy(alarmId = 1, alarmDate = "DATE").haveAlarm())
    }

    @Test fun clearRank() {
        val item = rollItem.deepCopy(rankId = 0, rankPs = 0)

        assertTrue(item.haveRank())
        assertFalse(item.clearRank().haveRank())
    }

    @Test fun clearAlarm() {
        val item = rollItem.deepCopy(alarmId = 1, alarmDate = "123")

        assertTrue(item.haveAlarm())
        assertFalse(item.clearAlarm().haveAlarm())
    }

    @Test fun isRankVisible() {
        val idList = listOf<Long>(1, 2, 3)

        assertTrue(rollItem.deepCopy().isRankVisible(idList))
        assertTrue(rollItem.deepCopy(rankId = 0).isRankVisible(idList))
        assertTrue(rollItem.deepCopy(rankPs = 0).isRankVisible(idList))

        assertFalse(rollItem.deepCopy(rankId = 0, rankPs = 0).isRankVisible(idList))
        assertTrue(rollItem.deepCopy(rankId = 1, rankPs = 1).isRankVisible(idList))
    }


    @Test fun onDelete() {
        rollItem.deepCopy(change = changeText, isBin = false, isStatus = true).onDelete().let {
            assertChangeTime(it)
            assertTrue(it.isBin)
            assertFalse(it.isStatus)
        }
    }

    @Test fun onRestore() {
        rollItem.deepCopy(change = changeText, isBin = true).onRestore().let {
            assertChangeTime(it)
            assertFalse(it.isBin)
        }
    }

    //region TextNote

    @Test fun defaultValues_forText() {
        NoteItem.Text(color = Color.BLUE).apply {
            assertEquals(Note.Default.ID, id)
            assertEquals(getTime(), create)
            assertEquals(Note.Default.CHANGE, change)
            assertEquals(Note.Default.NAME, name)
            assertEquals(Note.Default.TEXT, text)
            assertEquals(Note.Default.RANK_ID, rankId)
            assertEquals(Note.Default.RANK_PS, rankPs)
            assertEquals(Note.Default.BIN, isBin)
            assertEquals(Note.Default.STATUS, isStatus)
            assertEquals(Alarm.Default.ID, alarmId)
            assertEquals(Alarm.Default.DATE, alarmDate)
        }
    }

    @Test fun isSaveEnabled_forText() {
        textItem.deepCopy().apply {
            assertFalse(isSaveEnabled())
            text = "123"
            assertTrue(isSaveEnabled())
        }
    }


    @Test fun deepCopy_forText() {
        val firstItem = textItem.deepCopy(text = realText)
        val secondItem = firstItem.deepCopy()

        assertEquals(firstItem, secondItem)

        firstItem.text = copyText
        assertEquals(realText, secondItem.text)
    }

    @Test fun splitText() {
        assertEquals(splitList, textItem.deepCopy(text = splitText).splitText())
    }


    @Test fun onSave_forText() {
        textItem.deepCopy(change = changeText, name = nameSpace).apply {
            onSave()

            assertEquals(nameClear, name)
            assertChangeTime(this)
        }
    }

    @Test fun onConvert_forText() {
        textItem.deepCopy(change = changeText, text = splitText).onConvert().apply {
            assertEquals(NoteType.ROLL, type)

            assertEquals(splitList.size, list.size)

            for ((i, text) in splitList.withIndex()) {
                assertEquals(i, list[i].position)
                assertEquals(text, list[i].text)
            }

            assertChangeTime(this)
            assertEquals("0/${splitList.size}", text)
        }
    }

    //endregion

    //region RollNote

    @Test fun defaultValues_forRoll() {
        NoteItem.Roll(color = Color.BLUE).apply {
            assertEquals(Note.Default.ID, id)
            assertEquals(getTime(), create)
            assertEquals(Note.Default.CHANGE, change)
            assertEquals(Note.Default.NAME, name)
            assertEquals(Note.Default.TEXT, text)
            assertEquals(Note.Default.RANK_ID, rankId)
            assertEquals(Note.Default.RANK_PS, rankPs)
            assertEquals(Note.Default.BIN, isBin)
            assertEquals(Note.Default.STATUS, isStatus)
            assertEquals(Alarm.Default.ID, alarmId)
            assertEquals(Alarm.Default.DATE, alarmDate)
            assertEquals(0, list.size)
        }
    }

    @Test fun isSaveEnabled_forRoll() {
        rollItem.deepCopy().apply {
            assertTrue(isSaveEnabled())

            for (it in list) it.text = ""
            assertFalse(isSaveEnabled())
        }
    }

    @Test fun deepCopy_forRoll() {
        val firstItem = rollItem.deepCopy(name = realText)
        val secondItem = firstItem.deepCopy()

        assertEquals(firstItem, secondItem)

        firstItem.name = copyText
        firstItem.list.first().position = copyPosition

        assertEquals(realText, secondItem.name)
        assertEquals(realPosition, secondItem.list.first().position)
    }


    @Test fun updateComplete() {
        rollItem.deepCopy().apply {
            assertEquals("0/${list.size}", updateComplete(Complete.EMPTY).text)
        }

        rollItem.deepCopy().apply {
            assertEquals("${list.size}/${list.size}", updateComplete(Complete.FULL).text)
        }

        rollItem.deepCopy().apply {
            assertEquals("${checkCount}/${list.size}", updateComplete().text)
        }

        rollItem.deepCopy().apply {
            with(list) { while (size != MAX_COUNT) add(first()) }
            assertEquals("${checkCount}/${MAX_COUNT}", updateComplete().text)

            with(list) { add(first()) }
            assertEquals("${checkCount}/${MAX_COUNT}", updateComplete().text)
        }

        rollItem.deepCopy().apply {
            with(list) {
                for (it in this) it.isCheck = true
                while (size != MAX_COUNT) add(random().copy(isCheck = true))
            }
            assertEquals("${MAX_COUNT}/${MAX_COUNT}", updateComplete().text)

            with(list) { add(random().copy(isCheck = true)) }
            assertEquals("${MAX_COUNT}/${MAX_COUNT}", updateComplete().text)
        }
    }

    @Test fun updateCheck() {
        rollItem.deepCopy().apply {
            updateCheck(isCheck = true)

            assertFalse(list.any { !it.isCheck })
            assertEquals("${list.size}/${list.size}", text)
        }

        rollItem.deepCopy().apply {
            updateCheck(isCheck = false)

            assertFalse(list.any { it.isCheck })
            assertEquals("0/${list.size}", text)
        }
    }

    @Test fun getCheck() = assertEquals(checkCount, rollItem.getCheck())


    @Test fun onItemCheck() {
        rollItem.deepCopy(change = changeText).apply {
            onItemCheck(list.indices.first)

            assertTrue(list.first().isCheck)
            assertChangeTime(this)
            assertEquals("3/3", text)
        }

        rollItem.deepCopy(change = changeText).apply {
            onItemCheck(p = 4)

            assertEquals(changeText, change)
        }
    }

    @Test fun onItemLongCheck() {
        rollItem.deepCopy(change = changeText).apply {
            assertTrue(onItemLongCheck())
            assertChangeTime(this)
            assertEquals("3/3", text)

            change = changeText

            assertFalse(onItemLongCheck())
            assertChangeTime(this)
            assertEquals("0/3", text)
        }
    }


    @Test fun onSave_forRoll() {
        rollItem.deepCopy(change = changeText, name = nameSpace).apply {
            list.add(RollItem(position = 6, text = "   "))
            list.add(RollItem(position = 10, text = "   4  "))

            updateComplete()

            assertEquals(5, list.size)
            assertEquals("2/5", text)

            onSave()

            assertEquals(4, list.size)
            assertEquals("2/4", text)

            for ((i, item) in list.withIndex()) {
                assertEquals(i, item.position)
            }

            assertEquals(nameClear, name)
            assertChangeTime(this)
        }
    }

    @Test fun onConvert_forRoll() {
        rollItem.deepCopy(change = changeText).onConvert().apply {
            assertEquals(NoteType.TEXT, type)

            assertChangeTime(this)
            assertEquals(rollList.getText(), text)
        }

        val list = rollList.subList(0, 2)
        rollItem.deepCopy(change = changeText, list = list).onConvert(list).apply {
            assertEquals(NoteType.TEXT, type)

            assertChangeTime(this)
            assertEquals(list.getText(), text)
        }
    }

    //endregion

}