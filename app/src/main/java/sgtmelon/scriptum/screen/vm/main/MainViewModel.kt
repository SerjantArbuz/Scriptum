package sgtmelon.scriptum.screen.vm.main

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.lifecycle.ViewModel
import sgtmelon.scriptum.R
import sgtmelon.scriptum.model.key.MainPage
import sgtmelon.scriptum.screen.callback.main.MainCallback
import sgtmelon.scriptum.screen.view.main.MainActivity

/**
 * ViewModel для [MainActivity]
 *
 * @author SerjantArbuz
 */
class MainViewModel : ViewModel() {

    lateinit var callback: MainCallback

    private var firstStart: Boolean = true
    private var page: Int = MainPage.NOTES.ordinal

    fun setupData(bundle: Bundle?) {
        if (bundle != null) page = bundle.getInt(PAGE_CURRENT)

        callback.setupNavigation(pageItemId[page])
    }

    fun saveData(bundle: Bundle) = bundle.putInt(PAGE_CURRENT, page)

    fun onSelectItem(@IdRes itemId: Int): Boolean {
        val pageFrom = MainPage.values()[page]
        val pageTo = itemId.getPageById()

        page = pageTo.ordinal

        if (!firstStart && pageTo == pageFrom) {
            callback.scrollTop(pageTo)
        } else {
            if (firstStart) firstStart = false

            callback.changeFabState(state = pageTo == MainPage.NOTES)
            callback.showPage(pageFrom, pageTo)
        }

        return true
    }

    companion object {
        private const val PAGE_CURRENT = "INSTANCE_MAIN_PAGE_CURRENT"

        private val pageItemId =
                intArrayOf(R.id.item_page_rank, R.id.item_page_notes, R.id.item_page_bin)

        private fun Int.getPageById(): MainPage = when (this) {
            R.id.item_page_rank -> MainPage.RANK
            R.id.item_page_notes -> MainPage.NOTES
            R.id.item_page_bin -> MainPage.BIN
            else -> throw NoSuchFieldException("Id doesn't match any of page")
        }

    }


}