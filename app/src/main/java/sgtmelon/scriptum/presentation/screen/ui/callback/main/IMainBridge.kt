package sgtmelon.scriptum.presentation.screen.ui.callback.main

import sgtmelon.scriptum.control.alarm.AlarmControl
import sgtmelon.scriptum.control.bind.BindControl
import sgtmelon.scriptum.interactor.main.MainInteractor
import sgtmelon.scriptum.presentation.screen.ui.impl.main.MainActivity

/**
 * Interface for communication [MainInteractor] with [MainActivity]
 */
interface IMainBridge : AlarmControl.Bridge.Full,
        BindControl.NoteBridge.Notify,
        BindControl.InfoBridge