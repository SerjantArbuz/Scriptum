package sgtmelon.scriptum.app.adapter

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import sgtmelon.scriptum.R
import sgtmelon.scriptum.app.adapter.holder.RollReadHolder
import sgtmelon.scriptum.app.adapter.holder.RollWriteHolder
import sgtmelon.scriptum.app.control.input.InputCallback
import sgtmelon.scriptum.app.control.touch.BindIntf
import sgtmelon.scriptum.app.model.item.RollItem
import sgtmelon.scriptum.app.screen.note.roll.RollNoteFragment
import sgtmelon.scriptum.databinding.ItemRollReadBinding
import sgtmelon.scriptum.databinding.ItemRollWriteBinding
import sgtmelon.scriptum.office.intf.ItemListener
import sgtmelon.scriptum.office.state.NoteState
import sgtmelon.scriptum.office.utils.AppUtils.inflateBinding

/**
 * Адаптер для [RollNoteFragment]
 */
class RollAdapter(context: Context,
                  private val clickListener: ItemListener.ClickListener,
                  private val dragListener: ItemListener.DragListener,
                  private val rollWatcher: ItemListener.RollWatcher,
                  private val inputCallback: InputCallback,
                  private val bindIntf: BindIntf
) : ParentAdapter<RollItem, RecyclerView.ViewHolder>(context) {

    lateinit var noteState: NoteState

    var checkToggle: Boolean = false
    var cursorPosition = UNDEFINED

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType == read) {
            true -> {
                val binding: ItemRollReadBinding =
                        inflater.inflateBinding(R.layout.item_roll_read, parent)

                RollReadHolder(binding, clickListener)
            }
            false -> {
                val binding: ItemRollWriteBinding =
                        inflater.inflateBinding(R.layout.item_roll_write, parent)

                RollWriteHolder(binding, dragListener, rollWatcher, inputCallback, bindIntf)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = list[position]

        when (holder) {
            is RollReadHolder -> holder.bind(item, noteState, checkToggle)
            is RollWriteHolder -> {
                holder.bind(item)

                if (cursorPosition != UNDEFINED) {
                    holder.setSelections(cursorPosition)
                    cursorPosition = UNDEFINED
                }
            }
        }
    }

    override fun getItemViewType(position: Int) = when (!noteState.isEdit) {
        true -> read
        false -> write
    }

    private companion object {
        private const val UNDEFINED = -1

        private const val read = 0
        private const val write = 1
    }

}