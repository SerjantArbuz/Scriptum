package sgtmelon.scriptum.data

import android.content.Context
import sgtmelon.scriptum.R
import sgtmelon.scriptum.TestUtils.random
import sgtmelon.scriptum.model.data.ColorData
import sgtmelon.scriptum.model.item.NoteItem
import sgtmelon.scriptum.model.item.RankItem
import sgtmelon.scriptum.model.item.RollItem
import sgtmelon.scriptum.model.key.NoteType
import sgtmelon.scriptum.office.utils.HelpUtils.Note.getCheck
import sgtmelon.scriptum.office.utils.TimeUtils.getTime
import sgtmelon.scriptum.room.RoomDb
import java.util.UUID.randomUUID

class TestData(private val context: Context) {

    // TODO testRoomDao

    private val dataBase: RoomDb get() = RoomDb.getInstance(context)

    val uniqueString get() = randomUUID().toString().substring(0, 16)

    val textNote: NoteItem
        get() = NoteItem().apply {
            create = context.getTime()
            change = context.getTime()
            name = context.getString(R.string.test_note_name)
            text = context.getString(R.string.test_note_text)
            color = (0 until ColorData.size).random()
            type = NoteType.TEXT
        }

    val rollNote: NoteItem
        get() = NoteItem().apply {
            create = context.getTime()
            change = context.getTime()
            name = context.getString(R.string.test_note_name)
            setCompleteText(listRoll.getCheck(), listRoll.size)
            color = (0 until ColorData.size).random()
            type = NoteType.ROLL
        }

    val listRoll = object : ArrayList<RollItem>() {
        init {
            for (i in 0 until 10) {
                add(RollItem().apply {
                    position = i
                    isCheck = Math.random() < 0.5
                    text = i.toString() + " | " + context.getString(R.string.test_roll_text)
                })
            }
        }
    }

    val rankItem: RankItem get() = RankItem(name = uniqueString)


    fun clearAllData() = dataBase.apply { clearAllTables() }.close()


    fun insertRank(rankItem: RankItem = this.rankItem): RankItem {
        dataBase.apply { rankItem.id = getRankDao().insert(rankItem) }.close()

        return rankItem
    }

    fun insertText(noteItem: NoteItem = textNote): NoteItem {
        dataBase.apply { noteItem.id = getNoteDao().insert(noteItem) }.close()

        return noteItem
    }

    fun insertRoll(noteItem: NoteItem = rollNote, listRoll: List<RollItem> = this.listRoll)
            : NoteItem {
        dataBase.apply {
            noteItem.id = getNoteDao().insert(noteItem)
            listRoll.forEach {
                it.noteId = noteItem.id
                getRollDao().insert(it)
            }
        }.close()

        return noteItem
    }


    fun insertTextToBin(noteItem: NoteItem = textNote): NoteItem {
        noteItem.isBin = true
        return insertText(noteItem)
    }

    fun insertRollToBin(noteItem: NoteItem = rollNote, listRoll: List<RollItem> = this.listRoll)
            : NoteItem {
        noteItem.isBin = true
        return insertRoll(noteItem, listRoll)
    }

    fun insertRankToNote() = ArrayList<RankItem>().apply {
        val noteItem = if (Math.random() < 0.5) insertText() else insertRoll()

        (1..2).forEach {
            val rankItem = insertRank(rankItem.apply {
                name = "$it | $name"
                noteId.add(noteItem.id)
                position = it
            })

            add(rankItem)
        }

        forEach {
            noteItem.rankId.add(it.id)
            noteItem.rankPs.add(it.position.toLong())
        }

        dataBase.apply { getNoteDao().update(noteItem) }.close()
    }

    fun fillRank(times: Int = 10) = ArrayList<RankItem>().apply {
        (1..times).forEach {
            val rankItem = insertRank(rankItem.apply {
                name = "$it | $name"
                position = it
                isVisible = Math.random() < 0.5
            })

            add(rankItem)
        }
    }

    fun fillNotes(times: Int = 10) = repeat(times) {
        if (Math.random() < 0.5) insertText() else insertRoll()
    }

    fun fillBin(times: Int = 10) = repeat(times) {
        if (Math.random() < 0.5) insertTextToBin() else insertRollToBin()
    }

}