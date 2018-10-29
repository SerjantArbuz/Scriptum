package sgtmelon.scriptum.app.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import javax.inject.Inject;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import sgtmelon.scriptum.R;
import sgtmelon.scriptum.app.control.MenuControl;
import sgtmelon.scriptum.app.control.SaveControl;
import sgtmelon.scriptum.app.database.RoomDb;
import sgtmelon.scriptum.app.injection.component.ActivityComponent;
import sgtmelon.scriptum.app.injection.component.DaggerActivityComponent;
import sgtmelon.scriptum.app.injection.module.blank.ActivityBlankModule;
import sgtmelon.scriptum.app.model.NoteRepo;
import sgtmelon.scriptum.app.model.item.NoteItem;
import sgtmelon.scriptum.app.view.callback.NoteCallback;
import sgtmelon.scriptum.app.view.fragment.RollFragment;
import sgtmelon.scriptum.app.view.fragment.TextFragment;
import sgtmelon.scriptum.app.view.parent.BaseActivityParent;
import sgtmelon.scriptum.app.vm.activity.ActivityNoteViewModel;
import sgtmelon.scriptum.app.vm.fragment.FragmentNoteViewModel;
import sgtmelon.scriptum.office.Help;
import sgtmelon.scriptum.office.annot.def.FragmentDef;
import sgtmelon.scriptum.office.annot.def.IntentDef;
import sgtmelon.scriptum.office.annot.def.TypeDef;
import sgtmelon.scriptum.office.intf.MenuIntf;
import sgtmelon.scriptum.office.st.NoteSt;

