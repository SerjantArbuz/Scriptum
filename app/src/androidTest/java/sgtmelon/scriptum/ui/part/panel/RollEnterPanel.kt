package sgtmelon.scriptum.ui.part.panel

import sgtmelon.scriptum.R
import sgtmelon.scriptum.basic.extension.*
import sgtmelon.scriptum.data.State
import sgtmelon.scriptum.ui.ParentUi
import sgtmelon.scriptum.ui.screen.note.INoteScreen
import sgtmelon.scriptum.ui.screen.note.RollNoteScreen
import kotlin.random.Random

/**
 * Part of UI abstraction for [RollNoteScreen]
 */
class RollEnterPanel(private val callback: INoteScreen) : ParentUi() {

    //region Views

    private val enterContainer = getViewById(R.id.roll_note_enter_container)
    private val textEnter = getViewById(R.id.roll_note_enter)
    private val addButton = getViewById(R.id.roll_note_add_button)

    //endregion

    private var enterText: String = ""
        set(value) {
            field = value
            assert()
        }

    fun onAddRoll(text: String) {
        textEnter.typeText(text)

        enterText = text
        if (Random.nextBoolean()) addButton.click() else addButton.longClick()
        enterText = ""
    }


    fun assert() {
        val visible = with(callback) { state == State.EDIT || state == State.NEW }

        enterContainer.isDisplayed(visible)
        textEnter.isDisplayed(visible)
        addButton.isDisplayed(visible).isEnabled(enterText.isNotEmpty())
    }

    companion object {
        operator fun invoke(func: RollEnterPanel.() -> Unit, callback: INoteScreen) =
                RollEnterPanel(callback).apply { assert() }.apply(func)
    }

}