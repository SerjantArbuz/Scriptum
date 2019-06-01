package sgtmelon.scriptum.screen.callback.main

import android.content.Intent
import sgtmelon.scriptum.model.NoteModel
import sgtmelon.scriptum.screen.view.main.NotesFragment
import sgtmelon.scriptum.screen.vm.main.NotesViewModel

/**
 * Интерфейс для общения [NotesViewModel] и [NotesFragment]
 *
 * @author SerjantArbuz
 */
interface NotesCallback {

    fun setupToolbar()

    fun setupRecycler(theme: Int)

    fun bind()

    fun scrollTop()

    fun startActivity(intent: Intent)

    fun showOptionsDialog(itemArray: Array<String>, p: Int)

    fun notifyDataSetChanged(list: MutableList<NoteModel>)

    fun notifyItemChanged(p: Int, list: MutableList<NoteModel>)

    fun notifyItemRemoved(p: Int, list: MutableList<NoteModel>)

}