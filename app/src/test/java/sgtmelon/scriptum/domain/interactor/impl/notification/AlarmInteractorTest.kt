package sgtmelon.scriptum.domain.interactor.impl.notification

import io.mockk.coEvery
import io.mockk.coVerifySequence
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verifySequence
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.*
import org.junit.Test
import sgtmelon.extension.clearSeconds
import sgtmelon.extension.getText
import sgtmelon.scriptum.FastTest
import sgtmelon.scriptum.ParentInteractorTest
import sgtmelon.scriptum.TestData
import sgtmelon.scriptum.data.repository.preference.IPreferenceRepo
import sgtmelon.scriptum.data.repository.room.callback.IAlarmRepo
import sgtmelon.scriptum.data.repository.room.callback.INoteRepo
import sgtmelon.scriptum.domain.model.item.NotificationItem
import sgtmelon.scriptum.domain.model.item.NotificationItem.Alarm
import sgtmelon.scriptum.domain.model.item.NotificationItem.Note
import sgtmelon.scriptum.domain.model.key.NoteType
import sgtmelon.scriptum.presentation.screen.ui.callback.notification.IAlarmBridge
import java.util.*
import kotlin.random.Random

/**
 * Test for [AlarmInteractor].
 */
@ExperimentalCoroutinesApi
class AlarmInteractorTest : ParentInteractorTest() {

    private val data = TestData.Note

    @MockK lateinit var preferenceRepo: IPreferenceRepo
    @MockK lateinit var alarmRepo: IAlarmRepo
    @MockK lateinit var noteRepo: INoteRepo
    @MockK lateinit var callback: IAlarmBridge

    private val interactor by lazy {
        AlarmInteractor(preferenceRepo, alarmRepo, noteRepo, callback)
    }

    @Test override fun onDestroy() {
        assertNotNull(interactor.callback)
        interactor.onDestroy()
        assertNull(interactor.callback)
    }


    @Test fun getTheme() = FastTest.getTheme(preferenceRepo) { interactor.theme }

    @Test fun getRepeat() = FastTest.getRepeat(preferenceRepo) { interactor.repeat }

    @Test fun getVolume() = FastTest.getVolume(preferenceRepo) { interactor.volume }

    @Test fun getVolumeIncrease() {
        fun checkRequestGet(value: Boolean) {
            every { preferenceRepo.volumeIncrease } returns value
            assertEquals(interactor.volumeIncrease, value)
        }

        val valueList = listOf(true, false)
        valueList.forEach { checkRequestGet(it) }

        verifySequence {
            repeat(valueList.size) { preferenceRepo.volumeIncrease }
        }
    }

    @Test fun getModel() = startCoTest {
        val noteId = Random.nextLong()
        val item = data.itemList.random()

        coEvery { noteRepo.getItem(noteId, optimisation = true) } returns null
        assertEquals(null, interactor.getModel(noteId))

        coEvery { noteRepo.getItem(noteId, optimisation = true) } returns item
        assertEquals(item, interactor.getModel(noteId))

        coVerifySequence {
            alarmRepo.delete(noteId)
            noteRepo.getItem(noteId, optimisation = true)

            alarmRepo.delete(noteId)
            noteRepo.getItem(noteId, optimisation = true)
        }
    }

    @Test fun setupRepeat() = startCoTest {
        val item = data.itemList.random()

        val timeArray = intArrayOf(1, 2, 3, 4)
        val repeat = timeArray.indices.random()
        val calendar = interactor.getCalendarWithAdd(timeArray[repeat])

        coEvery { alarmRepo.getList() } returns mutableListOf()

        interactor.setupRepeat(item, intArrayOf(), Random.nextInt())
        interactor.setupRepeat(item, timeArray, repeat)

        coVerifySequence {
            alarmRepo.getList()
            alarmRepo.insertOrUpdate(item, calendar.getText())
            callback.setAlarm(calendar, item.id)
        }
    }

    @Test fun checkDateExist() = startCoTest {
        val dateList = List(size = 3) { interactor.getCalendarWithAdd(it).getText() }

        val itemList = MutableList(dateList.size) {
            val id = it.toLong()
            val type = if (Random.nextBoolean()) NoteType.TEXT else NoteType.ROLL

            return@MutableList NotificationItem(
                    Note(id, name = "name_$it", color = it, type = type), Alarm(id, dateList[it])
            )
        }

        coEvery { alarmRepo.getList() } returns itemList

        val currentCalendar = Calendar.getInstance().clearSeconds()
        val minute = currentCalendar.get(Calendar.MINUTE)

        interactor.checkDateExist(currentCalendar)

        val expected = (minute + dateList.size).takeIf { it < 60 } ?: 0
        assertEquals(expected, currentCalendar.get(Calendar.MINUTE))

        coVerifySequence {
            alarmRepo.getList()
        }
    }

}