package sgtmelon.scriptum.interactor.note

import android.content.Context
import sgtmelon.scriptum.interactor.ParentInteractor
import sgtmelon.scriptum.interactor.callback.note.INoteInteractor
import sgtmelon.scriptum.model.annotation.Color
import sgtmelon.scriptum.model.annotation.Theme
import sgtmelon.scriptum.screen.vm.note.NoteViewModel

/**
 * Interactor for [NoteViewModel]
 */
class NoteInteractor(context: Context) : ParentInteractor(context), INoteInteractor {

    @Theme override val theme: Int get() = iPreferenceRepo.theme

    @Color override val defaultColor: Int get() = iPreferenceRepo.defaultColor

}