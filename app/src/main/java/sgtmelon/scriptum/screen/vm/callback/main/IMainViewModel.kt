package sgtmelon.scriptum.screen.vm.callback.main

import android.os.Bundle
import android.view.MenuItem
import androidx.annotation.IdRes
import sgtmelon.scriptum.receiver.MainReceiver
import sgtmelon.scriptum.screen.ui.main.MainActivity
import sgtmelon.scriptum.screen.vm.callback.IParentViewModel
import sgtmelon.scriptum.screen.vm.main.MainViewModel

/**
 * Interface for communication [MainActivity] with [MainViewModel]
 */
interface IMainViewModel : IParentViewModel, MainReceiver.Callback {

    fun onSaveData(bundle: Bundle)

    fun onSelectItem(@IdRes itemId: Int)

    fun onResultAddDialog(menuItem: MenuItem)

}