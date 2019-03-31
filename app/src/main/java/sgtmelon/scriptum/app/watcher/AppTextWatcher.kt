package sgtmelon.scriptum.app.watcher

import android.text.Editable
import android.text.TextWatcher

/**
 * TextWatcher, чтобы не имплеменьтить все методы
 */
abstract class AppTextWatcher : TextWatcher {

    override fun afterTextChanged(s: Editable?) {}

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

}