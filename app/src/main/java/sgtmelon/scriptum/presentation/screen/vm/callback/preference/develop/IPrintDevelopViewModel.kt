package sgtmelon.scriptum.presentation.screen.vm.callback.preference.develop

import android.os.Bundle
import sgtmelon.scriptum.presentation.screen.ui.callback.preference.develop.IPrintDevelopActivity
import sgtmelon.scriptum.presentation.screen.vm.callback.IParentViewModel
import sgtmelon.scriptum.presentation.screen.vm.impl.preference.develop.PrintDevelopViewModel

/**
 * Interface for communication [IPrintDevelopActivity] with [PrintDevelopViewModel].
 */
interface IPrintDevelopViewModel : IParentViewModel {

    fun onSaveData(bundle: Bundle)

    fun onUpdateData()
}