package sgtmelon.scriptum.presentation.screen.vm.callback.preference

import sgtmelon.scriptum.domain.model.annotation.Color
import sgtmelon.scriptum.domain.model.annotation.Repeat
import sgtmelon.scriptum.domain.model.annotation.Sort
import sgtmelon.scriptum.domain.model.annotation.Theme
import sgtmelon.scriptum.domain.model.key.PermissionResult
import sgtmelon.scriptum.presentation.screen.ui.callback.preference.IPreferenceFragment
import sgtmelon.scriptum.presentation.screen.vm.callback.IParentViewModel
import sgtmelon.scriptum.presentation.screen.vm.impl.preference.PreferenceViewModel

/**
 * Interface for communication [IPreferenceFragment] with [PreferenceViewModel].
 */
interface IPreferenceViewModel : IParentViewModel {

    fun onPause()


    fun onClickTheme(): Boolean

    fun onResultTheme(@Theme value: Int)


    fun onClickExport(result: PermissionResult?): Boolean

    fun onClickImport(result: PermissionResult?): Boolean

    fun onResultImport(name: String)


    fun onClickSort(): Boolean

    fun onResultNoteSort(@Sort value: Int)

    fun onClickNoteColor(): Boolean

    fun onResultNoteColor(@Color value: Int)

    fun onClickSaveTime(): Boolean

    fun onResultSaveTime(value: Int)


    fun onClickRepeat(): Boolean

    fun onResultRepeat(@Repeat value: Int)

    fun onClickSignal(): Boolean

    fun onResultSignal(valueArray: BooleanArray)

    fun onClickMelody(result: PermissionResult?): Boolean

    fun onSelectMelody(value: Int)

    fun onResultMelody(title: String)

    fun onClickVolume(): Boolean

    fun onResultVolume(value: Int)


    fun onUnlockDeveloper()

}