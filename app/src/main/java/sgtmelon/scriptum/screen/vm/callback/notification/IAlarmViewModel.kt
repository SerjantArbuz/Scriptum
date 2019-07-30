package sgtmelon.scriptum.screen.vm.callback.notification

import android.os.Bundle
import sgtmelon.scriptum.screen.ui.notification.AlarmActivity
import sgtmelon.scriptum.screen.vm.callback.IParentViewModel
import sgtmelon.scriptum.screen.vm.notification.AlarmViewModel

/**
 * Interface for communication [AlarmActivity] with [AlarmViewModel]
 *
 * @author SerjantArbuz
 */
interface IAlarmViewModel: IParentViewModel {

    fun onSetup()

    fun onSetupData(bundle: Bundle?)

    fun onStart()

    fun onSaveData(bundle: Bundle)

    fun onClickNote()

    fun onClickDisable()

    fun onClickPostpone()

}