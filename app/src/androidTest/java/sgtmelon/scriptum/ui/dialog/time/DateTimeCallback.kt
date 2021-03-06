package sgtmelon.scriptum.ui.dialog.time

import java.util.*

/**
 * Interface for results of [DateDialogUi] and [TimeDialogUi]
 */
interface DateTimeCallback {

    fun onDateDialogResetResult()

    fun onTimeDialogResult(calendar: Calendar)

}