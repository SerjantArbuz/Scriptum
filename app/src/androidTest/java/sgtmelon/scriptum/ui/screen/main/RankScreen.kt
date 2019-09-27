package sgtmelon.scriptum.ui.screen.main

import android.view.View
import androidx.test.espresso.matcher.ViewMatchers.hasDescendant
import org.hamcrest.Matcher
import sgtmelon.scriptum.R
import sgtmelon.scriptum.basic.extension.*
import sgtmelon.scriptum.data.InfoPage
import sgtmelon.scriptum.room.entity.RankEntity
import sgtmelon.scriptum.screen.ui.main.RankFragment
import sgtmelon.scriptum.ui.ParentRecyclerItem
import sgtmelon.scriptum.ui.ParentRecyclerScreen
import sgtmelon.scriptum.ui.dialog.RenameDialogUi
import sgtmelon.scriptum.ui.part.InfoContainer
import sgtmelon.scriptum.ui.part.toolbar.RankToolbar

/**
 * Class for UI control of [RankFragment]
 */
class RankScreen : ParentRecyclerScreen(R.id.rank_recycler) {

    //region Views

    private val parentContainer = getViewById(R.id.rank_parent_container)
    private val infoContainer = InfoContainer(InfoPage.RANK)

    private fun getItem(rankEntity: RankEntity) = Item(recyclerView, rankEntity)

    fun toolbar(func: RankToolbar.() -> Unit) = RankToolbar.invoke(func)

    //endregion

    fun openRenameDialog(title: String, p: Int = random, func: RenameDialogUi.() -> Unit = {}) {
        onClickItem(p)
        RenameDialogUi.invoke(func, title)
    }

    fun onClickVisible(rankEntity: RankEntity) = waitAfter(ANIM_TIME) {
        getItem(rankEntity).visibleButton.click()
    }

    fun onLongClickVisible(rankEntity: RankEntity) = waitAfter(ANIM_TIME) {
        getItem(rankEntity).visibleButton.longClick()
    }

    fun onClickCancel(rankEntity: RankEntity) {
        getItem(rankEntity).cancelButton.click()
    }


    fun onAssertItem(rankEntity: RankEntity) {
        getItem(rankEntity).assert()
    }

    fun assert(empty: Boolean) {
        toolbar { assert() }

        parentContainer.isDisplayed()

        infoContainer.assert(empty)
        recyclerView.isDisplayed(!empty)
    }

    private inner class Item(list: Matcher<View>, private val rankEntity: RankEntity) :
            ParentRecyclerItem(list, hasDescendant(getView(R.id.rank_name_text, rankEntity.name))) {

        val visibleButton by lazy { getChild(getViewById(R.id.rank_visible_button)) }
        val cancelButton by lazy { getChild(getViewById(R.id.rank_cancel_button)) }

        private val nameText by lazy { getChild(getViewById(R.id.rank_name_text)) }
        private val countText by lazy { getChild(getViewById(R.id.rank_text_count_text)) }

        fun assert() {
            val isVisible = rankEntity.isVisible
            visibleButton.isDisplayed().withDrawable(
                    if (isVisible) R.drawable.ic_visible_exit else R.drawable.ic_visible_enter,
                    if (isVisible) R.attr.clAccent else R.attr.clContent
            )

            cancelButton.isDisplayed().withDrawable(R.drawable.ic_cancel_enter, R.attr.clContent)

            nameText.isDisplayed().haveText(rankEntity.name)
            countText.isDisplayed().haveText(string = "${context.getString(R.string.list_item_rank_count)} ${rankEntity.noteId.size}")
        }

    }

    companion object {
        private const val ANIM_TIME = 300L

        operator fun invoke(func: RankScreen.() -> Unit, empty: Boolean) =
                RankScreen().apply { assert(empty) }.apply(func)
    }

}