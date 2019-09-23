package sgtmelon.scriptum.ui

import androidx.annotation.IdRes
import androidx.recyclerview.widget.RecyclerView
import sgtmelon.scriptum.basic.*
import sgtmelon.scriptum.data.Scroll

/**
 * Parent class for screens which contains [RecyclerView]
 */
abstract class ParentRecyclerScreen(@IdRes protected val recyclerId: Int) : ParentUi() {

    protected val recyclerView = getViewById(recyclerId)

    val count: Int get() = recyclerView.getCount()

    protected val random: Int get() = recyclerView.getRandomPosition()

    protected fun onClickItem(position: Int = random) {
        recyclerView.click(position)
    }

    fun onScroll(scroll: Scroll, time: Int = REPEAT_COUNT) = repeat(time) {
        waitAfter(SCROLL_TIME) {
            when (scroll) {
                Scroll.START -> recyclerView.swipeDown()
                Scroll.END -> recyclerView.swipeUp()
            }
        }
    }

    fun onScrollThrough() = repeat(REPEAT_COUNT) {
        onScroll(Scroll.END)
        onScroll(Scroll.START)
    }


    companion object {
        const val SCROLL_TIME = 200L
        const val REPEAT_COUNT = 2
    }

}