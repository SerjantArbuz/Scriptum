package sgtmelon.scriptum.model

import androidx.room.Embedded
import androidx.room.Relation
import sgtmelon.scriptum.model.data.DbData
import sgtmelon.scriptum.model.key.NoteType
import sgtmelon.scriptum.room.entity.AlarmEntity
import sgtmelon.scriptum.room.entity.NoteItem
import sgtmelon.scriptum.room.entity.RollItem

/**
 * Модель заметки
 *
 * @author SerjantArbuz
 */
data class NoteModel(
        @Embedded val noteItem: NoteItem,
        @Relation(parentColumn = DbData.Note.ID, entityColumn = DbData.Roll.NOTE_ID)
        val rollList: MutableList<RollItem> = ArrayList(),
        @Embedded val alarmEntity: AlarmEntity = AlarmEntity()
) {

    /**
     * При отметке всех пунктов
     */
    fun updateCheck(check: Boolean) = rollList.forEach { it.isCheck = check }

    fun isSaveEnabled(): Boolean = when (noteItem.type) {
        NoteType.TEXT -> noteItem.text.isNotEmpty()
        NoteType.ROLL -> {
            if (rollList.isNotEmpty()) rollList.forEach { if (it.text.isNotEmpty()) return true }
            false
        }
    }

}