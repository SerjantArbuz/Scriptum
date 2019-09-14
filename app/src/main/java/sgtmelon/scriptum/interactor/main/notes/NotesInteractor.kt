package sgtmelon.scriptum.interactor.main.notes

import android.content.Context
import sgtmelon.scriptum.control.alarm.callback.AlarmCallback
import sgtmelon.scriptum.interactor.ParentInteractor
import sgtmelon.scriptum.model.NoteModel
import sgtmelon.scriptum.model.annotation.Theme
import sgtmelon.scriptum.model.key.NoteType
import sgtmelon.scriptum.receiver.AlarmReceiver
import sgtmelon.scriptum.room.entity.NoteEntity
import sgtmelon.scriptum.screen.vm.main.NotesViewModel

/**
 * Interactor for [NotesViewModel]
 */
class NotesInteractor(context: Context) : ParentInteractor(context), INotesInteractor {

    @Theme override val theme: Int get() = iPreferenceRepo.theme

    override fun getList() = iRoomRepo.getNoteModelList(bin = false)

    override fun isListHide() = iRoomRepo.isListHide(bin = false)

    override fun updateNote(noteEntity: NoteEntity) = iRoomRepo.updateNote(noteEntity)

    override fun convert(noteModel: NoteModel) = when (noteModel.noteEntity.type) {
        NoteType.TEXT -> iRoomRepo.convertToRoll(noteModel)
        NoteType.ROLL -> iRoomRepo.convertToText(noteModel)
    }

    override suspend fun deleteNote(noteModel: NoteModel, callback: AlarmCallback.Cancel?) {
        callback?.cancelAlarm(AlarmReceiver[noteModel.noteEntity])
        iRoomRepo.deleteNote(noteModel)
    }

}