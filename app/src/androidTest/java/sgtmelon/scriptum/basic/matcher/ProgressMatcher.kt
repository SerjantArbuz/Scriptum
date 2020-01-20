package sgtmelon.scriptum.basic.matcher

import android.view.View
import android.widget.ProgressBar
import org.hamcrest.Description
import org.hamcrest.TypeSafeMatcher

/**
 * Matcher for check progress and max values.
 */
class ProgressMatcher(
        private val progress: Int,
        private val max: Int
) : TypeSafeMatcher<View>() {

    private var actualProgress: Int = -1
    private var actualMax: Int = -1

    override fun matchesSafely(item: View?): Boolean {
        if (item == null || item !is ProgressBar) return false

        actualProgress = item.progress
        actualMax = item.max

        return progress == actualProgress && max == actualMax
    }

    override fun describeTo(description: Description?) {
        description?.appendText("\nExpected: progress = $progress, max = $max")
        description?.appendText(" | Actual: progress = $actualProgress, max = $actualMax")
    }

}