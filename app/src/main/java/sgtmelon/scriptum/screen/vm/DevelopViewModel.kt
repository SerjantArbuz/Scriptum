package sgtmelon.scriptum.screen.vm

import android.app.Application
import android.os.Bundle
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import sgtmelon.scriptum.repository.room.DevelopRepo
import sgtmelon.scriptum.repository.room.callback.IDevelopRepo
import sgtmelon.scriptum.screen.ui.DevelopActivity
import sgtmelon.scriptum.screen.ui.callback.IDevelopActivity
import sgtmelon.scriptum.screen.vm.callback.IDevelopViewModel

/**
 * ViewModel for [DevelopActivity]
 */
class DevelopViewModel(application: Application) : ParentViewModel<IDevelopActivity>(application),
        IDevelopViewModel {

    private val iDevelopRepo: IDevelopRepo = DevelopRepo(context)

    override fun onSetup(bundle: Bundle?) {
        viewModelScope.launch {
            callback?.apply {
                fillAboutNoteTable(iDevelopRepo.getNoteTablePrint())
                fillAboutRollTable(iDevelopRepo.getRollTablePrint())
                fillAboutRankTable(iDevelopRepo.getRankTablePrint())
                fillAboutPreference(iDevelopRepo.getPreferencePrint())
            }
        }
    }

    override fun onIntroClick() {
        callback?.startIntroActivity()
    }

}