package sgtmelon.scriptum.presentation.screen.ui.callback.preference

import androidx.annotation.StringRes
import sgtmelon.scriptum.domain.model.annotation.Theme
import sgtmelon.scriptum.presentation.screen.ui.impl.preference.PreferenceFragment
import sgtmelon.scriptum.presentation.screen.vm.callback.preference.IPreferenceViewModel

/**
 * Interface for communication [IPreferenceViewModel] with [PreferenceFragment].
 */
interface IPreferenceFragment {

    fun showToast(@StringRes stringId: Int)

    fun setupApp()

    fun setupOther()

    fun setupDeveloper()

    fun updateThemeSummary(summary: String?)

    fun showThemeDialog(@Theme value: Int)

}