package sgtmelon.safedialog

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog

class SingleDialog : DialogBlank() {

    lateinit var rows: Array<String>

    private var init = 0
    var check = 0
        private set

    fun setArguments(check: Int) {
        arguments = Bundle().apply {
            putInt(INIT, check)
            putInt(VALUE, check)
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val bundle = arguments

        init = savedInstanceState?.getInt(INIT)
                ?: bundle?.getInt(INIT)
                ?: 0

        check = savedInstanceState?.getInt(VALUE)
                ?: bundle?.getInt(VALUE)
                ?: 0

        return AlertDialog.Builder(activity)
                .setTitle(title)
                .setSingleChoiceItems(rows, check) { _, i ->
                    check = i
                    setEnable()
                }
                .setPositiveButton(getString(R.string.dialog_btn_accept), onPositiveClick)
                .setNegativeButton(getString(R.string.dialog_btn_cancel)) { dialog, _ -> dialog.cancel() }
                .setCancelable(true)
                .create()
    }

    override fun onSaveInstanceState(outState: Bundle) =
            super.onSaveInstanceState(outState.apply {
                putInt(INIT, init)
                putInt(VALUE, check)
            })

    override fun setEnable() {
        super.setEnable()
        buttonPositive?.isEnabled = init != check
    }

}