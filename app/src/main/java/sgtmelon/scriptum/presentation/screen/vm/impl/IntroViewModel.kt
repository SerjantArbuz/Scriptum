package sgtmelon.scriptum.presentation.screen.vm.impl

import android.app.Application
import android.os.Bundle
import sgtmelon.scriptum.domain.interactor.callback.IIntroInteractor
import sgtmelon.scriptum.presentation.screen.ui.callback.IIntroActivity
import sgtmelon.scriptum.presentation.screen.vm.callback.IIntroViewModel

/**
 * ViewModel for [IIntroActivity].
 */
class IntroViewModel(application: Application) : ParentViewModel<IIntroActivity>(application),
        IIntroViewModel {

    private lateinit var interactor: IIntroInteractor

    fun setInteractor(interactor: IIntroInteractor) {
        this.interactor = interactor
    }


    override fun onSetup(bundle: Bundle?) {
        callback?.setupViewPager()
        callback?.setupInsets()
    }

    override fun onClickEnd() {
        interactor.onIntroFinish()
        callback?.startMainActivity()
    }

}