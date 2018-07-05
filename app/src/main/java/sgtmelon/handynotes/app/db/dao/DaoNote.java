package sgtmelon.handynotes.app.db.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.TypeConverters;
import android.arch.persistence.room.Update;
import android.content.Context;
import android.text.TextUtils;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import sgtmelon.handynotes.app.model.item.ItemNote;
import sgtmelon.handynotes.app.model.item.ItemRoll;
import sgtmelon.handynotes.app.model.item.ItemStatus;
import sgtmelon.handynotes.app.model.repo.RepoNote;
import sgtmelon.handynotes.office.annot.def.db.DefBin;
import sgtmelon.handynotes.office.conv.ConvBool;
import sgtmelon.handynotes.office.conv.ConvList;

@Dao
@TypeConverters({ConvBool.class})
public abstract class DaoNote extends DaoBase {

    @Insert
    public abstract long insert(ItemNote itemNote);

    @Query("SELECT * FROM NOTE_TABLE " +
            "WHERE NT_ID = :id")
    public abstract ItemNote get(long id);

    public RepoNote get(Context context, long id) {
        ItemNote itemNote = get(id);
        List<ItemRoll> listRoll = getRoll(id);

        Long[] rankVs = ConvList.fromList(getRankVisible());
        ItemStatus itemStatus = new ItemStatus(context, itemNote, rankVs);

        return new RepoNote(itemNote, listRoll, itemStatus);
    }

    public List<RepoNote> get(Context context, @DefBin int bin, String sortKeys) {
        List<RepoNote> listRepoNote = new ArrayList<>();
        List<ItemNote> listNote = getNote(bin, sortKeys);

        List<Long> rkVisible = getRankVisible();
        Long[] rkVs = ConvList.fromList(rkVisible);

        for (int i = 0; i < listNote.size(); i++) {
            ItemNote itemNote = listNote.get(i);

            RepoNote repoNote = new RepoNote();
            repoNote.setItemNote(itemNote);
            repoNote.setListRoll(getRollView(itemNote.getId()));

            ItemStatus itemStatus = new ItemStatus(context, itemNote, rkVs);

            Long[] rkId = itemNote.getRankId();
            if (rkId.length != 0 && !rkVisible.contains(rkId[0])) {
                itemStatus.cancelNote();
            } else {
                repoNote.setItemStatus(itemStatus);
                listRepoNote.add(repoNote);
            }
        }
        return listRepoNote;
    }

    @Query("SELECT * FROM NOTE_TABLE " +
            "WHERE NT_BIN = :bin " +
            "ORDER BY DATE(NT_CREATE) DESC, TIME(NT_CREATE) DESC")
    abstract List<ItemNote> get(@DefBin int bin);

    @Update
    public abstract void update(ItemNote itemNote);

    /**
     * Обновление положения заметки относительно корзины
     *
     * @param id     - Id обновляемой заметки
     * @param change - Время изменения
     * @param bin    - Положение относительно корзины
     */
    @Query("UPDATE NOTE_TABLE " +
            "SET NT_CHANGE = :change, NT_BIN = :bin " +
            "WHERE NT_ID = :id")
    public abstract void update(long id, String change, boolean bin);

    /**
     * Обновление привязки к статус бару
     *
     * @param id     - Id обновляемой заметки
     * @param status - Привязка к статус бару
     */
    @Query("UPDATE NOTE_TABLE " +
            "SET NT_STATUS = :status " +
            "WHERE NT_ID = :id")
    public abstract void update(long id, boolean status);

    @Delete
    abstract void delete(ItemNote itemNote);

    @Delete
    abstract void delete(List<ItemNote> lisNote);

    public void delete(long id) {
        ItemNote itemNote = get(id);

        Long[] rkId = itemNote.getRankId();
        if (rkId.length != 0) {
            clearRank(itemNote.getId(), rkId);
        }

        delete(itemNote);
    }

    public void clearBin() {
        List<ItemNote> listNote = get(DefBin.in);

        for (int i = 0; i < listNote.size(); i++) {
            ItemNote itemNote = listNote.get(i);

            Long[] rkId = itemNote.getRankId();
            if (rkId.length != 0) {
                clearRank(itemNote.getId(), rkId);
            }
        }

        delete(listNote);
    }

    public void listAll(TextView textView) {
        List<ItemNote> listNote = get(DefBin.in);
        listNote.addAll(get(DefBin.out));

        String annotation = "Note Data Base:";
        textView.setText(annotation);

        for (int i = 0; i < listNote.size(); i++) {
            ItemNote itemNote = listNote.get(i);

            textView.append("\n\n" +
                    "ID: " + itemNote.getId() + " | " +
                    "CR: " + itemNote.getCreate() + " | " +
                    "CH: " + itemNote.getChange() + "\n");

            String name = itemNote.getName();
            if (!name.equals("")) textView.append("NM: " + name + "\n");

            String text = itemNote.getText();
            textView.append("TX: " + text.substring(0, Math.min(text.length(), 45)).replace("\n", " "));
            if (text.length() > 40) textView.append("...");
            textView.append("\n");

            textView.append("CL: " + itemNote.getColor() + " | " +
                    "TP: " + itemNote.getType() + " | " +
                    "BN: " + itemNote.isBin() + "\n" +
                    "RK ID: " + TextUtils.join(", ", itemNote.getRankId()) + " | " +
                    "RK PS: " + TextUtils.join(", ", itemNote.getRankPs()) + "\n" +
                    "ST: " + itemNote.isStatus());
        }
    }

}