package sgtmelon.scriptum.interactor.notification

import android.content.Context
import sgtmelon.extension.getDateFormat

import sgtmelon.scriptum.interactor.ParentInteractor
import sgtmelon.scriptum.interactor.callback.notification.IAlarmInteractor
import sgtmelon.scriptum.model.NoteModel
import sgtmelon.scriptum.model.annotation.Theme
import sgtmelon.scriptum.receiver.AlarmReceiver
import sgtmelon.scriptum.repository.alarm.AlarmRepo
import sgtmelon.scriptum.repository.alarm.IAlarmRepo
import sgtmelon.scriptum.screen.ui.callback.notification.IAlarmBridge
import sgtmelon.scriptum.screen.vm.notification.AlarmViewModel
import java.util.*

/**
 * Interactor for [AlarmViewModel]
 */
class AlarmInteractor(context: Context, private var callback: IAlarmBridge?) :
        ParentInteractor(context),
        IAlarmInteractor {

    private val iAlarmRepo: IAlarmRepo = AlarmRepo(context)

    override fun onDestroy(func: () -> Unit) = super.onDestroy { callback = null }


    @Theme override val theme: Int get() = iPreferenceRepo.theme

    override val repeat: Int get() = iPreferenceRepo.repeat

    override val volume: Int get() = iPreferenceRepo.volume

    override val volumeIncrease: Boolean get() = iPreferenceRepo.volumeIncrease

    override fun getModel(id: Long): NoteModel? {
        /**
         * Delete before return noteModel for hide alarm icon
         */
        iAlarmRepo.delete(id)
        return iRoomRepo.getNoteModel(id)
    }

    override fun setupRepeat(noteModel: NoteModel, valueArray: IntArray) {
        val calendar = Calendar.getInstance().apply {
            add(Calendar.MINUTE, valueArray[repeat])
        }

        iAlarmRepo.insertOrUpdate(noteModel.alarmEntity.apply {
            date = getDateFormat().format(calendar.time)
        })

        callback?.setAlarm(calendar, AlarmReceiver[noteModel.noteEntity])
    }

}