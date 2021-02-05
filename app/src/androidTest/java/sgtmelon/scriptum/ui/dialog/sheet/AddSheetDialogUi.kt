package sgtmelon.scriptum.ui.dialog.sheet

import sgtmelon.scriptum.R
import sgtmelon.scriptum.basic.extension.click
import sgtmelon.scriptum.basic.extension.isDisplayed
import sgtmelon.scriptum.basic.extension.isEnabled
import sgtmelon.scriptum.basic.extension.withTextColor
import sgtmelon.scriptum.data.State
import sgtmelon.scriptum.domain.model.item.NoteItem
import sgtmelon.scriptum.presentation.dialog.SheetAddDialog
import sgtmelon.scriptum.ui.screen.note.RollNoteScreen
import sgtmelon.scriptum.ui.screen.note.TextNoteScreen

/**
 * Class for UI control [SheetAddDialog].
 */
class AddSheetDialogUi : ParentSheetDialogUi(R.id.add_container, R.id.add_navigation) {

    //region Views

    private val titleText = getViewByText(R.string.dialog_title_add_note)
    private val textButton = getViewByText(R.string.dialog_add_text)
    private val rollButton = getViewByText(R.string.dialog_add_roll)

    //endregion

    fun createText(
        item: NoteItem.Text,
        isRankEmpty: Boolean = true,
        func: TextNoteScreen.() -> Unit = {}
    ) {
        textButton.click()
        TextNoteScreen(func, State.NEW, item, isRankEmpty)
    }

    fun createRoll(
        item: NoteItem.Roll,
        isRankEmpty: Boolean = true,
        func: RollNoteScreen.() -> Unit = {}
    ) {
        rollButton.click()
        RollNoteScreen(func, State.NEW, item, isRankEmpty)
    }


    override fun assert() {
        super.assert()

        titleText.isDisplayed().withTextColor(R.attr.clContentSecond)
        textButton.isDisplayed().withTextColor(R.attr.clContent).isEnabled()
        rollButton.isDisplayed().withTextColor(R.attr.clContent).isEnabled()
    }

    companion object {
        operator fun invoke(func: AddSheetDialogUi.() -> Unit): AddSheetDialogUi {
            return AddSheetDialogUi().apply { waitOpen { assert() } }.apply(func)
        }
    }
}