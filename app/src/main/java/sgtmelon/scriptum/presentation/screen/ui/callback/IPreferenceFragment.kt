package sgtmelon.scriptum.presentation.screen.ui.callback

import sgtmelon.scriptum.domain.model.annotation.Color
import sgtmelon.scriptum.domain.model.annotation.Repeat
import sgtmelon.scriptum.domain.model.annotation.Sort
import sgtmelon.scriptum.domain.model.annotation.Theme
import sgtmelon.scriptum.presentation.screen.ui.impl.preference.PreferenceFragment
import sgtmelon.scriptum.presentation.screen.vm.impl.PreferenceViewModel

/**
 * Interface for communication [PreferenceViewModel] with [PreferenceFragment]
 */
interface IPreferenceFragment {

    fun setupApp()

    fun setupNote()

    fun setupNotification(melodyTitleArray: Array<String>)

    fun setupSave()

    fun setupOther()


    fun updateThemeSummary(summary: String?)

    fun showThemeDialog(@Theme value: Int)


    fun updateSortSummary(summary: String?)

    fun showSortDialog(@Sort value: Int)

    fun updateColorSummary(summary: String?)

    fun showColorDialog(@Color color: Int, @Theme theme: Int)


    fun updateRepeatSummary(summary: String?)

    fun showRepeatDialog(@Repeat value: Int)

    fun updateSignalSummary(summary: String?)

    fun showSignalDialog(valueArray: BooleanArray)

    fun showMelodyPermissionDialog()

    fun updateMelodyGroupEnabled(enabled: Boolean)

    fun updateMelodySummary(summary: String)

    fun showMelodyDialog(value: Int)

    fun playMelody(stringUri: String)

    fun updateVolumeSummary(summary: String?)

    fun showVolumeDialog(value: Int)


    fun updateSavePeriodSummary(summary: String?)

    fun showSaveTimeDialog(value: Int)

}