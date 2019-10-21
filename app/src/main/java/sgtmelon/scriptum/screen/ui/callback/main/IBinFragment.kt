package sgtmelon.scriptum.screen.ui.callback.main

import sgtmelon.scriptum.model.NoteModel
import sgtmelon.scriptum.model.annotation.Theme
import sgtmelon.scriptum.room.entity.NoteEntity
import sgtmelon.scriptum.screen.ui.main.BinFragment
import sgtmelon.scriptum.screen.vm.main.BinViewModel

/**
 * Interface for communication [BinViewModel] with [BinFragment]
 */
interface IBinFragment : IBinBridge{

    fun setupToolbar()

    fun setupRecycler(@Theme theme: Int)

    fun bindList()

    fun scrollTop()

    fun startNoteActivity(noteEntity: NoteEntity)

    fun showOptionsDialog(itemArray: Array<String>, p: Int)

    fun notifyMenuClearBin()

    fun notifyDataSetChanged(list: MutableList<NoteModel>)

    fun notifyItemRemoved(p: Int, list: MutableList<NoteModel>)

}