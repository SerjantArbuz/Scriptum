package sgtmelon.scriptum.domain.interactor.impl

import sgtmelon.scriptum.data.repository.preference.IPreferenceRepo
import sgtmelon.scriptum.data.room.converter.type.IntConverter
import sgtmelon.scriptum.domain.interactor.callback.IPreferenceInteractor
import sgtmelon.scriptum.domain.model.annotation.Color
import sgtmelon.scriptum.domain.model.annotation.Repeat
import sgtmelon.scriptum.domain.model.annotation.Sort
import sgtmelon.scriptum.domain.model.annotation.Theme
import sgtmelon.scriptum.presentation.provider.SummaryProvider
import sgtmelon.scriptum.presentation.screen.vm.callback.IPreferenceViewModel
import java.util.*

/**
 * Interactor for [IPreferenceViewModel].
 */
class PreferenceInteractor(
        private val summaryProvider: SummaryProvider,
        private val preferenceRepo: IPreferenceRepo
) : IPreferenceInteractor {

    @Theme override val theme: Int get() = preferenceRepo.theme

    override fun getThemeSummary(): String? = summaryProvider.theme.getOrNull(theme)

    override fun updateTheme(@Theme value: Int): String? {
        preferenceRepo.theme = value
        return getThemeSummary()
    }


    @Sort override val sort: Int get() = preferenceRepo.sort

    override fun getSortSummary(): String? = summaryProvider.sort.getOrNull(sort)

    override fun updateSort(@Sort value: Int): String? {
        preferenceRepo.sort = value
        return getSortSummary()
    }


    @Color override val defaultColor: Int get() = preferenceRepo.defaultColor

    override fun getDefaultColorSummary(): String? = summaryProvider.color.getOrNull(defaultColor)

    override fun updateDefaultColor(@Color value: Int): String? {
        preferenceRepo.defaultColor = value
        return getDefaultColorSummary()
    }


    override val savePeriod: Int get() = preferenceRepo.savePeriod

    override fun getSavePeriodSummary(): String? = summaryProvider.savePeriod.getOrNull(savePeriod)


    override fun updateSavePeriod(value: Int): String? {
        preferenceRepo.savePeriod = value
        return getSavePeriodSummary()
    }


    @Repeat override val repeat: Int get() = preferenceRepo.repeat

    override fun getRepeatSummary(): String? = summaryProvider.repeat.getOrNull(repeat)

    override fun updateRepeat(@Repeat value: Int): String? {
        preferenceRepo.repeat = value
        return getRepeatSummary()
    }


    override fun getSignalSummary(valueArray: BooleanArray): String? {
        val summaryArray = summaryProvider.signal

        if (summaryArray.size != valueArray.size) return null

        return StringBuilder().apply {
            var firstAppend = true
            valueArray.forEachIndexed { i, bool ->
                if (bool) {
                    append(if (firstAppend) {
                        firstAppend = false
                        summaryArray[i]
                    } else {
                        (", ").plus(summaryArray[i].toLowerCase(Locale.getDefault()))
                    })
                }
            }
        }.toString()
    }

    override fun updateSignal(valueArray: BooleanArray): String? {
        preferenceRepo.signal = IntConverter().toInt(valueArray)
        return getSignalSummary(valueArray)
    }


    override val volume: Int get() = preferenceRepo.volume

    override fun getVolumeSummary(): String? = summaryProvider.getVolume(volume)

    override fun updateVolume(value: Int): String? {
        preferenceRepo.volume = value
        return getVolumeSummary()
    }

}