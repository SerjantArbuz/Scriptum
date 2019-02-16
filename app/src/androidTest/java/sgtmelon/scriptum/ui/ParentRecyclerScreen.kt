package sgtmelon.scriptum.ui

import sgtmelon.scriptum.TestUtils
import sgtmelon.scriptum.basic.BasicValue

abstract class ParentRecyclerScreen(protected val recyclerId: Int) : ParentUi() {

    private val value: BasicValue = BasicValue()

    val count = value.getCount(recyclerId)

    protected val positionRandom: Int
        get() = TestUtils.random(0 until count - 1)

    fun onClickItem(position: Int = positionRandom) =
            action { onClick(recyclerId, position) }

    fun onScroll(scroll: Scroll, time: Int = 1) = action {
        repeat(time) {
            when (scroll) {
                Scroll.START -> onSwipeDown(recyclerId)
                Scroll.END -> onSwipeUp(recyclerId)
            }
        }
    }

}