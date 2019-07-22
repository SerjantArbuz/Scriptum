package sgtmelon.scriptum.screen.vm.callback

import sgtmelon.scriptum.screen.ui.DevelopActivity
import sgtmelon.scriptum.screen.vm.DevelopViewModel

/**
 * Interface for communication [DevelopActivity] with [DevelopViewModel]
 *
 * @author SerjantArbuz
 */
interface IDevelopViewModel : IParentViewModel {

    fun onSetup(): Any

    fun onIntroClick()

}