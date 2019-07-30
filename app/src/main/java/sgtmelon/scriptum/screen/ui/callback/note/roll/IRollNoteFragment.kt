package sgtmelon.scriptum.screen.ui.callback.note.roll

import sgtmelon.scriptum.control.input.InputCallback
import sgtmelon.scriptum.control.input.InputControl
import sgtmelon.scriptum.model.NoteModel
import sgtmelon.scriptum.model.annotation.Color
import sgtmelon.scriptum.model.annotation.Theme
import sgtmelon.scriptum.model.state.NoteState
import sgtmelon.scriptum.room.entity.RollEntity
import sgtmelon.scriptum.screen.ui.note.RollNoteFragment
import sgtmelon.scriptum.screen.vm.note.RollNoteViewModel

/**
 * Interface for communication [RollNoteViewModel] with [RollNoteFragment]
 *
 * @author SerjantArbuz
 */
interface IRollNoteFragment {

    /**
     * Установка элементов для биндинга, которые постоянные
     */
    fun setupBinding(@Theme theme: Int, rankEmpty: Boolean)

    fun setupToolbar(@Theme theme: Int, @Color color: Int, noteState: NoteState)

    fun setupDialog(rankNameList: List<String>)

    fun setupEnter(inputCallback: InputCallback)

    fun setupRecycler(inputCallback: InputCallback)

    fun bindEdit(editMode: Boolean, noteModel: NoteModel)

    fun bindNote(noteModel: NoteModel)

    fun bindEnter()

    fun bindInput(inputAccess: InputControl.Access, noteModel: NoteModel)

    fun onPressBack(): Boolean

    fun tintToolbar(@Color from: Int, @Color to: Int)

    fun tintToolbar(@Color color: Int)

    fun changeToolbarIcon(drawableOn: Boolean, needAnim: Boolean)

    fun focusOnEdit()

    fun changeName(text: String, cursor: Int)

    fun getEnterText(): String

    fun clearEnterText()

    fun scrollToItem(simpleClick: Boolean, p: Int, list: MutableList<RollEntity>)

    fun changeCheckToggle(state: Boolean)

    fun updateNoteState(noteState: NoteState)

    fun notifyListItem(p: Int, rollEntity: RollEntity)

    fun notifyList(list: MutableList<RollEntity>)

    fun notifyDataSetChanged(list: MutableList<RollEntity>)

    fun notifyItemInserted(p: Int, cursor: Int, list: MutableList<RollEntity>)

    fun notifyItemChanged(p: Int, list: MutableList<RollEntity>, cursor: Int)

    fun notifyItemRemoved(p: Int, list: MutableList<RollEntity>)

    fun notifyItemMoved(from: Int, to: Int, list: MutableList<RollEntity>)

    fun hideKeyboard()

    fun showRankDialog(rankCheck: BooleanArray)

    fun showColorDialog(color: Int)

    fun showConvertDialog()

}