package sgtmelon.scriptum.ui.dialog

import androidx.test.espresso.Espresso.pressBack
import sgtmelon.safedialog.MessageDialog
import sgtmelon.scriptum.R
import sgtmelon.scriptum.ui.ParentUi
import sgtmelon.scriptum.ui.basic.BasicMatch
import sgtmelon.scriptum.waitAfter
import sgtmelon.scriptum.waitBefore

/**
 * Класс для ui контроля диалога [MessageDialog] при отчистке корзины
 *
 * @author SerjantArbuz
 */
class ClearDialogUi : ParentUi() {

    fun assert(func: Assert.() -> Unit) = Assert().apply { func() }

    fun onCloseSoft() = waitAfter(time = 300) { pressBack() }

    fun onClickNo() = waitAfter(time = 300) { action { onClickText(R.string.dialog_btn_no) } }

    fun onClickYes() = waitAfter(time = 300) { action { onClickText(R.string.dialog_btn_yes) } }

    companion object {
        operator fun invoke(func: ClearDialogUi.() -> Unit) = ClearDialogUi().apply {
            waitBefore(time = 100) {
                assert { onDisplayContent() }
                func()
            }
        }
    }

    class Assert : BasicMatch() {

        fun onDisplayContent() {
            onDisplayText(R.string.dialog_title_clear_bin)
            onDisplayText(R.string.dialog_text_clear_bin)

            onDisplayText(R.string.dialog_btn_yes)
            onDisplayText(R.string.dialog_btn_no)
        }

    }

}