package sgtmelon.scriptum.interactor

import android.content.Context
import sgtmelon.extension.beforeNow
import sgtmelon.extension.getCalendar
import sgtmelon.scriptum.interactor.callback.ISplashInteractor
import sgtmelon.scriptum.repository.alarm.AlarmRepo
import sgtmelon.scriptum.repository.alarm.IAlarmRepo
import sgtmelon.scriptum.screen.ui.callback.ISplashBridge
import sgtmelon.scriptum.screen.vm.SplashViewModel

/**
 * Interactor for [SplashViewModel]
 */
class SplashInteractor(context: Context, private var callback: ISplashBridge?) :
        ParentInteractor(context),
        ISplashInteractor {

    private val iAlarmRepo: IAlarmRepo = AlarmRepo(context)

    override fun onDestroy(func: () -> Unit) = super.onDestroy { callback = null }


    override val firstStart: Boolean get() = iPreferenceRepo.firstStart

    override suspend fun tidyUpAlarm() = iAlarmRepo.getList().forEach {
        val calendar = it.alarm.date.getCalendar()
        val id = it.note.id

        if (calendar.beforeNow()) {
            callback?.cancelAlarm(id)
            iAlarmRepo.delete(id)
        } else {
            callback?.setAlarm(calendar, id)
        }
    }

}