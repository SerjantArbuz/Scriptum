package sgtmelon.scriptum.interactor.main

import android.content.Context
import sgtmelon.scriptum.control.alarm.AlarmControl
import sgtmelon.scriptum.control.bind.BindControl


import sgtmelon.scriptum.interactor.ParentInteractor
import sgtmelon.scriptum.interactor.callback.main.INotesInteractor
import sgtmelon.scriptum.model.NoteModel
import sgtmelon.scriptum.model.annotation.Theme
import sgtmelon.scriptum.model.key.NoteType
import sgtmelon.scriptum.receiver.AlarmReceiver
import sgtmelon.scriptum.repository.bind.BindRepo
import sgtmelon.scriptum.repository.bind.IBindRepo
import sgtmelon.scriptum.room.entity.NoteEntity
import sgtmelon.scriptum.screen.ui.callback.main.INotesBridge
import sgtmelon.scriptum.screen.vm.main.NotesViewModel

/**
 * Interactor for [NotesViewModel]
 */
class NotesInteractor(context: Context, private var callback: INotesBridge?) :
        ParentInteractor(context),
        INotesInteractor {

    private val iBindRepo: IBindRepo = BindRepo(context)

    override fun onDestroy(func: () -> Unit) = super.onDestroy { callback = null }


    @Theme override val theme: Int get() = iPreferenceRepo.theme

    override fun getList() = iRoomRepo.getNoteModelList(bin = false)

    override fun isListHide() = iRoomRepo.isListHide(bin = false)

    override fun updateNote(noteEntity: NoteEntity, callback: BindControl.Bridge.Notify?) {
        iRoomRepo.updateNote(noteEntity)

        val noteModel = NoteModel(noteEntity, iBindRepo.getRollList(noteEntity.id))
        callback?.notifyBind(noteModel, iRoomRepo.getRankIdVisibleList())
    }

    override fun convert(noteModel: NoteModel, callback: BindControl.Bridge.Notify?): NoteModel {
        when (noteModel.noteEntity.type) {
            NoteType.TEXT -> iRoomRepo.convertToRoll(noteModel)
            NoteType.ROLL -> {
                iRoomRepo.convertToText(noteModel)

                /**
                 * Optimisation for get only first 4 items
                 */
                if (noteModel.rollList.size > 4) {
                    noteModel.rollList.dropLast(n = noteModel.rollList.size - 4)
                }
            }
        }

        callback?.notifyBind(noteModel, iRoomRepo.getRankIdVisibleList())

        return noteModel
    }

    override suspend fun deleteNote(noteModel: NoteModel, alarmCallback: AlarmControl.Bridge.Cancel?,
                                    bindCallback: BindControl.Bridge.Cancel?) {
        iRoomRepo.deleteNote(noteModel)

        alarmCallback?.cancelAlarm(AlarmReceiver[noteModel.noteEntity])
        bindCallback?.cancelBind(noteModel.noteEntity.id.toInt())
    }

}