package sgtmelon.scriptum.model

import androidx.room.Embedded
import androidx.room.Ignore
import androidx.room.Relation
import sgtmelon.scriptum.model.item.NoteItem
import sgtmelon.scriptum.model.item.RollItem
import sgtmelon.scriptum.model.item.StatusItem
import sgtmelon.scriptum.model.key.DbField

/**
 * Модель заметки
 *
 * @author SerjantArbuz
 */
class NoteModel(@field:Embedded val noteItem: NoteItem,
                @field:Relation(parentColumn = DbField.Note.ID, entityColumn = DbField.Roll.NOTE_ID)
                val listRoll: MutableList<RollItem>,
                @field:Ignore val statusItem: StatusItem) {

    /**
     * При отметке всех пунктов
     */
    fun updateCheck(check: Boolean) = listRoll.forEach { it.isCheck = check }

    fun updateStatus(status: Boolean) = when (status) {
        true -> statusItem.notifyNote()
        false -> statusItem.cancelNote()
    }

    fun updateStatus(listRankVisible: List<Long>) =
            statusItem.updateNote(noteItem, listRankVisible)

}