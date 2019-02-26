package sgtmelon.scriptum.app.adapter.holder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import sgtmelon.scriptum.R
import sgtmelon.scriptum.app.adapter.SortAdapter
import sgtmelon.scriptum.app.model.item.SortItem
import sgtmelon.scriptum.databinding.ItemSortBinding
import sgtmelon.scriptum.office.intf.ItemListener

/**
 * Держатель сортировки для [SortAdapter]
 */
class SortHolder(private val binding: ItemSortBinding,
                 private val clickListener: ItemListener.ClickListener
) : RecyclerView.ViewHolder(binding.root) {

    private val clickView: View = itemView.findViewById(R.id.sort_click_container)

    init {
        clickView.setOnClickListener { v -> clickListener.onItemClick(v, adapterPosition)}
    }

    fun bind(sortItem: SortItem, position: Int, sortEnd: Int) {
        binding.sortItem = sortItem
        binding.position = position
        binding.sortEnd = sortEnd
        binding.executePendingBindings()
    }

}