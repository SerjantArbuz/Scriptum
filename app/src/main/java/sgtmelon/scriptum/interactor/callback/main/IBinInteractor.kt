package sgtmelon.scriptum.interactor.callback.main

import sgtmelon.scriptum.interactor.main.BinInteractor
import sgtmelon.scriptum.model.NoteModel
import sgtmelon.scriptum.model.annotation.Theme

/**
 * Interface for communicate with [BinInteractor]
 */
interface IBinInteractor {

    @Theme val theme: Int

    fun getList(): MutableList<NoteModel>

    suspend fun clearBin()

    suspend fun restoreNote(noteModel: NoteModel)

    suspend fun clearNote(noteModel: NoteModel)

}