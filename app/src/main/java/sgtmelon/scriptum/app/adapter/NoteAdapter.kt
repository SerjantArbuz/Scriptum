package sgtmelon.scriptum.app.adapter

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import sgtmelon.scriptum.R
import sgtmelon.scriptum.app.adapter.holder.NoteRollHolder
import sgtmelon.scriptum.app.adapter.holder.NoteTextHolder
import sgtmelon.scriptum.app.model.NoteRepo
import sgtmelon.scriptum.app.model.key.NoteType
import sgtmelon.scriptum.app.screen.main.bin.BinFragment
import sgtmelon.scriptum.app.screen.main.notes.NotesFragment
import sgtmelon.scriptum.databinding.ItemNoteRollBinding
import sgtmelon.scriptum.databinding.ItemNoteTextBinding
import sgtmelon.scriptum.office.intf.ItemListener
import sgtmelon.scriptum.office.utils.AppUtils.inflateBinding

/**
 * Адаптер списка заметок для [NotesFragment], [BinFragment]
 */
class NoteAdapter(context: Context) : ParentAdapter<NoteRepo, RecyclerView.ViewHolder>(context) {

    lateinit var clickListener: ItemListener.ClickListener
    lateinit var longClickListener: ItemListener.LongClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType == NoteType.TEXT.ordinal) {
            true -> {
                val binding: ItemNoteTextBinding =
                        inflater.inflateBinding(R.layout.item_note_text, parent)

                NoteTextHolder(binding, clickListener, longClickListener)
            }
            false -> {
                val binding: ItemNoteRollBinding =
                        inflater.inflateBinding(R.layout.item_note_roll, parent)

                NoteRollHolder(binding, clickListener, longClickListener)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val noteRepo = list[position]

        when (holder) {
            is NoteTextHolder -> holder.bind(noteRepo.noteItem)
            is NoteRollHolder -> holder.bind(noteRepo.noteItem, noteRepo.listRoll)
        }
    }

    override fun getItemViewType(position: Int) = list[position].noteItem.type.ordinal

}