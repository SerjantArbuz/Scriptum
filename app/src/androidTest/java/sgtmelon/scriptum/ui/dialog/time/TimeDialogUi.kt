package sgtmelon.scriptum.ui.dialog.time

import sgtmelon.safedialog.time.TimeDialog
import sgtmelon.scriptum.R
import sgtmelon.scriptum.basic.extension.click
import sgtmelon.scriptum.basic.extension.isDisplayed
import sgtmelon.scriptum.basic.extension.isEnabled
import sgtmelon.scriptum.basic.extension.withTextColor
import sgtmelon.scriptum.ui.IDialogUi
import sgtmelon.scriptum.ui.ParentUi
import java.util.*

/**
 * Class for UI control [TimeDialog]
 */
class TimeDialogUi(
    private val callback: DateTimeCallback,
    private val calendar: Calendar,
    private val dateList: List<String>
) : ParentUi(), IDialogUi {

    //region Views

    private val cancelButton = getViewByText(R.string.dialog_button_cancel)
    private val applyButton = getViewByText(R.string.dialog_button_apply)

    //endregion

    fun onTime(min: Int) = apply {
        calendar.add(Calendar.MINUTE, min)

        TimeDialog.callback?.updateTime(calendar)

        waitOperation { assert() }
    }

    fun onTime(calendar: Calendar) = apply {
        this.calendar.apply {
            set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY))
            set(Calendar.MINUTE, calendar.get(Calendar.MINUTE))
        }

        TimeDialog.callback?.updateTime(calendar)

        waitOperation { assert() }
    }

    fun onClickCancel() = waitClose { cancelButton.click() }

    fun onClickApply() = waitClose {
        if (!applyEnabled) throw IllegalAccessException("Apply button not enabled")

        applyButton.click()
        callback.onTimeDialogResult(calendar)
    }


    fun assert() {
        cancelButton.isDisplayed().isEnabled().withTextColor(R.attr.clAccent)
        applyButton.isDisplayed().isEnabled(applyEnabled) { withTextColor(R.attr.clAccent) }
    }

    private val applyEnabled get() = TimeDialog.getPositiveEnabled(calendar, dateList)

    companion object {
        operator fun invoke(
            func: TimeDialogUi.() -> Unit,
            calendar: Calendar,
            dateList: List<String>,
            callback: DateTimeCallback
        ): TimeDialogUi {
            return TimeDialogUi(callback, calendar, dateList)
                .apply { waitOpen { assert() } }
                .apply(func)
        }
    }
}