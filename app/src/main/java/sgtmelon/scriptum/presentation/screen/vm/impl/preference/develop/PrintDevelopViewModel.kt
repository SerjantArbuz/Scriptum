package sgtmelon.scriptum.presentation.screen.vm.impl.preference.develop

import android.app.Application
import android.os.Bundle
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import sgtmelon.scriptum.domain.interactor.callback.preference.develop.IPrintDevelopInteractor
import sgtmelon.scriptum.domain.model.annotation.test.RunPrivate
import sgtmelon.scriptum.domain.model.data.IntentData.Print.Default
import sgtmelon.scriptum.domain.model.data.IntentData.Print.Intent
import sgtmelon.scriptum.domain.model.item.PrintItem
import sgtmelon.scriptum.domain.model.key.PrintType
import sgtmelon.scriptum.extension.clearAdd
import sgtmelon.scriptum.extension.runBack
import sgtmelon.scriptum.idling.AppIdlingResource
import sgtmelon.scriptum.idling.IdlingTag
import sgtmelon.scriptum.presentation.screen.ui.callback.preference.develop.IPrintDevelopActivity
import sgtmelon.scriptum.presentation.screen.vm.callback.preference.develop.IPrintDevelopViewModel
import sgtmelon.scriptum.presentation.screen.vm.impl.ParentViewModel

/**
 * ViewModel for [IPrintDevelopActivity].
 */
class PrintDevelopViewModel(
    application: Application
) : ParentViewModel<IPrintDevelopActivity>(application),
    IPrintDevelopViewModel {

    private lateinit var interactor: IPrintDevelopInteractor

    fun setInteractor(interactor: IPrintDevelopInteractor) {
        this.interactor = interactor
    }


    @RunPrivate val itemList: MutableList<PrintItem> = ArrayList()

    @RunPrivate var type: PrintType? = null

    override fun onSetup(bundle: Bundle?) {
        val typeOrdinal = bundle?.getInt(Intent.TYPE, Default.TYPE) ?: Default.TYPE
        val type = PrintType.values().getOrNull(typeOrdinal) ?: return
        this.type = type

        callback?.setupView(type)
        callback?.setupInsets()
    }

    override fun onSaveData(bundle: Bundle) = with(bundle) {
        putInt(Intent.TYPE, type?.ordinal ?: Default.TYPE)
    }

    override fun onUpdateData() {
        val type = type ?: return

        AppIdlingResource.getInstance().startWork(IdlingTag.Print.LOAD_DATA)

        callback?.beforeLoad()

        fun updateList() = callback?.apply {
            notifyList(itemList)
            onBindingList()
        }

        /**
         * If was rotation need show list. After that fetch updates.
         */
        if (itemList.isNotEmpty()) {
            updateList()
        } else {
            callback?.showProgress()
        }

        viewModelScope.launch {
            runBack { itemList.clearAdd(interactor.getList(type)) }
            updateList()

            AppIdlingResource.getInstance().stopWork(IdlingTag.Print.LOAD_DATA)
        }
    }
}