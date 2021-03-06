package sgtmelon.scriptum.test.ui.auto.screen.preference.help

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.presentation.screen.ui.impl.preference.help.HelpDisappearActivity
import sgtmelon.scriptum.test.parent.ParentUiTest

/**
 * Test for [HelpDisappearActivity].
 */
@RunWith(AndroidJUnit4::class)
class HelpDisappearTest : ParentUiTest(), IHelpDisappearTest {

    @Test fun close() = runTest { onClickClose() }

    @Test fun assert() = runTest { assert() }

    @Test fun openVideo() = runTest { openVideo() }

    // TODO it's not start next test correctly
    @Test fun openSettings() = runTest { openSettings() }
}