package sgtmelon.scriptum.domain.interactor.impl.notification

import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import sgtmelon.extension.nextString
import sgtmelon.scriptum.data.repository.room.callback.IAlarmRepo
import sgtmelon.scriptum.data.repository.room.callback.IBindRepo
import sgtmelon.scriptum.data.repository.room.callback.INoteRepo
import sgtmelon.scriptum.domain.model.item.NoteItem
import sgtmelon.scriptum.domain.model.item.NotificationItem
import sgtmelon.scriptum.parent.ParentInteractorTest
import kotlin.random.Random

/**
 * Test for [NotificationInteractor].
 */
@ExperimentalCoroutinesApi
class NotificationInteractorTest : ParentInteractorTest() {

    @MockK lateinit var noteRepo: INoteRepo
    @MockK lateinit var alarmRepo: IAlarmRepo
    @MockK lateinit var bindRepo: IBindRepo

    private val interactor by lazy {
        NotificationInteractor(noteRepo, alarmRepo, bindRepo)
    }

    @After override fun tearDown() {
        super.tearDown()
        confirmVerified(noteRepo, alarmRepo, bindRepo)
    }


    @Test fun getCount() = startCoTest {
        val count = Random.nextInt()

        coEvery { bindRepo.getNotificationCount() } returns count
        assertEquals(count, interactor.getCount())

        coVerifySequence {
            bindRepo.getNotificationCount()
        }
    }

    @Test fun getList() = startCoTest {
        val list = mockk<MutableList<NotificationItem>>()

        coEvery { alarmRepo.getList() } returns list
        assertEquals(list, interactor.getList())

        coVerifySequence {
            alarmRepo.getList()
        }
    }


    @Test fun setNotification() = startCoTest {
        val notificationItem = mockk<NotificationItem>()
        val newNotificationItem = mockk<NotificationItem>()
        val noteItem = mockk<NoteItem>()

        val note = mockk<NotificationItem.Note>()
        val alarm = mockk<NotificationItem.Alarm>()

        val id = Random.nextLong()
        val date = nextString()

        every { notificationItem.note } returns note
        every { note.id } returns id
        coEvery { noteRepo.getItem(id, isOptimal = true) } returns null
        assertNull(interactor.setNotification(notificationItem))

        coEvery { noteRepo.getItem(id, isOptimal = true) } returns noteItem
        every { notificationItem.alarm } returns alarm
        every { alarm.date } returns date
        coEvery { alarmRepo.getItem(id) } returns null
        assertNull(interactor.setNotification(notificationItem))

        coEvery { alarmRepo.getItem(id) } returns newNotificationItem
        assertEquals(newNotificationItem, interactor.setNotification(notificationItem))

        coVerifySequence {
            notificationItem.note
            note.id
            noteRepo.getItem(id, isOptimal = true)

            repeat(times = 2) {
                notificationItem.note
                note.id
                noteRepo.getItem(id, isOptimal = true)
                notificationItem.alarm
                alarm.date
                alarmRepo.insertOrUpdate(noteItem, date)
                alarmRepo.getItem(id)
            }
        }
    }

    @Test fun cancelNotification() = startCoTest {
        val item = mockk<NotificationItem>()
        val note = mockk<NotificationItem.Note>()
        val id = Random.nextLong()

        every { item.note } returns note
        every { note.id } returns id

        interactor.cancelNotification(item)

        coVerifySequence {
            item.note
            note.id
            alarmRepo.delete(id)
        }
    }
}