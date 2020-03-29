package sgtmelon.scriptum.presentation.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import sgtmelon.scriptum.R
import sgtmelon.scriptum.domain.model.annotation.Theme
import sgtmelon.scriptum.domain.model.item.NoteItem
import sgtmelon.scriptum.domain.model.key.NoteType
import sgtmelon.scriptum.extension.clearAddAll
import sgtmelon.scriptum.extension.inflateBinding
import sgtmelon.scriptum.presentation.adapter.diff.NoteDiff
import sgtmelon.scriptum.presentation.adapter.holder.NoteRollHolder
import sgtmelon.scriptum.presentation.adapter.holder.NoteTextHolder
import sgtmelon.scriptum.presentation.listener.ItemListener
import sgtmelon.scriptum.presentation.screen.ui.impl.main.BinFragment
import sgtmelon.scriptum.presentation.screen.ui.impl.main.NotesFragment

/**
 * Adapter which displays list of notes for [NotesFragment], [BinFragment]
 */
class NoteAdapter(
        private val clickListener: ItemListener.Click,
        private val longClickListener: ItemListener.LongClick? = null
) : ParentDiffAdapter<NoteItem, NoteDiff, RecyclerView.ViewHolder>() {

    @Theme var theme: Int = Theme.UNDEFINED


    override val diff = NoteDiff()

    override fun setList(list: List<NoteItem>) = apply {
        super.setList(list)
        this.list.clearAddAll(ArrayList(list.map { it.deepCopy() }))
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = when (viewType) {
            NoteType.TEXT.ordinal -> NoteTextHolder(
                    parent.inflateBinding(R.layout.item_note_text),
                    clickListener, longClickListener
            )
            else -> NoteRollHolder(
                    parent.inflateBinding(R.layout.item_note_roll),
                    clickListener, longClickListener
            )
        }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is NoteTextHolder -> holder.bind(theme, list[position])
            is NoteRollHolder -> holder.bind(theme, list[position])
        }
    }

    override fun getItemViewType(position: Int) = list[position].type.ordinal

}