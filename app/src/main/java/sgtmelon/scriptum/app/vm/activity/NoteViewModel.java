package sgtmelon.scriptum.app.vm.activity;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import sgtmelon.scriptum.app.database.RoomDb;
import sgtmelon.scriptum.app.model.NoteRepo;
import sgtmelon.scriptum.app.model.item.NoteItem;
import sgtmelon.scriptum.app.model.item.StatusItem;
import sgtmelon.scriptum.app.vm.fragment.note.ParentNoteViewModel;
import sgtmelon.scriptum.office.annot.def.ColorDef;
import sgtmelon.scriptum.office.annot.def.IntentDef;
import sgtmelon.scriptum.office.annot.key.NoteType;
import sgtmelon.scriptum.office.state.NoteState;
import sgtmelon.scriptum.office.utils.PrefUtils;
import sgtmelon.scriptum.office.utils.TimeUtils;

public final class NoteViewModel extends AndroidViewModel {

    private final Context context;

    private boolean ntCreate;
    private NoteType noteType;
    private long ntId;

    private NoteState noteState;

    private boolean rankEmpty;

    private List<Long> rankVisible;
    private NoteRepo noteRepo;

    private RoomDb db;

    public NoteViewModel(@NonNull Application application) {
        super(application);
        context = application.getApplicationContext();
    }

    public void setValue(Bundle bundle) {
        ntCreate = bundle.getBoolean(IntentDef.NOTE_CREATE);
        noteType = NoteType.values()[bundle.getInt(IntentDef.NOTE_TYPE)];
        ntId = bundle.getLong(IntentDef.NOTE_ID);

        if (noteRepo == null) {
            loadData();
        }
    }

    public NoteType getNoteType() {
        return noteType;
    }

    public long getNtId() {
        return ntId;
    }

    public NoteState getNoteState() {
        return noteState;
    }

    public boolean isRankEmpty() {
        return rankEmpty;
    }

    public NoteRepo getNoteRepo() {
        return noteRepo;
    }

    public void setNoteRepo(NoteRepo noteRepo) {
        final NoteItem noteItem = noteRepo.getNoteItem();
        noteRepo.getStatusItem().updateNote(noteItem, rankVisible);

        this.noteRepo = noteRepo;
    }

    private void loadData() {
        db = RoomDb.provideDb(context);

        rankEmpty = db.daoRank().getCount() == 0;
        rankVisible = db.daoRank().getRankVisible();

        if (ntCreate) {
            final String create = TimeUtils.INSTANCE.getTime(context);
            final int color = new PrefUtils(context).getDefaultColor();

            final NoteItem noteItem = new NoteItem(create, color, noteType);
            final StatusItem statusItem = new StatusItem(context, noteItem, false);

            noteRepo = new NoteRepo(noteItem, new ArrayList<>(), statusItem);

            noteState = new NoteState(true);
            noteState.setBin(false);
        } else {
            noteRepo = db.daoNote().get(context, ntId);

            noteState = new NoteState(false);
            noteState.setBin(noteRepo.getNoteItem().isBin());
        }

        db.close();
    }

    @ColorDef
    public int resetFragmentData(long id, @NonNull ParentNoteViewModel viewModel) {
        db = RoomDb.provideDb(context);
        noteRepo = db.daoNote().get(context, id);
        db.close();

        viewModel.setNoteRepo(noteRepo);

        return noteRepo.getNoteItem().getColor();
    }

    public void onMenuRestore() {
        db = RoomDb.provideDb(context);
        db.daoNote().update(
                noteRepo.getNoteItem().getId(), TimeUtils.INSTANCE.getTime(context), false
        );
        db.close();
    }

    public void onMenuRestoreOpen() {
        noteState.setBin(false);

        final NoteItem noteItem = noteRepo.getNoteItem();
        noteItem.setChange(TimeUtils.INSTANCE.getTime(context));
        noteItem.setBin(false);

        db = RoomDb.provideDb(context);
        db.daoNote().update(noteItem);
        db.close();
    }

    public void onMenuClear() {
        db = RoomDb.provideDb(context);
        db.daoNote().delete(noteRepo.getNoteItem().getId());
        db.close();

        noteRepo.update(false);
    }

    public void onMenuDelete() {
        final NoteItem noteItem = noteRepo.getNoteItem();

        db = RoomDb.provideDb(context);
        db.daoNote().update(noteItem.getId(), TimeUtils.INSTANCE.getTime(context), true);

        if (noteItem.isStatus()) db.daoNote().update(noteItem.getId(), false);

        db.close();

        noteRepo.update(false);
    }

}