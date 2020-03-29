package sgtmelon.scriptum.data.repository.room.callback

import sgtmelon.scriptum.data.repository.room.NoteRepo
import sgtmelon.scriptum.domain.model.annotation.Sort
import sgtmelon.scriptum.domain.model.item.NoteItem
import sgtmelon.scriptum.domain.model.item.RollItem

/**
 * Interface for communicate with [NoteRepo]
 */
interface INoteRepo {

    suspend fun getCount(bin: Boolean): Int

    suspend fun getList(@Sort sort: Int, bin: Boolean, optimal: Boolean,
                filterVisible: Boolean): MutableList<NoteItem>

    suspend fun getItem(id: Long, optimisation: Boolean): NoteItem?

    suspend fun getRollList(noteId: Long): MutableList<RollItem>


    suspend fun isListHide(): Boolean

    suspend fun clearBin()


    suspend fun deleteNote(noteItem: NoteItem)

    suspend fun restoreNote(noteItem: NoteItem)

    suspend fun clearNote(noteItem: NoteItem)


    /**
     * TODO #THINK in notes list need add fast convert
     * (prepare all data - update note - suspend work with db)
     */
    suspend fun convertToRoll(noteItem: NoteItem)

    /**
     * TODO #THINK in notes list need add fast convert
     * (prepare all data - update note - suspend work with db)
     */
    suspend fun convertToText(noteItem: NoteItem, useCache: Boolean)

    suspend fun getCopyText(noteItem: NoteItem): String

    suspend fun saveTextNote(noteItem: NoteItem, isCreate: Boolean)

    suspend fun saveRollNote(noteItem: NoteItem, isCreate: Boolean)


    suspend fun updateRollCheck(noteItem: NoteItem, p: Int)

    suspend fun updateRollCheck(noteItem: NoteItem, check: Boolean)

    suspend fun updateNote(noteItem: NoteItem)


    suspend fun setRollVisible(noteId: Long, isVisible: Boolean)

    suspend fun getRollVisible(noteId: Long): Boolean

}