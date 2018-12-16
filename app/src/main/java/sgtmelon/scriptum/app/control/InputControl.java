package sgtmelon.scriptum.app.control;

import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import sgtmelon.scriptum.app.model.item.InputItem;
import sgtmelon.scriptum.office.annot.DbAnn;
import sgtmelon.scriptum.office.annot.def.InputDef;
import sgtmelon.scriptum.office.intf.InputIntf;

/**
 * Класс предназначенный для контроля ввода данных в заметку, применения undo и redo
 * Модель для хранения данных: {@link InputItem}
 * <p>
 * {@link InputDef} - Значения, которые будут содержаться в списке:
 * Name change  - Текст (до/после)
 * Rank change  - Отмеченные id (до/после)
 * Color change - Отмеченный цвет (до/после)
 * Text change  - Текст (до/после)
 * Roll change  - Текст (пункт/до/после)
 * Roll add     - Номер пункта : значение
 * Roll swipe   - Номер пункта : значение
 * Roll move    - Перемещение (до/после)
 */
public final class InputControl implements InputIntf {

    private static final String TAG = InputControl.class.getSimpleName();

    private final List<InputItem> listInput = new ArrayList<>();

    private int position = -1;
    private boolean enable; //Переменная для предотвращения записи первичного биндинга

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public void clear() {
        listInput.clear();
        position = -1;
    }

    /**
     * Проверка доступна ли отмена
     *
     * @return - Есть куда возвращаться или нет
     */
    public boolean isUndoAccess() {
        if (listInput.size() != 0) {
            return position != -1;
        } else {
            return false;
        }
    }

    public InputItem undo() {
        if (isUndoAccess()) {
            return listInput.get(position--);
        } else {
            return null;
        }
    }

    /**
     * Проверка доступен ли возврат
     *
     * @return - Есть куда возвращаться или нет
     */
    public boolean isRedoAccess() {
        if (listInput.size() != 0) {
            return position != listInput.size() - 1;
        } else {
            return false;
        }
    }

    public InputItem redo() {
        if (isRedoAccess()) {
            return listInput.get(++position);
        } else {
            return null;
        }
    }

    private void add(InputItem inputItem) {
        if (enable) {
            remove();
            listInput.add(inputItem);
            position++;
        }

        listAll();
    }

    /**
     * Если позиция не в конце, то удаление ненужной информации перед добавлением новой
     */
    private void remove() {
        final int endPosition = listInput.size() - 1;

        if (position != endPosition) {
            for (int i = endPosition; i > position; i--) {
                listInput.remove(i);
            }
        }

        listAll();
    }

    @Override
    public void onRankChange(List<Long> valueFrom, List<Long> valueTo) {
        final InputItem inputItem = new InputItem(InputDef.rank,
                TextUtils.join(DbAnn.Value.DIVIDER, valueFrom),
                TextUtils.join(DbAnn.Value.DIVIDER, valueTo)
        );
        add(inputItem);
    }

    @Override
    public void onColorChange(int valueFrom, int valueTo) {
        final InputItem inputItem = new InputItem(
                InputDef.color, Integer.toString(valueFrom), Integer.toString(valueTo)
        );
        add(inputItem);
    }

    @Override
    public void onNameChange(String valueFrom, String valueTo) {
        final InputItem inputItem = new InputItem(InputDef.name, valueFrom, valueTo);
        add(inputItem);
    }

    @Override
    public void onTextChange(String valueFrom, String valueTo) {
        final InputItem inputItem = new InputItem(InputDef.text, valueFrom, valueTo);
        add(inputItem);
    }

    @Override
    public void onRollChange(int p, String valueFrom, String valueTo) {
        final InputItem inputItem = new InputItem(InputDef.roll, p, valueFrom, valueTo);
        add(inputItem);
    }

    @Override
    public void onRollAdd(int p, String valueTo) {
        final InputItem inputItem = new InputItem(InputDef.rollAdd, p, "", valueTo);
        add(inputItem);
    }

    @Override
    public void onRollRemove(int p, String valueFrom) {
        final InputItem inputItem = new InputItem(InputDef.rollRemove, p, valueFrom, "");
        add(inputItem);
    }

    @Override
    public void onRollMove(int valueFrom, int valueTo) {
        final InputItem inputItem = new InputItem(
                InputDef.rollMove, Integer.toString(valueFrom), Integer.toString(valueTo)
        );
        add(inputItem);
    }

    private void listAll() {
        Log.i(TAG, "listAll:");

        for (int i = 0; i < listInput.size(); i++) {
            final InputItem inputItem = listInput.get(i);
            final String ps = position == i
                    ? " | cursor = " + position
                    : "";

            Log.i(TAG, "i = " + i + " | " + inputItem.toString() + ps);
        }
    }

}