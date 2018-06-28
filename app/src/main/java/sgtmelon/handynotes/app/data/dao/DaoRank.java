package sgtmelon.handynotes.app.data.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import android.text.TextUtils;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import sgtmelon.handynotes.office.conv.ConvList;
import sgtmelon.handynotes.office.def.data.DefType;
import sgtmelon.handynotes.app.model.item.ItemNote;
import sgtmelon.handynotes.app.model.item.ItemRank;

@Dao
public abstract class DaoRank extends DaoBase {

    @Query("SELECT COUNT(RK_ID) FROM RANK_TABLE")
    public abstract long getCount();

    @Insert
    public abstract long insert(ItemRank itemRank);

    @Query("SELECT * FROM RANK_TABLE " +
            "ORDER BY RK_POSITION")
    abstract List<ItemRank> getSimple();

    /**
     * @return - Список моделей категорий с указанной информацией
     */
    public List<ItemRank> get() {
        List<ItemRank> listRank = getSimple();

        // FIXME: 28.06.2018
//        for (int i = 0; i < listRank.size(); i++) {
//            ItemRank itemRank = listRank.get(i);
//            String[] rankCreate = itemRank.getCreate();
//
//            itemRank.setTextCount(getNoteCount(DefType.text, rankCreate));
//            itemRank.setRollCount(getNoteCount(DefType.roll, rankCreate));
//            itemRank.setRollCheck(getRollCheck(rankCreate), getRollCount(rankCreate));
//
//            listRank.set(i, itemRank);
//        }

        return listRank;
    }

    /**
     * @param rankName - Уникальное имя категории
     * @return - Модель категории
     */
    @Query("SELECT * FROM RANK_TABLE " +
            "WHERE RK_NAME = :rankName")
    abstract ItemRank get(String rankName);

    /**
     * @return - Лист с именами в высоком регистре
     */
    @Query("SELECT UPPER(RK_NAME) FROM RANK_TABLE " +
            "ORDER BY RK_POSITION")
    public abstract List<String> getNameUp();

    @Query("SELECT RK_NAME FROM RANK_TABLE " +
            "ORDER BY RK_POSITION")
    public abstract String[] getName();

    @Query("SELECT RK_ID FROM RANK_TABLE " +
            "ORDER BY RK_POSITION")
    public abstract Integer[] getId();

    public boolean[] getCheck(String[] rankId) {
        List<ItemRank> listRank = getSimple();

        boolean[] rankCheck = new boolean[listRank.size()];
        for (int i = 0; i < listRank.size(); i++) {
            ItemRank itemRank = listRank.get(i);

            String id = Long.toString(itemRank.getId());
            rankCheck[i] = Arrays.asList(rankId).contains(id);
        }

        return rankCheck;
    }

    public void update(String noteCreate, String[] noteRankId) {
        List<ItemRank> listRank = get();

        boolean[] rankCheck = getCheck(noteRankId);

        for (int i = 0; i < listRank.size(); i++) {
            ItemRank itemRank = listRank.get(i);
            List<String> rankCreate = ConvList.toList(itemRank.getCreate());

            if (rankCheck[i]) {
                if (!rankCreate.contains(noteCreate)) rankCreate.add(noteCreate);
            } else {
                if (rankCreate.contains(noteCreate)) rankCreate.remove(noteCreate);
            }

            itemRank.setCreate(ConvList.fromList(rankCreate));
            listRank.set(i, itemRank);
        }

        updateRank(listRank);
    }

    @Update
    public abstract void update(ItemRank itemRank);

    /**
     * @param startPosition - Начальная позиция обновления
     * @param endPosition   - Конечная позиция обновления
     */
    public void update(int startPosition, int endPosition) {
        boolean startFirst = startPosition < endPosition;

        int iStart = startFirst ? startPosition : endPosition;
        int iEnd = startFirst ? endPosition : startPosition;
        int iAdd = startFirst ? -1 : 1;

        List<ItemRank> listRank = getSimple();

        int newPosition;
        List<String> ntCreate = new ArrayList<>();

        for (int i = iStart; i <= iEnd; i++) {
            ItemRank itemRank = listRank.get(i);

            for (String rankCreate : itemRank.getCreate()) {
                if (!ntCreate.contains(rankCreate)) {
                    ntCreate.add(rankCreate);
                }
            }

            newPosition = i == startPosition ? endPosition : i + iAdd;

            itemRank.setPosition(newPosition);
            listRank.set(i, itemRank);
        }

        updateRank(listRank);

        update(ntCreate, listRank);
    }

    /**
     * @param startPosition - Позиция удаления категории
     */
    public void update(int startPosition) {
        List<ItemRank> listRank = getSimple();

        List<String> ntCreate = new ArrayList<>();

        for (int i = startPosition; i < listRank.size(); i++) {
            ItemRank itemRank = listRank.get(i);

            for (String rankCreate : itemRank.getCreate()) {
                if (!ntCreate.contains(rankCreate)) {
                    ntCreate.add(rankCreate);
                }
            }

            itemRank.setPosition(i);
            listRank.set(i, itemRank);
        }

        updateRank(listRank);

        update(ntCreate, listRank);
    }

    /**
     * @param ntCreate - Даты создания заметок, которые нужно обновить
     * @param listRank - Новый список категорий, с новыми позициями
     */
    private void update(List<String> ntCreate, List<ItemRank> listRank) {
        List<ItemNote> listNote = getNote(ntCreate);

        for (int i = 0; i < listNote.size(); i++) {
            ItemNote itemNote = listNote.get(i);

            String[] rankIdOld = itemNote.getRankId();
            String[] rankIdNew = new String[rankIdOld.length];
            String[] rankPsNew = new String[rankIdOld.length];

            int p = 0;
            for (int j = 0; j < listRank.size(); j++) {
                ItemRank itemRank = listRank.get(j);

                String rankId = Long.toString(itemRank.getId());
                String rankPs = Integer.toString(itemRank.getPosition());

                if (Arrays.asList(rankIdOld).contains(rankId)) {
                    rankIdNew[p] = rankId;
                    rankPsNew[p] = rankPs;
                    p++;
                }
            }

            itemNote.setRankId(rankIdNew);
            itemNote.setRankPs(rankPsNew);
            listNote.set(i, itemNote);
        }

        updateNote(listNote);
    }

    @Delete
    abstract void delete(ItemRank itemRank);

    public void delete(String rankName) {
        ItemRank itemRank = get(rankName);

        String[] rankCreate = itemRank.getCreate();
        if (rankCreate.length != 0) updateNote(rankCreate, Long.toString(itemRank.getId()));

        delete(itemRank);
    }

    public void listAll(TextView textView) {
        List<ItemRank> listRank = get();

        String annotation = "Rank Data Base: ";
        textView.setText(annotation);

        for (int i = 0; i < listRank.size(); i++) {
            ItemRank itemRank = listRank.get(i);

            textView.append("\n\n" +
                    "ID: " + itemRank.getId() + " | " +
                    "PS: " + itemRank.getPosition() + "\n" +
                    "NM: " + itemRank.getName() + "\n" +
                    "CR: " + TextUtils.join(", ", itemRank.getCreate()) + "\n" +
                    "VS: " + itemRank.isVisible());
        }
    }
}
