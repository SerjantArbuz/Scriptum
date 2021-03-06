package sgtmelon.scriptum.presentation.screen.vm.callback.notification

import android.os.Bundle
import sgtmelon.scriptum.presentation.screen.ui.callback.notification.INotificationActivity
import sgtmelon.scriptum.presentation.screen.vm.callback.IParentViewModel
import sgtmelon.scriptum.presentation.screen.vm.impl.notification.NotificationViewModel

/**
 * Interface for communication [INotificationActivity] with [NotificationViewModel].
 */
interface INotificationViewModel : IParentViewModel {

    fun onSaveData(bundle: Bundle)

    fun onUpdateData()

    fun onClickNote(p: Int)

    fun onClickCancel(p: Int)


    fun onSnackbarAction()

    fun onSnackbarDismiss()

}