package sgtmelon.scriptum.app.view.activity

import android.content.DialogInterface
import android.os.Bundle
import android.view.MenuItem
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import sgtmelon.scriptum.R
import sgtmelon.scriptum.app.factory.DialogFactory
import sgtmelon.scriptum.app.factory.FragmentFactory
import sgtmelon.scriptum.app.view.callback.MainCallback
import sgtmelon.scriptum.app.view.fragment.main.NotesFragment
import sgtmelon.scriptum.app.view.parent.BaseActivityParent
import sgtmelon.scriptum.office.annot.def.DialogDef
import sgtmelon.scriptum.office.annot.def.IntentDef
import sgtmelon.scriptum.office.annot.key.MainPage
import sgtmelon.scriptum.office.annot.key.NoteType
import sgtmelon.scriptum.office.state.OpenState
import sgtmelon.scriptum.office.utils.AppUtils.setState

class MainActivity : BaseActivityParent(),
        MainCallback,
        BottomNavigationView.OnNavigationItemSelectedListener {

    companion object {
        // TODO: 28.01.2019 перевести приложение на Kotlin + RxJava + Spek
        // TODO: 13.01.2019 Добавить getAdapterPosition safety - RecyclerView.NO_POSITION check
        // TODO: 16.01.2019 сделать блокировку кнопки изменить сохранить при работе анимации крестик-стрелка (если анимируется - не нажимать)
        // TODO: 19.01.2019 Добавить перескакивание курсора при старте редактирования в нужное место
        // TODO: 20.01.2019 Разобраться со стилями
        // TODO: 27.01.2019 Добавить ещё одну тему
        // TODO: 22.11.2018 аннотация профессор
    }

    private var firstStart: Boolean = true
    private var page: Int = MainPage.Name.NOTES.ordinal

    private val openState = OpenState()

    private val fm by lazy { supportFragmentManager }

    private val rankFragment by lazy { FragmentFactory.getRankFragment(fm) }
    private val notesFragment by lazy { FragmentFactory.getNotesFragment(fm) }
    private val binFragment by lazy { FragmentFactory.getBinFragment(fm) }

    private val sheetDialog by lazy { DialogFactory.getSheetDialog(fm) }

    private val fab: FloatingActionButton by lazy {
        findViewById<FloatingActionButton>(R.id.main_add_fab)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState != null) {
            page = savedInstanceState.getInt(IntentDef.STATE_PAGE)
            openState.isOpen = savedInstanceState.getBoolean(IntentDef.STATE_OPEN)
        }

        setupNavigation()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putInt(IntentDef.STATE_PAGE, page)
        outState.putBoolean(IntentDef.STATE_OPEN, openState.isOpen)
    }

    private fun setupNavigation() {
        fab.setOnClickListener {// TODO сделать с помощью extension
            if (!openState.isOpen) {
                openState.isOpen = true

                sheetDialog.setArguments(R.layout.sheet_add, R.id.add_navigation)
                sheetDialog.show(supportFragmentManager, DialogDef.SHEET)
            }
        }

        val navigationView = findViewById<BottomNavigationView>(R.id.main_menu_navigation)
        navigationView.setOnNavigationItemSelectedListener(this)
        navigationView.selectedItemId = MainPage.id[page]

        sheetDialog.itemSelectedListener = NavigationView.OnNavigationItemSelectedListener {
            sheetDialog.dismiss()

            val type = when (it.itemId) {
                R.id.item_add_text -> NoteType.TEXT
                else -> NoteType.ROLL
            }

            startActivity(NoteActivity.getIntent(this@MainActivity, type))
            return@OnNavigationItemSelectedListener true
        }
        sheetDialog.dismissListener = DialogInterface.OnDismissListener { openState.isOpen = false }
    }

    override fun changeFabState(state: Boolean) = fab.setState(state)

    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean { // TODO Оптимизировать
        val pageFrom = enumValues<MainPage.Name>()[page]
        val pageTo = when (menuItem.itemId) {
            R.id.item_page_rank -> MainPage.Name.RANK
            R.id.item_page_notes -> MainPage.Name.NOTES
            else -> MainPage.Name.BIN
        }

        page = pageTo.ordinal

        val transaction = fm.beginTransaction()
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)

        if (!firstStart && pageTo == pageFrom) {
            when (pageTo) {
                MainPage.Name.RANK -> rankFragment.scrollTop()
                MainPage.Name.NOTES -> notesFragment.scrollTop()
                MainPage.Name.BIN -> binFragment.scrollTop()
            }
        } else {
            if (firstStart) firstStart = false

            val fragmentTo: Fragment = when (pageTo) {
                MainPage.Name.RANK -> rankFragment
                MainPage.Name.NOTES -> notesFragment
                MainPage.Name.BIN -> binFragment
            }

            changeFabState(fragmentTo is NotesFragment)

            if (fm.findFragmentByTag(pageTo.name) != null) {
                transaction.show(fragmentTo)
                fragmentTo.onResume()
            } else {
                transaction.add(R.id.main_fragment_container, fragmentTo, pageTo.name)
            }

            if (fm.findFragmentByTag(pageFrom.name) != null) {
                transaction.hide(when (pageFrom) {
                    MainPage.Name.RANK -> rankFragment
                    MainPage.Name.NOTES -> notesFragment
                    MainPage.Name.BIN -> binFragment
                })
            }
        }

        transaction.commit()
        return true
    }

}