package sgtmelon.scriptum.element.common;

import android.app.Dialog;
import android.os.Bundle;

import java.util.Arrays;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import sgtmelon.scriptum.R;
import sgtmelon.scriptum.office.annot.def.DialogDef;
import sgtmelon.scriptum.office.blank.DialogBlank;

public final class MultiplyDialog extends DialogBlank {

    private String[] name;
    private boolean[] init, check;

    public void setArguments(boolean[] check) {
        Bundle arg = new Bundle();

        arg.putBooleanArray(DialogDef.INIT, check.clone());
        arg.putBooleanArray(DialogDef.VALUE, check);

        setArguments(arg);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle arg = getArguments();

        if (savedInstanceState != null) {
            init = savedInstanceState.getBooleanArray(DialogDef.INIT);
            check = savedInstanceState.getBooleanArray(DialogDef.VALUE);
        } else if (arg != null) {
            init = arg.getBooleanArray(DialogDef.INIT);
            check = arg.getBooleanArray(DialogDef.VALUE);
        }

        return new AlertDialog.Builder(context)
                .setTitle(title)
                .setMultiChoiceItems(name, check, (dialog, which, isChecked) -> {
                    check[which] = isChecked;
                    setEnable();
                })
                .setPositiveButton(getString(R.string.dialog_btn_accept), onPositiveClick)
                .setNegativeButton(getString(R.string.dialog_btn_cancel), (dialog, id) -> dialog.cancel())
                .setCancelable(true)
                .create();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putBooleanArray(DialogDef.INIT, init);
        outState.putBooleanArray(DialogDef.VALUE, check);
    }

    public void setRows(String[] name) {
        this.name = name;
    }

    public boolean[] getCheck() {
        return check;
    }

    @Override
    protected void setEnable() {
        super.setEnable();

        if (Arrays.equals(init, check)) buttonPositive.setEnabled(false);
        else buttonPositive.setEnabled(true);
    }

}
