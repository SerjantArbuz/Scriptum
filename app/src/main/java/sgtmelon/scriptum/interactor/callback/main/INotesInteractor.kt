package sgtmelon.scriptum.interactor.callback.main

import sgtmelon.scriptum.interactor.callback.IParentInteractor
import sgtmelon.scriptum.interactor.main.NotesInteractor
import sgtmelon.scriptum.model.annotation.Sort
import sgtmelon.scriptum.model.annotation.Theme
import sgtmelon.scriptum.model.item.NoteItem
import sgtmelon.scriptum.model.item.NotificationItem
import sgtmelon.scriptum.presentation.screen.vm.main.NotesViewModel
import java.util.*

/**
 * Interface for communication [NotesViewModel] with [NotesInteractor]
 */
interface INotesInteractor : IParentInteractor {

    @Theme val theme: Int

    @Sort val sort: Int


    suspend fun getCount(): Int

    suspend fun getList(): MutableList<NoteItem>

    suspend fun isListHide(): Boolean

    suspend fun updateNote(noteItem: NoteItem)

    suspend fun convert(noteItem: NoteItem)


    suspend fun getDateList(): List<String>

    suspend fun clearDate(noteItem: NoteItem)

    suspend fun setDate(noteItem: NoteItem, calendar: Calendar)


    suspend fun copy(noteItem: NoteItem)

    suspend fun deleteNote(noteItem: NoteItem)


    suspend fun getNotification(noteId: Long): NotificationItem?

}