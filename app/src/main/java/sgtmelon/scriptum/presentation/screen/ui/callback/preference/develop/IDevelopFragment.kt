package sgtmelon.scriptum.presentation.screen.ui.callback.preference.develop

import androidx.annotation.StringRes
import sgtmelon.scriptum.domain.model.key.PrintType
import sgtmelon.scriptum.presentation.screen.ui.impl.preference.develop.DevelopFragment
import sgtmelon.scriptum.presentation.screen.vm.callback.preference.develop.IDevelopViewModel

/**
 * Interface for communication [IDevelopViewModel] with [DevelopFragment].
 */
interface IDevelopFragment {

    fun setupPrints()

    fun setupScreens()

    fun setupService()

    fun setupOther()

    fun showToast(@StringRes stringId: Int)


    fun openPrintScreen(type: PrintType)

    fun openAlarmScreen(noteId: Long)

}