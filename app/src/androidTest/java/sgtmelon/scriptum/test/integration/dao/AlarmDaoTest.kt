package sgtmelon.scriptum.test.integration.dao

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.data.room.RoomDb
import sgtmelon.scriptum.data.room.dao.IAlarmDao
import sgtmelon.scriptum.data.room.entity.AlarmEntity
import sgtmelon.scriptum.data.room.entity.NoteEntity
import sgtmelon.scriptum.data.room.extension.inRoomTest
import sgtmelon.scriptum.domain.model.item.NotificationItem
import sgtmelon.scriptum.domain.model.key.NoteType
import sgtmelon.scriptum.test.parent.ParentRoomTest
import kotlin.random.Random

/**
 * Integration test for [IAlarmDao]
 */
@RunWith(AndroidJUnit4::class)
class AlarmDaoTest : ParentRoomTest() {

    //region Variables

    private val firstNote = NoteEntity(
        id = 1, create = DATE_1, change = DATE_1, text = "123", name = "456",
        color = 1, type = NoteType.TEXT
    )

    private val secondNote = NoteEntity(
        id = 2, create = DATE_2, change = DATE_2, text = "654", name = "321",
        color = 2, type = NoteType.TEXT
    )

    private val firstAlarm = AlarmEntity(id = 1, noteId = 1, date = DATE_1)
    private val secondAlarm = AlarmEntity(id = 2, noteId = 2, date = DATE_2)

    private val firstNotification = NotificationItem(
        with(firstNote) { NotificationItem.Note(id, name, color, type) },
        with(firstAlarm) { NotificationItem.Alarm(id, date) }
    )

    private val secondNotification = NotificationItem(
        with(secondNote) { NotificationItem.Note(id, name, color, type) },
        with(secondAlarm) { NotificationItem.Alarm(id, date) }
    )

    private val notificationList = arrayListOf(firstNotification, secondNotification)

    //endregion

    private suspend fun RoomDb.insertAlarmRelation(
        noteEntity: NoteEntity,
        alarmEntity: AlarmEntity
    ) {
        noteDao.insert(noteEntity)
        alarmDao.insert(alarmEntity)
    }

    // Dao tests

    @Test fun insertWithUnique() = inRoomTest {
        insertAlarmRelation(firstNote, firstAlarm)

        val newAlarm = secondAlarm.copy(noteId = firstAlarm.noteId)
        assertEquals(newAlarm.id, alarmDao.insert(newAlarm))
    }

    @Test fun delete() = inRoomTest {
        insertAlarmRelation(firstNote, firstAlarm)

        alarmDao.delete(firstAlarm.noteId)
        assertNull(alarmDao.get(firstAlarm.noteId))
    }

    @Test fun update() = inRoomTest {
        insertAlarmRelation(firstNote, firstAlarm)

        firstAlarm.copy(date = DATE_2).let {
            alarmDao.update(it)
            assertEquals(it, alarmDao.get(firstAlarm.noteId))
        }
    }

    @Test fun get() = inRoomTest {
        insertAlarmRelation(secondNote, secondAlarm)
        insertAlarmRelation(firstNote, firstAlarm)

        assertEquals(listOf(firstAlarm, secondAlarm), alarmDao.get())
    }

    @Test fun getOnWrongId() = inRoomTest { assertNull(alarmDao.get(Random.nextLong())) }

    @Test fun getOnCorrectId() = inRoomTest {
        insertAlarmRelation(firstNote, firstAlarm)

        assertEquals(firstAlarm, alarmDao.get(firstAlarm.noteId))
    }

    @Test fun getListById() = inRoomTest {
        insertAlarmRelation(firstNote, firstAlarm)
        insertAlarmRelation(secondNote, secondAlarm)

        val alarmList = listOf(firstAlarm, secondAlarm)
        val noteIdList = listOf(firstNote.id, secondNote.id)

        assertEquals(alarmList, alarmDao.get(noteIdList))
    }

    @Test fun getListByIdCrowd() = inRoomTest { alarmDao.get(crowdList) }

    @Test fun getItem() = inRoomTest {
        assertNull(alarmDao.getItem(Random.nextLong()))

        insertAlarmRelation(firstNote, firstAlarm)
        insertAlarmRelation(secondNote, secondAlarm)

        assertEquals(firstNotification, alarmDao.getItem(firstNote.id))
        assertEquals(secondNotification, alarmDao.getItem(secondNote.id))
    }

    @Test fun getList() = inRoomTest {
        assertTrue(alarmDao.getList().isEmpty())

        insertAlarmRelation(firstNote, firstAlarm)
        insertAlarmRelation(secondNote, secondAlarm)

        assertEquals(notificationList, alarmDao.getList())
    }

    @Test fun getCount() = inRoomTest {
        var size = 0

        assertEquals(size, alarmDao.getCount())

        insertAlarmRelation(firstNote, firstAlarm)
        assertEquals(++size, alarmDao.getCount())

        insertAlarmRelation(secondNote, secondAlarm)
        assertEquals(++size, alarmDao.getCount())
    }

    @Test fun getCountByIdList() = inRoomTest {
        var size = 0

        assertEquals(size, alarmDao.getCount(listOf()))

        insertAlarmRelation(firstNote, firstAlarm)
        assertEquals(++size, alarmDao.getCount(listOf(firstNote.id)))

        insertAlarmRelation(secondNote, secondAlarm)
        assertEquals(++size, alarmDao.getCount(listOf(firstNote.id, secondNote.id)))
    }

    @Test fun getCountByIdListCrowd() = inRoomTest { alarmDao.getCount(crowdList) }
}