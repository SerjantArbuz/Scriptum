package sgtmelon.scriptum.presentation.adapter.holder

import android.view.View
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView
import sgtmelon.scriptum.R
import sgtmelon.scriptum.databinding.ItemRollReadBinding
import sgtmelon.scriptum.domain.model.item.RollItem
import sgtmelon.scriptum.domain.model.state.NoteState
import sgtmelon.scriptum.extension.checkNoPosition
import sgtmelon.scriptum.presentation.adapter.RollAdapter
import sgtmelon.scriptum.presentation.listener.ItemListener

/**
 * Holder of note roll row read state, use in [RollAdapter].
 */
class RollReadHolder(
        private val binding: ItemRollReadBinding,
        private val clickListener: ItemListener.ActionClick,
        private val longClickListener: ItemListener.LongClick
) : RecyclerView.ViewHolder(binding.root) {

    /**
     * Button which displays above [rollCheck] for ripple effect on click
     */
    private val clickView: View = itemView.findViewById(R.id.roll_read_click_button)
    private val rollCheck: CheckBox = itemView.findViewById(R.id.roll_read_check)

    init {
        clickView.apply {
            setOnClickListener {
                checkNoPosition {
                    clickListener.onItemClick(it, adapterPosition) { rollCheck.toggle() }
                }
            }

            setOnLongClickListener {
                checkNoPosition { longClickListener.onItemLongClick(it, adapterPosition) }
                return@setOnLongClickListener true
            }
        }
    }

    fun bind(item: RollItem, noteState: NoteState?, isToggleCheck: Boolean) = binding.apply {
        this.item = item
        this.isBin = noteState?.isBin == true
        this.isToggleCheck = isToggleCheck
    }.executePendingBindings()

}