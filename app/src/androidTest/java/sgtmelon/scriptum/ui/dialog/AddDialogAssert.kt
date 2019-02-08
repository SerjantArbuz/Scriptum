package sgtmelon.scriptum.ui.dialog

import sgtmelon.scriptum.R
import sgtmelon.scriptum.basic.BasicMatch

class AddDialogAssert : BasicMatch() {

    fun onDisplayContent() {
        onDisplay(R.id.add_navigation)
        onDisplayText(R.string.dialog_add_text)
        onDisplayText(R.string.dialog_add_roll)
    }

}
