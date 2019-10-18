package sgtmelon.scriptum.screen.ui.callback

import sgtmelon.scriptum.control.bind.BindControl
import sgtmelon.scriptum.model.annotation.Color
import sgtmelon.scriptum.model.key.NoteType
import sgtmelon.scriptum.screen.ui.SplashActivity
import sgtmelon.scriptum.screen.vm.SplashViewModel

/**
 * Interface for communication [SplashViewModel] with [SplashActivity]
 */
interface ISplashActivity : ISplashBridge {

    fun startIntroActivity()

    fun startMainActivity()

    fun startAlarmActivity(id: Long, @Color color: Int)

    fun startNoteActivity(id: Long, type: NoteType)

    fun startNotificationActivity()

}