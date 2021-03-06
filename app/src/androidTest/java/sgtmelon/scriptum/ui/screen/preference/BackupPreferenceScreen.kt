package sgtmelon.scriptum.ui.screen.preference

import sgtmelon.scriptum.R
import sgtmelon.scriptum.presentation.screen.ui.impl.preference.BackupPreferenceFragment
import sgtmelon.scriptum.ui.logic.preference.BackupPreferenceLogic

/**
 * Class for UI control of [BackupPreferenceFragment].
 */
class BackupPreferenceScreen :
    ParentPreferenceScreen<BackupPreferenceLogic>(R.string.pref_title_backup) {

    override val screenLogic = BackupPreferenceLogic()

    companion object {
        operator fun invoke(func: BackupPreferenceScreen.() -> Unit): BackupPreferenceScreen {
            return BackupPreferenceScreen().apply { assert() }.apply(func)
        }
    }
}