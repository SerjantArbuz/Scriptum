package sgtmelon.scriptum.ui.screen

import org.junit.Assert.assertTrue
import sgtmelon.scriptum.R
import sgtmelon.scriptum.basic.extension.*
import sgtmelon.scriptum.data.SimpleInfoPage
import sgtmelon.scriptum.data.State
import sgtmelon.scriptum.domain.model.item.NoteItem
import sgtmelon.scriptum.presentation.screen.ui.impl.notification.NotificationActivity
import sgtmelon.scriptum.ui.IPressBack
import sgtmelon.scriptum.ui.ParentRecyclerScreen
import sgtmelon.scriptum.ui.item.NotificationItemUi
import sgtmelon.scriptum.ui.part.info.SimpleInfoContainer
import sgtmelon.scriptum.ui.part.panel.SnackbarPanel
import sgtmelon.scriptum.ui.part.toolbar.SimpleToolbar
import sgtmelon.scriptum.ui.screen.note.RollNoteScreen
import sgtmelon.scriptum.ui.screen.note.TextNoteScreen

/**
 * Class for UI control of [NotificationActivity].
 */
class NotificationScreen : ParentRecyclerScreen(R.id.notification_recycler), IPressBack {

    //region Views

    private val parentContainer = getViewById(R.id.notification_parent_container)
    private val toolbar = SimpleToolbar(R.string.title_notification, withBack = true)

    private val infoContainer = SimpleInfoContainer(SimpleInfoPage.NOTIFICATION)

    fun getSnackbar(func: SnackbarPanel.() -> Unit = {}): SnackbarPanel {
        val message = R.string.snackbar_message_notification
        val action = R.string.snackbar_action_cancel

        return SnackbarPanel(message, action, func)
    }

    private fun getItem(p: Int) = NotificationItemUi(recyclerView, p)

    //endregion

    fun onClickClose() {
        toolbar.getToolbarButton().click()
    }

    fun openText(
        item: NoteItem.Text,
        p: Int? = random,
        isRankEmpty: Boolean = true,
        func: TextNoteScreen.() -> Unit = {}
    ) {
        if (p == null) return

        getItem(p).view.click()
        TextNoteScreen(func, State.READ, item, isRankEmpty)
    }

    fun openRoll(
        item: NoteItem.Roll,
        p: Int? = random,
        isRankEmpty: Boolean = true,
        func: RollNoteScreen.() -> Unit = {}
    ) {
        if (p == null) return

        getItem(p).view.click()
        RollNoteScreen(func, State.READ, item, isRankEmpty)
    }

    fun onClickCancel(p: Int? = random, isWait: Boolean = false) = apply {
        if (p == null) return@apply

        waitAfter(time = if (isWait) SNACK_BAR_TIME else 0) {
            getItem(p).cancelButton.click()
            getSnackbar { assert() }
        }
    }

    //region Assertion

    fun onAssertItem(p: Int, item: NoteItem) = getItem(p).assert(item)

    fun assert(isEmpty: Boolean) = apply {
        parentContainer.isDisplayed()
        toolbar.assert()

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
        operator fun invoke(
            func: NotificationScreen.() -> Unit,
            isEmpty: Boolean
        ): NotificationScreen {
            return NotificationScreen().assert(isEmpty).apply(func)
        }
    }
}