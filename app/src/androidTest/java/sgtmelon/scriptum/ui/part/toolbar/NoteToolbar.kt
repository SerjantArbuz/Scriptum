package sgtmelon.scriptum.ui.part.toolbar

import sgtmelon.scriptum.R
import sgtmelon.scriptum.basic.extension.*
import sgtmelon.scriptum.data.State
import sgtmelon.scriptum.model.annotation.Theme
import sgtmelon.scriptum.model.item.InputItem
import sgtmelon.scriptum.ui.ParentUi
import sgtmelon.scriptum.ui.screen.note.INoteScreen
import sgtmelon.scriptum.ui.screen.note.RollNoteScreen
import sgtmelon.scriptum.ui.screen.note.TextNoteScreen

/**
 * Part of UI abstraction for [TextNoteScreen] и [RollNoteScreen]
 */
class NoteToolbar(private val callback: INoteScreen) : ParentUi() {

    //region Views

    private val parentContainer = getViewById(R.id.toolbar_note_container)
    private val nameScroll = getViewById(R.id.toolbar_note_scroll)

    private val colorView = getViewById(R.id.toolbar_note_color_view)

    private val nameText = getViewById(R.id.toolbar_note_text)
    private val nameEnter = getViewById(R.id.toolbar_note_enter)

    //endregion

    fun onEnterName(name: String) = apply {
        callback.throwOnWrongState(State.EDIT, State.NEW) {
            nameEnter.typeText(name)

            callback.apply {
                name.forEachIndexed { i, c ->
                    val valueFrom = if (i == 0) shadowItem.text else name[i - 1].toString()
                    val valueTo = c.toString()

                    inputControl.onNameChange(
                            valueFrom, valueTo, InputItem.Cursor(valueFrom.length, valueTo.length)
                    )
                }

                shadowItem.name = name
            }.fullAssert()
        }
    }

    fun onClickBack() {
        getToolbarButton().click()

        with(callback) {
            if (state == State.EDIT) {
                state = State.READ
                shadowItem = noteItem.copy()
                inputControl.reset()
                fullAssert()
            }
        }
    }


    // TODO #TEST (focus on title check)
    // TODO #TEST assert color (set contentDescription may be, or tag)
    fun assert() {
        parentContainer.isDisplayed()
        nameScroll.isDisplayed()

        colorView.isDisplayed(visible = theme == Theme.DARK)

        callback.apply {
            when (state) {
                State.READ, State.BIN -> {
                    val name = noteItem.name

                    nameEnter.isDisplayed(visible = false)
                    nameText.isDisplayed().apply {
                        if (name.isNotEmpty()) haveText(name) else haveHint(R.string.hint_text_name)
                    }
                }
                State.EDIT, State.NEW -> {
                    val name = shadowItem.name

                    nameText.isDisplayed(visible = false)
                    nameEnter.isDisplayed().apply {
                        if (name.isNotEmpty()) haveText(name) else haveHint(R.string.hint_enter_name)
                    }
                }
            }
        }
    }

    companion object {
        operator fun invoke(func: NoteToolbar.() -> Unit, callback: INoteScreen) =
                NoteToolbar(callback).apply { assert() }.apply(func)
    }

}