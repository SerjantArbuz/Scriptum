package sgtmelon.scriptum.office.utils

import android.app.Activity
import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

object AppUtils {

    // TODO раскидать по разным Utils

    fun Activity.beforeFinish(func: () -> Unit) {
        func()
        finish()
    }


    fun EditText?.getClearText(): String {
        if (this == null) return ""

        return text.toString().trim().replace("\\s+".toRegex(), " ")
    }

    fun <T : ViewDataBinding> LayoutInflater.inflateBinding(layoutId: Int, parent: ViewGroup?,
                                                            attachToParent: Boolean = false): T {
        return DataBindingUtil.inflate(this, layoutId, parent, attachToParent)
    }

    fun RecyclerView.ViewHolder.checkNoPosition(func: () -> Unit): Boolean {
        if (adapterPosition == RecyclerView.NO_POSITION) return false

        func()
        return true
    }

    fun Context.getDimen(value: Float) = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, value, resources.displayMetrics
    ).toInt()

    fun Context.showToast(@StringRes stringId: Int, length: Int = Toast.LENGTH_SHORT) =
            Toast.makeText(this, getText(stringId), length).show()

    fun <T> MutableList<T>.swap(from: Int, to: Int) {
        val item = get(from)
        removeAt(from)
        add(to, item)
    }

    fun <E> MutableCollection<E>.clearAndAdd(replace: MutableList<E>) {
        clear()
        addAll(replace)
    }

}

fun View.showKeyboard() {
    (context.getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager)
            ?.showSoftInput(this, 0)
}

fun Activity.hideKeyboard() {
    (getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager)
            ?.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
}

fun View.requestFocusOnVisible(editText: EditText?) {
    setOnTouchListener { _, event ->
        if (event.action != MotionEvent.ACTION_DOWN) return@setOnTouchListener false

        editText?.apply {
            if (visibility == View.VISIBLE && !hasFocus()) {
                requestFocus()
                setSelection(text.toString().length)
                showKeyboard()
            }
        }

        return@setOnTouchListener false
    }
}