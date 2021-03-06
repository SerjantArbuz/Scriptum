package sgtmelon.scriptum.presentation.screen.ui.impl.main

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Rect
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import sgtmelon.safedialog.safeShow
import sgtmelon.scriptum.R
import sgtmelon.scriptum.domain.model.data.ReceiverData.Filter
import sgtmelon.scriptum.domain.model.key.MainPage
import sgtmelon.scriptum.domain.model.key.NoteType
import sgtmelon.scriptum.domain.model.state.OpenState
import sgtmelon.scriptum.extension.*
import sgtmelon.scriptum.idling.AppIdlingResource
import sgtmelon.scriptum.idling.IdlingTag
import sgtmelon.scriptum.presentation.control.toolbar.show.HolderShowControl
import sgtmelon.scriptum.presentation.factory.DialogFactory
import sgtmelon.scriptum.presentation.factory.FragmentFactory
import sgtmelon.scriptum.presentation.receiver.screen.MainScreenReceiver
import sgtmelon.scriptum.presentation.screen.ui.ScriptumApplication
import sgtmelon.scriptum.presentation.screen.ui.callback.main.IMainActivity
import sgtmelon.scriptum.presentation.screen.ui.impl.AppActivity
import sgtmelon.scriptum.presentation.screen.ui.impl.note.NoteActivity
import sgtmelon.scriptum.presentation.screen.vm.callback.main.IMainViewModel
import java.util.*
import javax.inject.Inject

/**
 * Screen which displays main menu and fragments: [RankFragment], [NotesFragment], [BinFragment].
 */
class MainActivity : AppActivity(), IMainActivity {

    @Inject internal lateinit var viewModel: IMainViewModel

    private val holderControl by lazy { HolderShowControl[toolbarHolder] }

    private val mainReceiver by lazy { MainScreenReceiver[viewModel, viewModel] }

    private val fragmentFactory = FragmentFactory.Main(fm)
    private val rankFragment by lazy { fragmentFactory.getRankFragment() }
    private val notesFragment by lazy { fragmentFactory.getNotesFragment() }
    private val binFragment by lazy { fragmentFactory.getBinFragment() }

    override val openState = OpenState()

    private val dialogFactory by lazy { DialogFactory.Main(context = this, fm = fm) }
    private val addDialog by lazy { dialogFactory.getAddDialog() }

    private val toolbarHolder by lazy { findViewById<View?>(R.id.main_toolbar_holder) }
    private val parentContainer by lazy { findViewById<ViewGroup?>(R.id.main_parent_container) }
    private val fab by lazy { findViewById<FloatingActionButton?>(R.id.main_add_fab) }
    private val menuNavigation by lazy { findViewById<BottomNavigationView>(R.id.main_menu_navigation) }

    override fun onCreate(savedInstanceState: Bundle?) {
        ScriptumApplication.component.getMainBuilder().set(activity = this).build()
                .inject(activity = this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        openState.get(savedInstanceState)

        viewModel.onSetup(savedInstanceState)

        registerReceiver(mainReceiver, IntentFilter(Filter.MAIN))

        AppIdlingResource.getInstance().stopWork(IdlingTag.Intro.FINISH)
    }

    override fun onResume() {
        super.onResume()

        /**
         * Clear [openState] after click on item container.
         */
        openState.changeEnabled = true
        openState.clear()

        /**
         * Show FAB on return to screen if it possible.
         */
        onFabStateChange(state = true)
    }

    override fun onDestroy() {
        super.onDestroy()

        openState.clearBlockCallback()

        holderControl.onDestroy()

        viewModel.onDestroy()
        unregisterReceiver(mainReceiver)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        openState.save(outState)
        viewModel.onSaveData(outState)
    }

    /**
     * If touch was outside of [RankFragment.enterCard], when need hide keyboard
     */
    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (ev?.action != MotionEvent.ACTION_DOWN) return super.dispatchTouchEvent(ev)

        rankFragment.enterCard?.let { if (!ev.onView(it)) hideKeyboard() }

        return super.dispatchTouchEvent(ev)
    }

    override fun setupNavigation(@IdRes itemId: Int) {
        fab?.setOnClickListener {
            openState.tryInvoke { addDialog.safeShow(fm, DialogFactory.Main.ADD) }
        }

        val animTime = resources.getInteger(R.integer.fade_anim_time).toLong()
        menuNavigation?.setOnNavigationItemSelectedListener {
            return@setOnNavigationItemSelectedListener openState.tryReturnInvoke {
                openState.block(animTime)
                viewModel.onSelectItem(it.itemId)

                return@tryReturnInvoke true
            } ?: false
        }
        menuNavigation?.selectedItemId = itemId

        addDialog.apply {
            itemSelectedListener = NavigationView.OnNavigationItemSelectedListener {
                dismiss()
                viewModel.onResultAddDialog(it.itemId)
                return@OnNavigationItemSelectedListener true
            }
            dismissListener = DialogInterface.OnDismissListener { openState.clear() }
        }
    }

