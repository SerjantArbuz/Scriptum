package sgtmelon.scriptum

import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import sgtmelon.extension.nextString
import sgtmelon.scriptum.domain.model.item.NoteItem
import sgtmelon.scriptum.domain.model.item.RollItem
import kotlin.random.Random

object FastMock {

    fun timeExtension() = mockkStatic("sgtmelon.extension.TimeExtensionUtils")

    fun listExtension() = mockkStatic("sgtmelon.scriptum.extension.ListExtensionUtils")

    fun daoExtension() = mockkStatic("sgtmelon.scriptum.data.room.extension.DaoExtensionUtils")

    fun daoHelpExtension() = mockkStatic("sgtmelon.scriptum.data.room.extension.DaoHelpExtensionUtils")

    object Note {

        fun deepCopy(
            item: NoteItem.Text,
            id: Long = Random.nextLong(),
            create: String = nextString(),
            change: String = nextString(),
            name: String = nextString(),
            text: String = nextString(),
            color: Int = Random.nextInt(),
            rankId: Long = Random.nextLong(),
            rankPs: Int = Random.nextInt(),
            isBin: Boolean = Random.nextBoolean(),
            isStatus: Boolean = Random.nextBoolean(),
            alarmId: Long = Random.nextLong(),
            alarmDate: String = nextString()
        ) {
            every { item.id } returns id
            every { item.create } returns create
            every { item.change } returns change
            every { item.name } returns name
            every { item.text } returns text
            every { item.color } returns color
            every { item.rankId } returns rankId
            every { item.rankPs } returns rankPs
            every { item.isBin } returns isBin
            every { item.isStatus } returns isStatus
            every { item.alarmId } returns alarmId
            every { item.alarmDate } returns alarmDate

            every {
                item.deepCopy(
                    any(), any(), any(), any(), any(), any(),
                    any(), any(), any(), any(), any(), any()
                )
            } returns item
        }

        fun deepCopy(
            item: NoteItem.Roll,
            id: Long = Random.nextLong(),
            create: String = nextString(),
            change: String = nextString(),
            name: String = nextString(),
            text: String = nextString(),
            color: Int = Random.nextInt(),
            rankId: Long = Random.nextLong(),
            rankPs: Int = Random.nextInt(),
            isBin: Boolean = Random.nextBoolean(),
            isStatus: Boolean = Random.nextBoolean(),
            alarmId: Long = Random.nextLong(),
            alarmDate: String = nextString(),
            isVisible: Boolean = Random.nextBoolean(),
            list: MutableList<RollItem> = MutableList(getRandomSize()) { mockk<RollItem>() }
        ) {
            every { item.id } returns id
            every { item.create } returns create
            every { item.change } returns change
            every { item.name } returns name
            every { item.text } returns text
            every { item.color } returns color
            every { item.rankId } returns rankId
            every { item.rankPs } returns rankPs
            every { item.isBin } returns isBin
            every { item.isStatus } returns isStatus
            every { item.alarmId } returns alarmId
            every { item.alarmDate } returns alarmDate
            every { item.isVisible } returns isVisible
            every { item.list } returns list

            for (it in list) {
                every { it.copy(any(), any(), any(), any()) } returns it
            }

            every {
                item.deepCopy(
                    any(), any(), any(), any(), any(), any(), any(),
                    any(), any(), any(), any(), any(), any(), any()
                )
            } returns item
        }
    }

}