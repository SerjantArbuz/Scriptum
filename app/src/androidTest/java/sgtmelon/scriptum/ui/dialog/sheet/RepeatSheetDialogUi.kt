package sgtmelon.scriptum.ui.dialog.sheet

import sgtmelon.scriptum.R
import sgtmelon.scriptum.basic.extension.click
import sgtmelon.scriptum.basic.extension.isDisplayed
import sgtmelon.scriptum.basic.extension.isEnabled
import sgtmelon.scriptum.basic.extension.withTextColor
import sgtmelon.scriptum.domain.model.annotation.Repeat
import sgtmelon.scriptum.presentation.dialog.SheetRepeatDialog

/**
 * Class for UI control [SheetRepeatDialog].
 */
class RepeatSheetDialogUi : ParentSheetDialogUi(R.id.repeat_container, R.id.repeat_navigation) {

    //region Views

    private val titleText = getViewByText(R.string.dialog_title_repeat)
    private val repeat0Button = getViewByText(R.string.dialog_repeat_0)
    private val repeat1Button = getViewByText(R.string.dialog_repeat_1)
    private val repeat2Button = getViewByText(R.string.dialog_repeat_2)
    private val repeat3Button = getViewByText(R.string.dialog_repeat_3)
    private val repeat4Button = getViewByText(R.string.dialog_repeat_4)

    //endregion

    fun onClickRepeat(@Repeat repeat: Int) {
        when(repeat) {
            Repeat.MIN_10 -> repeat0Button.click()
            Repeat.MIN_30 -> repeat1Button.click()
            Repeat.MIN_60 -> repeat2Button.click()
            Repeat.MIN_180 -> repeat3Button.click()
            Repeat.MIN_1440 -> repeat4Button.click()
        }
    }


    override fun assert() {
        super.assert()

        titleText.isDisplayed().withTextColor(R.attr.clContentSecond)
        repeat0Button.isDisplayed().withTextColor(R.attr.clContent).isEnabled()
        repeat1Button.isDisplayed().withTextColor(R.attr.clContent).isEnabled()
        repeat2Button.isDisplayed().withTextColor(R.attr.clContent).isEnabled()
        repeat3Button.isDisplayed().withTextColor(R.attr.clContent).isEnabled()
        repeat4Button.isDisplayed().withTextColor(R.attr.clContent).isEnabled()
    }

    companion object {
        operator fun invoke(func: RepeatSheetDialogUi.() -> Unit): RepeatSheetDialogUi {
            return RepeatSheetDialogUi().apply { waitOpen { assert() } }.apply(func)
        }
    }
}