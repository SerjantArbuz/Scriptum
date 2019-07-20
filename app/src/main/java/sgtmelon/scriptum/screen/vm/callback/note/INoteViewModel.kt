package sgtmelon.scriptum.screen.vm.callback.note

import android.os.Bundle
import sgtmelon.scriptum.receiver.NoteReceiver
import sgtmelon.scriptum.screen.ui.note.NoteActivity
import sgtmelon.scriptum.screen.vm.callback.IParentViewModel
import sgtmelon.scriptum.screen.vm.note.NoteViewModel

/**
 * Интерфейс общения [NoteActivity] с [NoteViewModel]
 *
 * @author SerjantArbuz
 */
interface INoteViewModel : IParentViewModel, NoteReceiver.Callback {

    fun onSetupData(bundle: Bundle?)

    fun onSaveData(bundle: Bundle)

    fun onSetupFragment(isSave: Boolean)

    fun onPressBack(): Boolean

    fun onUpdateNoteId(id: Long)

    fun onConvertNote()

}