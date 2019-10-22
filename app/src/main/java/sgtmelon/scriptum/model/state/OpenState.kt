package sgtmelon.scriptum.model.state

import android.os.Bundle

/**
 * State for dialogs which give us know open them or not
 */
class OpenState {

    var changeEnabled: Boolean = true
    var value: Boolean = false

    /**
     * Use for open dialog chain (for block actions during dialogs close/open)
     */
    var tag: String = TAG_ND

    /**
     * Use when need skip next [clear], e.g. on dialog dismiss
     */
    var skipClear = false

    fun tryInvoke(func: () -> Unit) {
        if (!changeEnabled) return

        if (!value) {
            value = true
            func()
        }
    }

    fun tryInvoke(tag: String, func: () -> Unit) {
        if (!changeEnabled) return

        if (this.tag == tag) {
            func()
        }
    }

    fun clear() {
        if (skipClear) {
            skipClear = false
            return
        }

        if (!changeEnabled) return

        value = false
        tag = TAG_ND
    }

    fun get(bundle: Bundle?) {
        bundle?.let {
            changeEnabled = it.getBoolean(KEY_CHANGE)
            value = it.getBoolean(KEY_VALUE)
            tag = it.getString(KEY_TAG) ?: TAG_ND
        }
    }

    fun save(bundle: Bundle) = bundle.let {
        it.putBoolean(KEY_CHANGE, changeEnabled)
        it.putBoolean(KEY_VALUE, value)
        it.putString(KEY_TAG, tag)
    }

    companion object {
        private const val KEY_PREFIX = "OPEN_STATE"

        private const val KEY_CHANGE = "${KEY_PREFIX}_CHANGE"
        private const val KEY_VALUE = "${KEY_PREFIX}_VALUE"
        private const val KEY_TAG = "${KEY_PREFIX}_TAG"


        private const val TAG_PREFIX = "TAG"

        const val TAG_ND = "${TAG_PREFIX}_ND"
        const val TAG_OPTIONS = "${TAG_PREFIX}_OPTIONS"
        const val TAG_DATE_TIME = "${TAG_PREFIX}_DATE_TIME"
    }

}