package sgtmelon.scriptum.screen.vm.callback.note

import android.os.Bundle
import sgtmelon.scriptum.control.input.watcher.InputTextWatcher
import sgtmelon.scriptum.screen.ui.callback.note.text.ITextNoteMenu
import sgtmelon.scriptum.screen.ui.note.TextNoteFragment
import sgtmelon.scriptum.screen.vm.callback.IParentViewModel
import sgtmelon.scriptum.screen.vm.note.TextNoteViewModel

/**
 * Interface for communication [TextNoteFragment] with [TextNoteViewModel]
 *
 * @author SerjantArbuz
 */
interface ITextNoteViewModel : IParentViewModel, ITextNoteMenu, InputTextWatcher.TextChange {

    fun onSetupData(bundle: Bundle?)

    fun onSaveData(bundle: Bundle)

    fun onPause()

    fun onClickBackArrow()

    fun onPressBack(): Boolean

    fun onResultColorDialog(check: Int)

    fun onResultRankDialog(check: BooleanArray)

    fun onResultConvertDialog()

    fun onCancelNoteBind()

}