package sgtmelon.handynotes.app.viewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import sgtmelon.handynotes.app.dataBase.DbRoom;
import sgtmelon.handynotes.app.model.item.ItemNote;
import sgtmelon.handynotes.app.model.item.ItemRoll;
import sgtmelon.handynotes.app.model.item.ItemStatus;
import sgtmelon.handynotes.app.model.repo.RepoNote;
import sgtmelon.handynotes.office.annot.Db;
import sgtmelon.handynotes.office.annot.def.DefPages;
import sgtmelon.handynotes.office.conv.ConvList;
import sgtmelon.handynotes.office.st.StNote;

public class VmActNote extends AndroidViewModel {

    private static final String TAG = "VmActNote";

    public VmActNote(@NonNull Application application) {
        super(application);
    }

    private boolean create;
    private int type;
    private long id;

    public boolean isCreate() {
        return create;
    }

    public int getType() {
        return type;
    }

    public long getId() {
        return id;
    }

    public void setValue(Bundle bundle) {
        create = bundle.getBoolean(DefPages.CREATE);
        type = bundle.getInt(Db.NT_TP);
        id = bundle.getLong(Db.NT_ID);

        Log.i(TAG, "setValue: create - " + create + ", type - " + type + ", id - " + id);

        if (repoNote == null) loadData();
    }

    private StNote stNote;
    private List<Long> rankVisible;
    private RepoNote repoNote;

    public StNote getStNote() {
        return stNote;
    }

    public void setStNote(StNote stNote) {
        this.stNote = stNote;
    }

    public RepoNote getRepoNote() {
        return repoNote;
    }

    public void setRepoNote(RepoNote repoNote) {
        this.repoNote = repoNote;
        repoNote.updateItemStatus(rankVisible);
    }

    public void setRepoNote(boolean status) {
        repoNote.updateItemStatus(status);
    }

    private DbRoom db;

    private void loadData() {
        Log.d(TAG, "loadData");

        stNote = new StNote();
        stNote.setCreate(create);
        stNote.setEdit();

        Context context = getApplication().getApplicationContext();

        db = DbRoom.provideDb(context);
        rankVisible = db.daoRank().getRankVisible();
        if (stNote.isCreate()) {
            ItemNote itemNote = new ItemNote(context, type);
            ItemStatus itemStatus = new ItemStatus(context, itemNote, ConvList.fromList(rankVisible));

            repoNote = new RepoNote(itemNote, new ArrayList<ItemRoll>(), itemStatus);
        } else {
            repoNote = db.daoNote().get(context, id);
            stNote.setBin(repoNote.getItemNote().isBin());
        }
        db.close();
    }

    public RepoNote loadData(long id) {
        Log.d(TAG, "loadData");

        Context context = getApplication().getApplicationContext();

        db = DbRoom.provideDb(context);
        repoNote = db.daoNote().get(context, id);
        db.close();

        return repoNote;
    }

}