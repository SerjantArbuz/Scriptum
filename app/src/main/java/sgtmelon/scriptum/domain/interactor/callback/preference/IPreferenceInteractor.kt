package sgtmelon.scriptum.domain.interactor.callback.preference

import sgtmelon.scriptum.domain.interactor.impl.preference.PreferenceInteractor
import sgtmelon.scriptum.domain.model.annotation.Theme
import sgtmelon.scriptum.presentation.screen.vm.callback.preference.IPreferenceViewModel

/**
 * Interface for communication [IPreferenceViewModel] with [PreferenceInteractor].
 */
interface IPreferenceInteractor {

    @Theme val theme: Int

    fun getThemeSummary(): String?

    fun updateTheme(@Theme value: Int): String?


    var isDeveloper: Boolean

}