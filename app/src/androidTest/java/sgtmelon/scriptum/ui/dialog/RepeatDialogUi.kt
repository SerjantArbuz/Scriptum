package sgtmelon.scriptum.ui.dialog

import sgtmelon.scriptum.R
import sgtmelon.scriptum.basic.extension.*
import sgtmelon.scriptum.domain.model.annotation.Repeat
import sgtmelon.scriptum.ui.IDialogUi
import sgtmelon.scriptum.ui.ParentRecyclerScreen

class RepeatDialogUi : ParentRecyclerScreen(R.id.select_dialog_listview), IDialogUi {

    private var check: Int = repeat

    //region Views

    /**
     * Exclude this parent for prevent match error with summary (similar strings).
     */
    private val preferenceList = getViewById(R.id.recycler_view)

    private val titleText = getViewByText(R.string.pref_title_alarm_repeat).excludeParent(preferenceList)

    private val repeat0Button = getViewByText(R.string.dialog_repeat_0).excludeParent(preferenceList)
    private val repeat1Button = getViewByText(R.string.dialog_repeat_1).excludeParent(preferenceList)
    private val repeat2Button = getViewByText(R.string.dialog_repeat_2).excludeParent(preferenceList)
    private val repeat3Button = getViewByText(R.string.dialog_repeat_3).excludeParent(preferenceList)
    private val repeat4Button = getViewByText(R.string.dialog_repeat_4).excludeParent(preferenceList)

    private val cancelButton = getViewByText(R.string.dialog_button_cancel)
    private val applyButton = getViewByText(R.string.dialog_button_apply)

    //endregion

    fun onClickItem(@Repeat repeat: Int) = apply {
        check = repeat

        when(repeat) {
            Repeat.MIN_10 -> repeat0Button.click()
            Repeat.MIN_30 -> repeat1Button.click()
            Repeat.MIN_60 -> repeat2Button.click()
            Repeat.MIN_180 -> repeat3Button.click()
            Repeat.MIN_1440 -> repeat4Button.click()
        }

        assert()
    }

    fun onClickCancel() = waitClose { cancelButton.click() }

    fun onClickApply() = waitClose {
        if (check == repeat) throw IllegalAccessException("Apply button not enabled")

        applyButton.click()
    }


    fun assert() {
        recyclerView.isDisplayed()
        titleText.isDisplayed()

        repeat0Button.isDisplayed().isChecked(isChecked = check == Repeat.MIN_10)
        repeat1Button.isDisplayed().isChecked(isChecked = check == Repeat.MIN_30)
        repeat2Button.isDisplayed().isChecked(isChecked = check == Repeat.MIN_60)
        repeat3Button.isDisplayed().isChecked(isChecked = check == Repeat.MIN_180)
        repeat4Button.isDisplayed().isChecked(isChecked = check == Repeat.MIN_1440)

        cancelButton.isDisplayed().isEnabled().withTextColor(R.attr.clAccent)
        applyButton.isDisplayed().isEnabled(isEnabled = check != repeat) {
            withTextColor(R.attr.clAccent)
        }
    }

    companion object {
        operator fun invoke(func: RepeatDialogUi.() -> Unit): RepeatDialogUi {
            return RepeatDialogUi().apply { waitOpen { assert() } }.apply(func)
        }
    }
}