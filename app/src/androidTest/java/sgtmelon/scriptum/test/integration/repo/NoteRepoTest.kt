package sgtmelon.scriptum.test.integration.repo

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.runner.RunWith
import sgtmelon.scriptum.data.repository.room.NoteRepo
import sgtmelon.scriptum.test.ParentIntegrationTest

/**
 * Integration test for [NoteRepo]
 */
@RunWith(AndroidJUnit4::class)
class NoteRepoTest : ParentIntegrationTest()  {

    // TODO make test only related with dao
    // TODO nullable tests

//    private val badNoteRepo: INoteRepo = NoteRepo(context = null)
//    private val noteRepo: INoteRepo = NoteRepo(context)
//
//    private val noteConverter = NoteConverter()
//    private val rollConverter = RollConverter()
//
//    @Test fun getCount() = inRoomTest {
//        TODO("nullable")
//
//        assertEquals(0, badNoteRepo.getCount(bin = false))
//        assertEquals(0, badNoteRepo.getCount(bin = true))
//
//        assertEquals(0, noteRepo.getCount(bin = false))
//        assertEquals(0, noteRepo.getCount(bin = true))
//
//        noteDao.insert(firstNote)
//        noteDao.insert(secondNote)
//        noteDao.insert(thirdNote)
//        noteDao.insert(fourthNote)
//
//        rankDao.insert(firstRank)
//        rankDao.insert(secondRank)
//
//        assertEquals(2, noteRepo.getCount(bin = false))
//        assertEquals(2, noteRepo.getCount(bin = true))
//
//        rankDao.update(firstRank.copy(isVisible = false))
//
//        assertEquals(1, noteRepo.getCount(bin = false))
//    }
//
//    @Test fun getList() = inRoomTest {
//        TODO()
//
//        noteRepo.getList(0, true, false, true)
//    }
//
//    @Test fun getItem() = inRoomTest {
//        TODO("nullable")
//
//        val rollList = rollConverter.toItem(firstRollList)
//        val noteItem = noteConverter.toItem(firstNote, rollList, firstAlarm)
//
//        val rollPreviewList = rollList.subList(0, NoteItem.Roll.PREVIEW_SIZE)
//        val notePreviewItem = noteConverter.toItem(firstNote, rollPreviewList, firstAlarm)
//
//        assertNull(badNoteRepo.getItem(noteItem.id, optimal = true))
//        assertNull(noteRepo.getItem(noteItem.id, optimal = true))
//
//        noteDao.insert(firstNote)
//        firstRollList.forEach { rollDao.insert(it) }
//        alarmDao.insert(firstAlarm)
//
//        assertEquals(noteItem, noteRepo.getItem(noteItem.id, optimal = false))
//        assertEquals(notePreviewItem, noteRepo.getItem(noteItem.id, optimal = true))
//    }
//
//    @Test fun getRollList() = inRoomTest {
//        TODO("nullable")
//
//        assertEquals(listOf<RankItem>(), noteRepo.getRollList(Random.nextLong()))
//
//        noteDao.insert(firstNote)
//        noteDao.insert(fourthNote)
//        firstRollList.forEach { rollDao.insert(it) }
//        fourthRollList.forEach { rollDao.insert(it) }
//
//        assertEquals(rollConverter.toItem(firstRollList), noteRepo.getRollList(firstNote.id))
//        assertEquals(rollConverter.toItem(fourthRollList), noteRepo.getRollList(fourthNote.id))
//    }
//
//
//    @Test fun isListHide() = inRoomTest {
//        TODO("nullable")
//
//        assertEquals(false, badNoteRepo.isListHide())
//
//        rankDao.insert(firstRank)
//        rankDao.insert(secondRank.copy(isVisible = false))
//
//        noteDao.insert(firstNote)
//        assertEquals(false, noteRepo.isListHide())
//
//        noteDao.insert(fourthNote)
//        assertEquals(false, noteRepo.isListHide())
//    }
//
//    @Test fun clearBin() = inRoomTest {
//        TODO("nullable")
//
//        noteDao.insert(firstNote)
//        noteDao.insert(secondNote)
//        noteDao.insert(thirdNote)
//
//        rankDao.insert(firstRank)
//        rankDao.insert(secondRank)
//
//        val itemSecond = noteConverter.toItem(secondNote)
//        val itemThird = noteConverter.toItem(thirdNote)
//
//        noteRepo.clearBin()
//
//        assertEquals(firstNote, noteDao.get(firstNote.id))
//
//        assertEquals(firstRank.copy(noteId = arrayListOf(4)), rankDao.get(firstRank.id))
//        assertNull(noteDao.get(itemSecond.id))
//
//        assertEquals(secondRank.copy(noteId = arrayListOf()), rankDao.get(secondRank.id))
//        assertNull(noteDao.get(itemThird.id))
//    }
//
//
//    @Test fun deleteNote() = inRoomTest {
//        TODO("nullable")
//
//        noteDao.insert(firstNote)
//        alarmDao.insert(firstAlarm)
//
//        val item = noteConverter.toItem(firstNote)
//
//        noteRepo.deleteNote(item)
//
//        assertChangeTime(item)
//        assertTrue(item.isBin)
//        assertFalse(item.isStatus)
//
//        assertEquals(noteConverter.toEntity(item), noteDao.get(item.id))
//        assertNull(alarmDao.get(item.id))
//    }
//
//    @Test fun restoreNote() = inRoomTest {
//        TODO("nullable")
//
//        noteDao.insert(secondNote)
//
//        val item = noteConverter.toItem(secondNote)
//
//        noteRepo.restoreNote(item)
//
//        assertChangeTime(item)
//        assertFalse(item.isBin)
//
//        assertEquals(noteConverter.toEntity(item), noteDao.get(item.id))
//    }
//
//    @Test fun clearNote() = inRoomTest {
//        TODO("nullable")
//
//        noteDao.insert(secondNote)
//        rankDao.insert(firstRank)
//
//        val item = noteConverter.toItem(secondNote)
//
//        noteRepo.clearNote(item)
//
//        assertEquals(firstRank.copy(noteId = arrayListOf(4)), rankDao.get(firstRank.id))
//        assertNull(noteDao.get(item.id))
//    }
//
//    @Test fun convertToRoll() = inRoomTest {
//        TODO()
//
//        //badNoteRepo.convertNote()
////        noteRepo.convertNote(NoteItem(0, "", "", color = 0, type = NoteType.TEXT))
//    }
//
//    @Test fun convertToText() = inRoomTest {
//        TODO("nullable")
//
//        val rollList = rollConverter.toItem(firstRollList)
//        var noteItem = noteConverter.toItem(firstNote, rollList, firstAlarm)
//
//        if (noteItem !is NoteItem.Roll) throw NoteCastException()
//
//        noteDao.insert(firstNote)
//        firstRollList.forEach { rollDao.insert(it) }
//        alarmDao.insert(firstAlarm)
//
//        noteRepo.convertNote(noteItem.deepCopy(), useCache = false)
//        noteItem = noteItem.onConvert()
//
//        assertEquals(noteItem, noteRepo.getItem(noteItem.id, optimal = false))
//    }
//
//    @Test fun convertToTextUseCache() = inRoomTest {
//        TODO("nullable")
//
//        val rollList = rollConverter.toItem(firstRollList)
//        var noteItem = noteConverter.toItem(firstNote, rollList, firstAlarm)
//
//        if (noteItem !is NoteItem.Roll) throw NoteCastException()
//
//        noteDao.insert(firstNote)
//        firstRollList.forEach { rollDao.insert(it) }
//        alarmDao.insert(firstAlarm)
//
//        noteRepo.convertNote(noteItem.deepCopy(), useCache = true)
//        noteItem = noteItem.onConvert()
//
//        assertEquals(noteItem, noteRepo.getItem(noteItem.id, optimal = false))
//    }
//
//    @Test fun getCopyText() = inRoomTest {
//        TODO("nullable")
//
//        noteDao.insert(firstNote)
//        noteDao.insert(secondNote)
//        noteDao.insert(thirdNote)
//        noteDao.insert(fourthNote)
//
//        firstRollList.forEach { rollDao.insert(it) }
//        fourthRollList.forEach { rollDao.insert(it) }
//
//        val firstList = rollConverter.toItem(firstRollList)
//        val fourthList = rollConverter.toItem(fourthRollList)
//
//        val firstItem = noteConverter.toItem(firstNote, firstList)
//        val secondItem = noteConverter.toItem(secondNote)
//        val thirdItem = noteConverter.toItem(thirdNote)
//        val fourthItem = noteConverter.toItem(fourthNote, fourthList)
//
//        val firstText = "${firstNote.name}\n${firstList.getText()}"
//        val secondText = secondNote.text
//        val thirdText = with(thirdNote) { "$name\n$text" }
//        val fourthText = "${fourthNote.name}\n${fourthList.getText()}"
//
//        assertEquals(firstText, noteRepo.getCopyText(firstItem))
//        assertEquals(secondText, noteRepo.getCopyText(secondItem))
//        assertEquals(thirdText, noteRepo.getCopyText(thirdItem))
//        assertEquals(fourthText, noteRepo.getCopyText(fourthItem))
//    }
//
//    @Test fun saveTextNote() = inRoomTest {
//        TODO()
////        noteRepo.saveNote(NoteItem(0, "", "", color = 0, type = NoteType.TEXT), true)
//    }
//
//    @Test fun saveRollNote() = inRoomTest {
//        TODO()
////        noteRepo.saveNote(NoteItem(0, "", "", color = 0, type = NoteType.TEXT), true)
//    }
//
//    @Test fun updateRollCheckSingle() = inRoomTest {
//        TODO("nullable")
//
//        noteDao.insert(firstNote)
//        firstRollList.forEach { rollDao.insert(it) }
//
//        val list = rollConverter.toItem(firstRollList)
//        val item = noteConverter.toItem(firstNote, list) as? NoteItem.Roll
//                ?: throw NoteCastException()
//
//        item.onItemCheck(p = 0)
//
//        noteRepo.updateRollCheck(item, p = 0)
//
//        assertEquals(item, noteRepo.getItem(item.id, optimal = false))
//        assertEquals(list, noteRepo.getRollList(item.id))
//    }
//
//    @Test fun updateRollCheckAllFalse() = inRoomTest {
//        TODO("nullable")
//
//        noteDao.insert(firstNote)
//        firstRollList.forEach { rollDao.insert(it) }
//
//        val list = rollConverter.toItem(firstRollList)
//        val item = noteConverter.toItem(firstNote, list) as? NoteItem.Roll
//                ?: throw NoteCastException()
//
//        list.forEach { it.isCheck = true }
//
//        val check = item.onItemLongCheck()
//
//        noteRepo.updateRollCheck(item, check)
//
//        assertEquals(item, noteRepo.getItem(item.id, optimal = false))
//        assertEquals(list, noteRepo.getRollList(item.id))
//    }
//
//    @Test fun updateRollCheckAllTrue() = inRoomTest {
//        TODO("nullable")
//
//        noteDao.insert(fourthNote)
//        fourthRollList.forEach { rollDao.insert(it) }
//
//        val list = rollConverter.toItem(fourthRollList)
//        val item = noteConverter.toItem(fourthNote, list) as? NoteItem.Roll
//                ?: throw NoteCastException()
//
//        list.forEach { it.isCheck = true }
//        list.random().isCheck = false
//
//        val check = item.onItemLongCheck()
//
//        noteRepo.updateRollCheck(item, check)
//
//        assertEquals(item, noteRepo.getItem(item.id, optimal = false))
//        assertEquals(list, noteRepo.getRollList(item.id))
//    }
//
//    @Test fun updateNote() = inRoomTest {
//        TODO("nullable")
//
//        val entity = firstNote.copy()
//
//        noteDao.insert(entity)
//        noteRepo.updateNote(noteConverter.toItem(entity.apply {
//            create = DATE_1
//            change = DATE_2
//        }))
//
//        assertEquals(entity, noteDao.get(entity.id))
//    }
//
//
//    // TODO check data class`
//    @Test fun setRollVisible() = inRoomTest {
//        TODO("nullable")
//
//        val noteId = Random.nextLong()
//        var isVisible = Random.nextBoolean()
//
//        assertNull(rollVisibleDao.get(noteId))
//
//        noteRepo.setRollVisible(noteId, isVisible)
//        assertEquals(isVisible, rollVisibleDao.get(noteId))
//
//        isVisible = !isVisible
//
//        noteRepo.setRollVisible(noteId, isVisible)
//        assertEquals(isVisible, rollVisibleDao.get(noteId))
//    }
//
//    // TODO check data class
//    @Test fun getRollVisible() = inRoomTest {
//        TODO("nullable")
//
//        val noteId = Random.nextLong()
//        val isVisible = Random.nextBoolean()
//
//        assertNull(rollVisibleDao.get(noteId))
//        assertEquals(DbData.RollVisible.Default.VALUE, noteRepo.getRollVisible(noteId))
//
//        rollVisibleDao.update(noteId, isVisible)
//        assertEquals(isVisible, noteRepo.getRollVisible(noteId))
//    }
//
//
//    private companion object {
//        val firstAlarm = AlarmEntity(id = 1, noteId = 1, date = DATE_4)
//
//        val firstNote = NoteEntity(id = 1,
//                create = DATE_5, change = DATE_3, name = "NAME 1", text = "3/5", color = 0,
//                type = NoteType.ROLL, rankId = -1, rankPs = -1, isBin = false, isStatus = true
//        )
//
//        val secondNote = NoteEntity(id = 2,
//                create = DATE_1, change = DATE_2, name = "", text = "TEXT 1", color = 1,
//                type = NoteType.TEXT, rankId = 1, rankPs = 0, isBin = true, isStatus = false
//        )
//
//        val thirdNote = NoteEntity(id = 3,
//                create = DATE_2, change = DATE_1, name = "NAME 3", text = "TEXT 2", color = 2,
//                type = NoteType.TEXT, rankId = 2, rankPs = 1, isBin = true, isStatus = false
//        )
//
//        val fourthNote = NoteEntity(id = 4,
//                create = DATE_3, change = DATE_5, name = "NAME 4", text = "0/2", color = 3,
//                type = NoteType.ROLL, rankId = 1, rankPs = 0, isBin = false, isStatus = true
//        )
//
//        val firstRollList = mutableListOf(
//                RollEntity(id = 1, noteId = 1, position = 0, isCheck = true, text = "0"),
//                RollEntity(id = 2, noteId = 1, position = 1, isCheck = false, text = "1"),
//                RollEntity(id = 3, noteId = 1, position = 2, isCheck = true, text = "2"),
//                RollEntity(id = 4, noteId = 1, position = 3, isCheck = false, text = "3"),
//                RollEntity(id = 5, noteId = 1, position = 4, isCheck = true, text = "4")
//        )
//
//        val fourthRollList = mutableListOf(
//                RollEntity(id = 6, noteId = 4, position = 0, isCheck = false, text = "0"),
//                RollEntity(id = 7, noteId = 4, position = 1, isCheck = false, text = "1")
//        )
//
//        val firstRank = RankEntity(
//                id = 1, noteId = arrayListOf(2, 4), position = 0, name = "0", isVisible = true
//        )
//
//        val secondRank = RankEntity(
//                id = 2, noteId = arrayListOf(3), position = 1, name = "1", isVisible = false
//        )
//    }

}