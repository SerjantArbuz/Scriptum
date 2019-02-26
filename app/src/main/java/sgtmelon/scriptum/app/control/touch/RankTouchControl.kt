package sgtmelon.scriptum.app.control.touch

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import sgtmelon.scriptum.app.adapter.RankAdapter
import sgtmelon.scriptum.app.screen.main.rank.RankFragment
import sgtmelon.scriptum.office.intf.ItemListener

/**
 * Управление перетаскиванием для [RankFragment]
 */
class RankTouchControl(private val callback: Result) : ItemTouchHelper.Callback(),
        ItemListener.DragListener {

    lateinit var adapter: RankAdapter

    private var drag = false

    override fun setDrag(drag: Boolean) {
        this.drag = drag
    }

    private var dragFrom: Int = RecyclerView.NO_POSITION

    override fun getMovementFlags(recyclerView: RecyclerView,
                                  viewHolder: RecyclerView.ViewHolder): Int {
        val flagsDrag = when (drag) {
            true -> ItemTouchHelper.UP or ItemTouchHelper.DOWN
            false -> 0
        }

        val flagsSwipe = 0

        return ItemTouchHelper.Callback.makeMovementFlags(flagsDrag, flagsSwipe)
    }

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        super.onSelectedChanged(viewHolder, actionState)

        if (actionState == ItemTouchHelper.ACTION_STATE_DRAG) {
            dragFrom = viewHolder?.adapterPosition ?: RecyclerView.NO_POSITION
        }
    }

    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        super.clearView(recyclerView, viewHolder)

        val dragTo = viewHolder.adapterPosition
        if (dragFrom != dragTo) {
            callback.onTouchClear(dragFrom, dragTo)
        }
    }

    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder,
                        target: RecyclerView.ViewHolder): Boolean {
        callback.onTouchMove(viewHolder.adapterPosition, target.adapterPosition)
        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {}

    interface Result {
        fun onTouchClear(dragFrom: Int, dragTo: Int)

        fun onTouchMove(from: Int, to: Int)
    }

}