public final class NoteActivity extends BaseActivityParent
        implements NoteCallback, MenuIntf.Note.DeleteMenuClick {

    //Если Id не существует то завершать активити

    private static final String TAG = NoteActivity.class.getSimpleName();

    @Inject ActivityNoteViewModel vm;
    @Inject FragmentManager fm;

    private SaveControl saveControl;

    private RoomDb db;
    private TextFragment textFragment;
    private RollFragment rollFragment;

    public static Intent getIntent(Context context, int type) {
        Intent intent = new Intent(context, NoteActivity.class);

        intent.putExtra(IntentDef.NOTE_CREATE, true);
        intent.putExtra(IntentDef.NOTE_TYPE, type);

        return intent;
    }

    public static Intent getIntent(Context context, long id) {
        Intent intent = new Intent(context, NoteActivity.class);

        intent.putExtra(IntentDef.NOTE_CREATE, false);
        intent.putExtra(IntentDef.NOTE_ID, id);

        return intent;
    }

    @Override
    protected void onPause() {
        Log.i(TAG, "onPause");
        super.onPause();

        saveControl.onPauseSave(vm.getNoteSt().isEdit());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        ActivityComponent activityComponent = DaggerActivityComponent.builder()
                .activityBlankModule(new ActivityBlankModule(this))
                .build();
        activityComponent.inject(this);

        Bundle bundle = getIntent().getExtras();
        vm.setValue(bundle != null ? bundle : savedInstanceState);

        saveControl = new SaveControl(this);

        setupFragment(savedInstanceState != null);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.i(TAG, "onSaveInstanceState");
        super.onSaveInstanceState(outState);

        outState.putBoolean(IntentDef.NOTE_CREATE, vm.getNoteSt().isCreate());
        outState.putInt(IntentDef.NOTE_TYPE, vm.getNtType());
        outState.putLong(IntentDef.NOTE_ID, vm.getNtId());
    }

    @Override
    public void onBackPressed() {
        Log.i(TAG, "onBackPressed");

        saveControl.setNeedSave(false);

        NoteItem noteItem = vm.getNoteRepo().getNoteItem();
        NoteSt noteSt = vm.getNoteSt();

        if (noteSt.isEdit() && !noteSt.isCreate()) {                  //Если это редактирование и не только что созданная заметка
            FragmentNoteViewModel viewModel;
            MenuControl menuControl;
            switch (noteItem.getType()) {
                case TypeDef.Note.text:
                    if (!textFragment.onMenuSaveClick(true)) {   //Если сохранение не выполнено, возвращает старое
                        menuControl = textFragment.getMenuControl();
                        menuControl.setStartColor(noteItem.getColor());

                        NoteRepo noteRepo = vm.loadData(noteItem.getId());
                        noteItem = noteRepo.getNoteItem();

                        viewModel = textFragment.getViewModel();
                        viewModel.setNoteRepo(noteRepo);
                        textFragment.setViewModel(viewModel);

                        menuControl.startTint(noteItem.getColor());
                        textFragment.setMenuControl(menuControl);

                        textFragment.onMenuEditClick(false);
                    }
                    break;
                case TypeDef.Note.roll:
                    if (!rollFragment.onMenuSaveClick(true)) {   //Если сохранение не выполнено, возвращает старое
                        menuControl = rollFragment.getMenuControl();
                        menuControl.setStartColor(noteItem.getColor());

                        NoteRepo noteRepo = vm.loadData(noteItem.getId());
                        noteItem = noteRepo.getNoteItem();

                        viewModel = rollFragment.getViewModel();
                        viewModel.setNoteRepo(noteRepo);
                        rollFragment.setViewModel(viewModel);

                        menuControl.startTint(noteItem.getColor());
                        rollFragment.setMenuControl(menuControl);

                        rollFragment.onMenuEditClick(false);
                        rollFragment.updateAdapter();
                    }
                    break;
            }
        } else if (noteSt.isCreate()) {     //Если только что создали заметку
            switch (noteItem.getType()) {   //Если сохранение не выполнено, выход без сохранения
                case TypeDef.Note.text:
                    if (!textFragment.onMenuSaveClick(true)) super.onBackPressed();
                    break;
                case TypeDef.Note.roll:
                    if (!rollFragment.onMenuSaveClick(true)) super.onBackPressed();
                    break;
            }
        } else super.onBackPressed();   //Другие случаи (не редактирование)
    }

    @Override
    public void setupFragment(boolean isSave) {
        Log.i(TAG, "setupFragment");

        if (!isSave) {
            NoteSt noteSt = vm.getNoteSt();
            noteSt.setFirst(true);
            vm.setNoteSt(noteSt);
        }

        FragmentTransaction transaction = fm.beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);

        switch (vm.getNoteRepo().getNoteItem().getType()) {
            case TypeDef.Note.text:
                if (isSave) textFragment = (TextFragment) fm.findFragmentByTag(FragmentDef.TEXT);
                else textFragment = new TextFragment();

                saveControl.setNoteMenuClick(textFragment);

                transaction.replace(R.id.fragment_container, textFragment, FragmentDef.TEXT);
                break;
            case TypeDef.Note.roll:
                if (isSave) rollFragment = (RollFragment) fm.findFragmentByTag(FragmentDef.ROLL);
                else rollFragment = new RollFragment();

                saveControl.setNoteMenuClick(rollFragment);

                transaction.replace(R.id.fragment_container, rollFragment, FragmentDef.ROLL);
                break;
        }
        transaction.commit();
    }

    @Override
    public SaveControl getSaveControl() {
        return saveControl;
    }

    @Override
    public void setSaveControl(SaveControl saveControl) {
        this.saveControl = saveControl;
    }

    @Override
    public ActivityNoteViewModel getViewModel() {
        return vm;
    }

    @Override
    public void setViewModel(ActivityNoteViewModel viewModel) {
        vm = viewModel;
    }

    @Override
    public void onMenuRestoreClick() {
        Log.i(TAG, "onMenuRestoreClick");

        db = RoomDb.provideDb(this);
        db.daoNote().update(vm.getNoteRepo().getNoteItem().getId(), Help.Time.getCurrentTime(this), false);
        db.close();

        finish();
    }

    @Override
    public void onMenuRestoreOpenClick() {
        Log.i(TAG, "onMenuRestoreOpenClick");

        NoteSt noteSt = vm.getNoteSt();
        noteSt.setBin(false);

        vm.setNoteSt(noteSt);

        NoteRepo noteRepo = vm.getNoteRepo();
        NoteItem noteItem = noteRepo.getNoteItem();

        noteItem.setChange(Help.Time.getCurrentTime(this));
        noteItem.setBin(false);
        noteRepo.setNoteItem(noteItem);

        vm.setNoteRepo(noteRepo);

        db = RoomDb.provideDb(this);
        db.daoNote().update(noteItem);
        db.close();

        FragmentNoteViewModel viewModel;
        MenuControl menuControl;
        switch (vm.getNoteRepo().getNoteItem().getType()) {
            case TypeDef.Note.text:
                viewModel = textFragment.getViewModel();
                viewModel.setNoteRepo(noteRepo);
                textFragment.setViewModel(viewModel);

                menuControl = textFragment.getMenuControl();
                menuControl.setMenuGroupVisible(false, false, true);
                textFragment.setMenuControl(menuControl);
                break;
            case TypeDef.Note.roll:
                viewModel = rollFragment.getViewModel();
                viewModel.setNoteRepo(noteRepo);
                rollFragment.setViewModel(viewModel);

                menuControl = rollFragment.getMenuControl();
                menuControl.setMenuGroupVisible(false, false, true);
                rollFragment.setMenuControl(menuControl);
                break;
        }
    }

    @Override
    public void onMenuClearClick() {
        Log.i(TAG, "onMenuClearClick");

        db = RoomDb.provideDb(this);
        db.daoNote().delete(vm.getNoteRepo().getNoteItem().getId());
        db.close();

        vm.setRepoNote(false);

        finish();
    }

    @Override
    public void onMenuDeleteClick() {
        Log.i(TAG, "onMenuDeleteClick");

        NoteItem noteItem = vm.getNoteRepo().getNoteItem();

        db = RoomDb.provideDb(this);
        db.daoNote().update(noteItem.getId(), Help.Time.getCurrentTime(this), true);
        if (noteItem.isStatus()) {
            db.daoNote().update(noteItem.getId(), false);
        }
        db.close();

        vm.setRepoNote(false);

        finish();
    }

}