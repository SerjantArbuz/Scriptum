package sgtmelon.scriptum.ui.screen.preference.help

import sgtmelon.scriptum.R
import sgtmelon.scriptum.presentation.screen.ui.impl.preference.help.HelpPreferenceFragment
import sgtmelon.scriptum.ui.logic.preference.HelpPreferenceLogic
import sgtmelon.scriptum.ui.screen.preference.ParentPreferenceScreen

/**
 * Class for UI control of [HelpPreferenceFragment].
 */
class HelpPreferenceScreen :
    ParentPreferenceScreen<HelpPreferenceLogic>(R.string.pref_title_other_help) {

    override val screenLogic = HelpPreferenceLogic()

    fun openNotificationDisappear(func: HelpDisappearScreen.() -> Unit = {}) {
        getItem(p = 0).Summary().onItemClick()
        HelpDisappearScreen(func)
    }

    fun openPrivacyPolicy() {
        getItem(p = 2).Simple().onItemClick()
    }

    companion object {
        operator fun invoke(func: HelpPreferenceScreen.() -> Unit): HelpPreferenceScreen {
            return HelpPreferenceScreen().apply { assert() }.apply(func)
        }
    }
}