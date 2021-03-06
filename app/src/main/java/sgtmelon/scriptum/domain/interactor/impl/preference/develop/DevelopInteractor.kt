package sgtmelon.scriptum.domain.interactor.impl.preference.develop

import sgtmelon.scriptum.data.repository.preference.IPreferenceRepo
import sgtmelon.scriptum.data.repository.room.callback.IDevelopRepo
import sgtmelon.scriptum.domain.interactor.callback.preference.develop.IDevelopInteractor
import sgtmelon.scriptum.domain.interactor.impl.ParentInteractor
import sgtmelon.scriptum.presentation.screen.vm.callback.preference.develop.IDevelopViewModel

/**
 * Interactor for [IDevelopViewModel].
 */
class DevelopInteractor(
    private val developRepo: IDevelopRepo,
    private val preferenceRepo: IPreferenceRepo
) : ParentInteractor(),
    IDevelopInteractor {

    override suspend fun getRandomNoteId(): Long = developRepo.getRandomNoteId()

    override fun resetPreferences() = preferenceRepo.clear()
}