    override fun setupInsets() {
        parentContainer?.doOnApplyWindowInsets { view, insets, _, _, margin ->
            view.updateMargin(InsetsDir.LEFT, insets, margin)
            view.updateMargin(InsetsDir.TOP, insets, margin)
            view.updateMargin(InsetsDir.RIGHT, insets, margin)

            /**
             * Need use this function (not [View.updateMargin]), because otherwise snackbar
             * inside [RankFragment] will handle this inset (and this cause strange bottom padding)
             */
            view.addSystemInsetsMargin(InsetsDir.BOTTOM, menuNavigation)

            return@doOnApplyWindowInsets insets
        }
    }


    override fun onFabStateChange(state: Boolean) = viewModel.onFabStateChange(state)

    override fun setFabState(state: Boolean) {
        fab?.setState(state)
    }

    override fun scrollTop(mainPage: MainPage) = onFragmentAdd(mainPage) {
        when (mainPage) {
            MainPage.RANK -> rankFragment.scrollTop()
            MainPage.NOTES -> notesFragment.scrollTop()
            MainPage.BIN -> binFragment.scrollTop()
        }
    }

    override fun showPage(pageFrom: MainPage, pageTo: MainPage) {
        holderControl.show()

        with(fm) {
            beginTransaction().apply {
                setCustomAnimations(R.anim.fade_in, R.anim.fade_out)

                if (findFragmentByTag(pageFrom.getFragmentTag()) != null) {
                    val fragmentFrom = pageFrom.getFragmentByName()

                    hide(fragmentFrom)
                    fragmentFrom.onPause()

                    /**
                     * Dismiss without callback happen inside [Fragment.onStop] function.
                     * So be careful when call it manually.
                     */
                    if (fragmentFrom is RankFragment) {
                        fragmentFrom.dismissSnackbar()
                    }
                }

                val fragmentTo = pageTo.getFragmentByName()
                val fragmentToTag = pageTo.getFragmentTag()

                if (findFragmentByTag(fragmentToTag) != null) {
                    show(fragmentTo)
                    fragmentTo.onResume()
                } else {
                    add(R.id.main_fragment_container, fragmentTo, fragmentToTag)
                }

                commit()
            }
        }
    }

    override fun openNoteScreen(noteType: NoteType) = openState.tryInvoke {
        startActivity(NoteActivity[this, noteType.ordinal])
    }

    //region Receiver callback

    override fun onReceiveUnbindNote(id: Long) {
        onFragmentAdd(MainPage.RANK) { rankFragment.onReceiveUnbindNote(id) }
        onFragmentAdd(MainPage.NOTES) { notesFragment.onReceiveUnbindNote(id) }
    }

    override fun onReceiveUpdateAlarm(id: Long) {
        onFragmentAdd(MainPage.NOTES) { notesFragment.onReceiveUpdateAlarm(id) }
    }

    //endregion

    private fun MainPage.getFragmentByName(): Fragment = when (this) {
        MainPage.RANK -> rankFragment
        MainPage.NOTES -> notesFragment
        MainPage.BIN -> binFragment
    }

    @FragmentFactory.Main.Tag
    private fun MainPage.getFragmentTag(): String = when (this) {
        MainPage.RANK -> FragmentFactory.Main.Tag.RANK
        MainPage.NOTES -> FragmentFactory.Main.Tag.NOTES
        MainPage.BIN -> FragmentFactory.Main.Tag.BIN
    }

    private fun FloatingActionButton.setState(isVisible: Boolean) {
        if (isVisible) show() else hide()
        isEnabled = isVisible
    }

    /**
     * Func return was [MotionEvent] happen on [view]
     */
    private fun MotionEvent?.onView(view: View?): Boolean {
        if (view == null || this == null) return false

        return Rect().apply {
            view.getGlobalVisibleRect(this)
        }.contains(rawX.toInt(), rawY.toInt())
    }

    /**
     * Call [func] only if correct fragment added.
     */
    private fun onFragmentAdd(mainPage: MainPage, func: () -> Unit) {
        if (fm.findFragmentByTag(mainPage.getFragmentTag()) == null) return

        func()
    }

    companion object {
        operator fun get(context: Context) = Intent(context, MainActivity::class.java)
    }

}