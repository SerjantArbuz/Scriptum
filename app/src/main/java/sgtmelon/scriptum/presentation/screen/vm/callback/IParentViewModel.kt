package sgtmelon.scriptum.presentation.screen.vm.callback

import android.os.Bundle
import sgtmelon.scriptum.presentation.screen.vm.impl.ParentViewModel

/**
 * Parent interface for communicate with children of [ParentViewModel].
 */
interface IParentViewModel {

    fun onSetup(bundle: Bundle? = null)

    fun onDestroy(func: () -> Unit = {})

}