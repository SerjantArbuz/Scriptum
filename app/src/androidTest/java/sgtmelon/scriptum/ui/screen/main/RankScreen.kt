package sgtmelon.scriptum.ui.screen.main

import org.junit.Assert.assertTrue
import sgtmelon.scriptum.R
import sgtmelon.scriptum.basic.extension.*
import sgtmelon.scriptum.data.SimpleInfoPage
import sgtmelon.scriptum.domain.model.item.RankItem
import sgtmelon.scriptum.presentation.screen.ui.impl.main.RankFragment
import sgtmelon.scriptum.ui.ParentRecyclerScreen
import sgtmelon.scriptum.ui.dialog.RenameDialogUi
import sgtmelon.scriptum.ui.item.RankItemUi
import sgtmelon.scriptum.ui.part.info.SimpleInfoContainer
import sgtmelon.scriptum.ui.part.panel.SnackbarPanel
import sgtmelon.scriptum.ui.part.toolbar.RankToolbar

/**
 * Class for UI control of [RankFragment].
 */
class RankScreen : ParentRecyclerScreen(R.id.rank_recycler) {

    //region Views

    private val parentContainer = getViewById(R.id.rank_parent_container)
    private val infoContainer = SimpleInfoContainer(SimpleInfoPage.RANK)

    fun getSnackbar(func: SnackbarPanel.() -> Unit = {}): SnackbarPanel {
        val message = R.string.snackbar_message_rank
        val action = R.string.snackbar_action_cancel

        return SnackbarPanel(message, action, func)
    }

    private fun getItem(p: Int) = RankItemUi(recyclerView, p)

    fun toolbar(func: RankToolbar.() -> Unit) = RankToolbar(func)

    //endregion

    fun openRenameDialog(
        title: String,
        p: Int? = random,
        func: RenameDialogUi.() -> Unit = {}
    ) = apply {
        if (p == null) return@apply

        getItem(p).view.click()
        RenameDialogUi(func, title)
    }

    fun onClickVisible(p: Int? = random) = apply {
        if (p == null) return@apply

        getItem(p).visibleButton.click()
    }

    fun onLongClickVisible(p: Int? = random) = apply {
        if (p == null) return@apply

        getItem(p).visibleButton.longClick()
    }

    fun onClickCancel(p: Int? = random, isWait: Boolean = false) = apply {
        if (p == null) return@apply

        waitAfter(time = if (isWait) SNACK_BAR_TIME else 0) {
            getItem(p).cancelButton.click()
            getSnackbar { assert() }
        }
    }

    //region Assertion

    fun onAssertItem(item: RankItem, p: Int? = random) {
        if (p == null) return

        getItem(p).assert(item)
    }

    fun assert(isEmpty: Boolean) = apply {
        toolbar { assert() }

        parentContainer.isDisplayed()

        infoContainer.assert(isEmpty)
        recyclerView.isDisplayed(!isEmpty)
    }

    fun assertSnackbarDismiss() {
        assertTrue(try {
            getSnackbar().assert()
            false
        } catch (e: Throwable) {
            true
        })
    }

    //endregion

    companion object {
        operator fun invoke(func: RankScreen.() -> Unit, isEmpty: Boolean): RankScreen {
            return RankScreen().assert(isEmpty).apply(func)
        }
    }
}