package sgtmelon.scriptum.dagger.module

import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import sgtmelon.scriptum.dagger.ActivityScope
import sgtmelon.scriptum.interactor.callback.IAppInteractor
import sgtmelon.scriptum.screen.ui.AppActivity
import sgtmelon.scriptum.screen.vm.AppViewModel
import sgtmelon.scriptum.screen.vm.callback.IAppViewModel

@Module
class AppModule {

    @Provides
    @ActivityScope
    fun provideViewModel(activity: AppActivity, interactor: IAppInteractor): IAppViewModel {
        return ViewModelProvider(activity).get(AppViewModel::class.java).apply {
            setCallback(activity)
            setInteractor(interactor)
        }
    }

}