package sgtmelon.scriptum.office.st;

import java.util.List;

import androidx.annotation.NonNull;
import sgtmelon.scriptum.app.model.item.RollItem;
import sgtmelon.scriptum.office.utils.HelpUtils;

/**
 * Состояние для отметок, определяющее отмечено ли всё в списке с элементами {@link RollItem}
 */
public final class CheckSt {

    private boolean all;

    public boolean isAll() {
        return all;
    }

    public void setAll(@NonNull List<RollItem> listRoll) {
        all = HelpUtils.Note.isAllCheck(listRoll);
    }

    /**
     * @param check - Количество отметок
     * @param size  - Размер списка
     * @return - Произошло ли изменение состояния
     */
    public boolean setAll(int check, int size) {
        final boolean all = check == size;

        if (this.all != all) {
            this.all = all;
            return true;
        }
        return false;
    }

}
