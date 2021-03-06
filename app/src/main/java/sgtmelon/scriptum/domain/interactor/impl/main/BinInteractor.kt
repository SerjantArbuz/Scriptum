package sgtmelon.scriptum.domain.interactor.impl.main

import sgtmelon.scriptum.data.repository.preference.IPreferenceRepo
import sgtmelon.scriptum.data.repository.room.callback.INoteRepo
import sgtmelon.scriptum.domain.interactor.callback.main.IBinInteractor
import sgtmelon.scriptum.domain.interactor.impl.ParentInteractor
import sgtmelon.scriptum.domain.model.item.NoteItem
import sgtmelon.scriptum.presentation.screen.vm.callback.main.IBinViewModel

/**
 * Interactor for [IBinViewModel].
 */
class BinInteractor(
    private val preferenceRepo: IPreferenceRepo,
    private val noteRepo: INoteRepo
) : ParentInteractor(),
    IBinInteractor {

    override suspend fun getCount(): Int = noteRepo.getCount(isBin = true)

    override suspend fun getList(): MutableList<NoteItem> {
        val sort = preferenceRepo.sort
        return noteRepo.getList(sort, isBin = true, isOptimal = true, filterVisible = false)
    }

    override suspend fun clearBin() = noteRepo.clearBin()

    override suspend fun restoreNote(item: NoteItem) = noteRepo.restoreNote(item)

    override suspend fun copy(item: NoteItem): String = noteRepo.getCopyText(item)

    override suspend fun clearNote(item: NoteItem) = noteRepo.clearNote(item)
}