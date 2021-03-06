package sgtmelon.scriptum.presentation.screen.vm.callback.note

import android.os.Bundle
import sgtmelon.scriptum.presentation.control.note.input.watcher.InputTextWatcher
import sgtmelon.scriptum.presentation.control.note.save.SaveControl
import sgtmelon.scriptum.presentation.receiver.screen.NoteScreenReceiver
import sgtmelon.scriptum.presentation.screen.ui.callback.note.INoteMenu
import sgtmelon.scriptum.presentation.screen.vm.callback.IParentViewModel
import sgtmelon.scriptum.presentation.screen.vm.impl.note.ParentNoteViewModel
import java.util.*

/**
 * Parent interface for communicate with children of [ParentNoteViewModel].
 */
interface IParentNoteViewModel : IParentViewModel,
    NoteScreenReceiver.Callback,
    INoteMenu,
    SaveControl.Callback,
    InputTextWatcher.Callback {

    fun onSaveData(bundle: Bundle)

    fun onResume()

    fun onPause()


    fun onClickBackArrow()

    fun onPressBack(): Boolean


    fun onResultColorDialog(check: Int)

    fun onResultRankDialog(check: Int)

    fun onResultDateDialog(calendar: Calendar)

    fun onResultDateDialogClear()

    fun onResultTimeDialog(calendar: Calendar)

    fun onResultConvertDialog()

}
