package sgtmelon.scriptum.ui.screen.main

import androidx.test.espresso.matcher.ViewMatchers.withId
import sgtmelon.scriptum.R
import sgtmelon.scriptum.basic.extension.*
import sgtmelon.scriptum.model.key.MainPage
import sgtmelon.scriptum.screen.ui.main.MainActivity
import sgtmelon.scriptum.ui.ParentUi
import sgtmelon.scriptum.ui.dialog.AddDialogUi

/**
 * Class for UI control of [MainActivity]
 */
class MainScreen : ParentUi() {

    //region Views

    private val parentContainer = withId(R.id.main_parent_container)
    private val toolbarHolder = withId(R.id.main_toolbar_holder)
    private val menuNavigation = withId(R.id.main_menu_navigation)

    private val rankMenuItem = withId(R.id.item_page_rank)
    private val notesMenuItem = withId(R.id.item_page_notes)
    private val binMenuItem = withId(R.id.item_page_bin)

    private val addFab = withId(R.id.main_add_fab)

    //endregion

    private var wasNavigate = false

    fun openPage(page: MainPage, empty: Boolean = false) = when(page) {
        MainPage.RANK -> openRankPage(empty)
        MainPage.NOTES -> openNotesPage(empty)
        MainPage.BIN -> openBinPage(empty)
    }

    fun openRankPage(empty: Boolean = false, func: RankScreen.() -> Unit = {}) {
        wasNavigate = true
        onNavigateTo(MainPage.RANK)

        RankScreen.invoke(func, empty)
    }

    fun openNotesPage(empty: Boolean = false,  hide: Boolean = false,
                      func: NotesScreen.() -> Unit = {}) {
        if (wasNavigate) onNavigateTo(MainPage.NOTES)

        NotesScreen.invoke(func, empty, hide)
    }

    fun openBinPage(empty: Boolean = false, func: BinScreen.() -> Unit = {}) {
        wasNavigate = true
        onNavigateTo(MainPage.BIN)

        BinScreen.invoke(func, empty)
    }

    fun openAddDialog(func: AddDialogUi.() -> Unit = {}) {
        addFab.click()
        AddDialogUi.invoke(func)
    }

    fun onNavigateTo(page: MainPage) {
        when (page) {
            MainPage.RANK -> rankMenuItem.click()
            MainPage.NOTES -> notesMenuItem.click()
            MainPage.BIN -> binMenuItem.click()
        }

        assert(page, fabVisible = page == MainPage.NOTES)
    }

    fun onScrollTop(page: MainPage) = waitAfter(SCROLL_TIME) {
        when (page) {
            MainPage.RANK -> rankMenuItem.longClick()
            MainPage.NOTES -> notesMenuItem.longClick()
            MainPage.BIN -> binMenuItem.longClick()
        }
    }


    fun assert(page: MainPage? = null, fabVisible: Boolean? = null) {
        parentContainer.isDisplayed()
        toolbarHolder.isDisplayed()
        menuNavigation.isDisplayed()

        when (page) {
            MainPage.RANK -> rankMenuItem.isSelected()
            MainPage.NOTES -> notesMenuItem.isSelected()
            MainPage.BIN -> binMenuItem.isSelected()
        }

        if (fabVisible != null) addFab.isDisplayed(fabVisible)
    }

    companion object {
        private const val SCROLL_TIME = 500L

        operator fun invoke(func: MainScreen.() -> Unit) =
                MainScreen().apply { assert(MainPage.NOTES, fabVisible = true) }.apply(func)
    }

}