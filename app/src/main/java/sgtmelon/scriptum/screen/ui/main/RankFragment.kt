package sgtmelon.scriptum.screen.ui.main

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import sgtmelon.scriptum.R
import sgtmelon.scriptum.adapter.RankAdapter
import sgtmelon.scriptum.control.touch.RankTouchControl
import sgtmelon.scriptum.databinding.FragmentRankBinding
import sgtmelon.scriptum.extension.addTextChangedListener
import sgtmelon.scriptum.extension.createVisibleAnim
import sgtmelon.scriptum.extension.inflateBinding
import sgtmelon.scriptum.factory.DialogFactory
import sgtmelon.scriptum.listener.ItemListener
import sgtmelon.scriptum.model.state.OpenState
import sgtmelon.scriptum.room.entity.RankEntity
import sgtmelon.scriptum.screen.ui.callback.main.IRankFragment
import sgtmelon.scriptum.screen.vm.callback.main.IRankViewModel
import sgtmelon.scriptum.screen.vm.main.RankViewModel

/**
 * Фрагмент для отображения списка категорий - [RankEntity]
 *
 * @author SerjantArbuz
 */
class RankFragment : Fragment(), IRankFragment {

    private var binding: FragmentRankBinding? = null

    private val iViewModel: IRankViewModel by lazy {
        ViewModelProviders.of(this).get(RankViewModel::class.java).apply {
            callback = this@RankFragment
        }
    }

    private val openState = OpenState()
    private val renameDialog by lazy { DialogFactory.Main.getRenameDialog(fragmentManager) }

    private val adapter by lazy {
        RankAdapter(ItemListener.Click { view, p ->
            when (view.id) {
                R.id.rank_visible_button -> iViewModel.onClickVisible(p)
                R.id.rank_click_container -> iViewModel.onShowRenameDialog(p)
                R.id.rank_cancel_button -> iViewModel.onClickCancel(p)
            }
        }, ItemListener.LongClick { _, p -> iViewModel.onLongClickVisible(p) })
    }
    private val layoutManager by lazy { LinearLayoutManager(context) }

    val enterCard: View? get() = view?.findViewById(R.id.toolbar_rank_card)

    private var nameEnter: EditText? = null
    private var parentContainer: ViewGroup? = null
    private var emptyInfoView: View? = null

    private var recyclerView: RecyclerView? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = inflater.inflateBinding(R.layout.fragment_rank, container)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        openState.get(savedInstanceState)

        setupToolbar()
        setupRecycler()
    }

    override fun onResume() {
        super.onResume()
        iViewModel.onUpdateData()
    }

    override fun onDestroy() {
        super.onDestroy()
        iViewModel.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) =
            super.onSaveInstanceState(outState.apply { openState.save(bundle = this) })

    private fun setupToolbar() {
        view?.findViewById<Toolbar>(R.id.toolbar_rank_container)?.apply {
            title = getString(R.string.title_rank)
        }

        view?.findViewById<ImageButton>(R.id.toolbar_rank_cancel_button)?.apply {
            setOnClickListener { iViewModel.onClickEnterCancel() }
        }

        view?.findViewById<ImageButton>(R.id.toolbar_rank_add_button)?.apply {
            setOnClickListener { iViewModel.onClickEnterAdd(simpleClick = true) }
            setOnLongClickListener {
                iViewModel.onClickEnterAdd(simpleClick = false)
                return@setOnLongClickListener true
            }
        }

        nameEnter = view?.findViewById(R.id.toolbar_rank_enter)
        nameEnter?.let {
            it.addTextChangedListener(on = { iViewModel.onUpdateToolbar() })
            it.setOnEditorActionListener { _, i, _ -> iViewModel.onEditorClick(i) }
        }
    }

    private fun setupRecycler() {
        parentContainer = view?.findViewById(R.id.rank_parent_container)
        emptyInfoView = view?.findViewById(R.id.rank_info_include)

        val touchCallback = RankTouchControl(iViewModel)

        adapter.dragListener = touchCallback

        recyclerView = view?.findViewById(R.id.rank_recycler)
        recyclerView?.let {
            it.itemAnimator = object : DefaultItemAnimator() {
                override fun onAnimationFinished(viewHolder: RecyclerView.ViewHolder) =
                        bindList(adapter.itemCount)
            }
            it.layoutManager = layoutManager
            it.adapter = adapter
        }

        ItemTouchHelper(touchCallback).attachToRecyclerView(recyclerView)

        renameDialog.apply {
            positiveListener = DialogInterface.OnClickListener { _, _ ->
                iViewModel.onRenameDialog(position, name)
            }
            dismissListener = DialogInterface.OnDismissListener { openState.clear() }
        }
    }

    override fun bindList(size: Int) {
        val empty = size == 0

        parentContainer?.createVisibleAnim(emptyInfoView, empty)

        binding?.isListEmpty = empty
        iViewModel.onUpdateToolbar()
    }

    override fun bindToolbar(isClearEnable: Boolean, isAddEnable: Boolean) {
        binding?.apply {
            this.isClearEnable = isClearEnable
            this.isAddEnable = isAddEnable
        }?.executePendingBindings()
    }

    override fun scrollTop() {
        recyclerView?.smoothScrollToPosition(0)
    }

    override fun getEnterText() = nameEnter?.text?.toString() ?: ""

    override fun clearEnter(): String {
        val name = nameEnter?.text?.toString() ?: ""
        nameEnter?.setText("")
        return name
    }

    override fun scrollToItem(simpleClick: Boolean, list: MutableList<RankEntity>) {
        val p = if (simpleClick) list.size else 0

        if (list.size == 1) {
            adapter.notifyItemInserted(p, list)
            bindList(list.size)
        } else {
            val fastScroll = with(layoutManager) {
                if (simpleClick) {
                    findLastVisibleItemPosition() == p - 2
                } else {
                    findFirstVisibleItemPosition() == p
                }
            }

            if (fastScroll) {
                recyclerView?.scrollToPosition(p)
                adapter.notifyItemInserted(p, list)
            } else {
                recyclerView?.smoothScrollToPosition(p)
                adapter.notifyDataSetChanged(list)
            }
        }
    }

    override fun showRenameDialog(p: Int, name: String, nameList: ArrayList<String>) {
        fragmentManager?.let {
            openState.tryInvoke {
                renameDialog.apply {
                    setArguments(p, name, nameList)
                }.show(it, DialogFactory.Main.RENAME)
            }
        }
    }

    override fun notifyVisible(p: Int, item: RankEntity) = adapter.setListItem(p, item)

    override fun notifyVisible(startAnim: BooleanArray, list: MutableList<RankEntity>) =
            adapter.apply {
                setList(list)
                this.startAnim = startAnim
            }.notifyDataSetChanged()

    override fun notifyDataSetChanged(list: MutableList<RankEntity>) =
            adapter.notifyDataSetChanged(list)

    override fun notifyItemChanged(p: Int, item: RankEntity) =
            adapter.notifyItemChanged(item, p)

    override fun notifyItemRemoved(p: Int, list: MutableList<RankEntity>) =
            adapter.notifyItemRemoved(p, list)

    override fun notifyItemMoved(from: Int, to: Int, list: MutableList<RankEntity>) =
            adapter.notifyItemMoved(from, to, list)

}