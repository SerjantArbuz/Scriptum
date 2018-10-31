package sgtmelon.scriptum.app.view.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

import java.util.List;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import sgtmelon.scriptum.R;
import sgtmelon.scriptum.app.control.SaveControl;
import sgtmelon.scriptum.app.database.RoomDb;
import sgtmelon.scriptum.app.injection.component.DaggerFragmentComponent;
import sgtmelon.scriptum.app.injection.component.FragmentComponent;
import sgtmelon.scriptum.app.injection.module.FragmentArchModule;
import sgtmelon.scriptum.app.injection.module.blank.FragmentBlankModule;
import sgtmelon.scriptum.app.model.NoteRepo;
import sgtmelon.scriptum.app.model.item.NoteItem;
import sgtmelon.scriptum.app.model.item.RollItem;
import sgtmelon.scriptum.app.view.parent.NoteFragmentParent;
import sgtmelon.scriptum.app.vm.activity.ActivityNoteViewModel;
import sgtmelon.scriptum.databinding.FragmentTextBinding;
import sgtmelon.scriptum.office.Help;
import sgtmelon.scriptum.office.annot.def.TypeDef;
import sgtmelon.scriptum.office.st.NoteSt;

public final class TextFragment extends NoteFragmentParent {

    private static final String TAG = TextFragment.class.getSimpleName();

    @Inject FragmentTextBinding binding;

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView");
        super.onCreateView(inflater, container, savedInstanceState);

        final FragmentComponent fragmentComponent = DaggerFragmentComponent.builder()
                .fragmentBlankModule(new FragmentBlankModule(this))
                .fragmentArchModule(new FragmentArchModule(inflater, container))
                .build();
        fragmentComponent.inject(this);

        frgView = binding.getRoot();

        return frgView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        Log.i(TAG, "onActivityCreated");
        super.onActivityCreated(savedInstanceState);

        final ActivityNoteViewModel viewModel = noteCallback.getViewModel();
        if (vm.isEmpty()) vm.setNoteRepo(viewModel.getNoteRepo());

        setupToolbar();
        setupDialog();
        setupEnter();

        final NoteSt noteSt = viewModel.getNoteSt();

        onMenuEditClick(noteSt.isEdit());

        noteSt.setFirst(false);
        viewModel.setNoteSt(noteSt);
        noteCallback.setViewModel(viewModel);
    }

    @Override
    protected void bind(boolean keyEdit) {
        Log.i(TAG, "bind: keyEdit=" + keyEdit);

        binding.setNoteItem(vm.getNoteRepo().getNoteItem());
        binding.setKeyEdit(keyEdit);

        binding.executePendingBindings();
    }

    @Override
    protected void setupDialog() {
        Log.i(TAG, "setupDialog");
        super.setupDialog();

        dlgConvert.setMessage(getString(R.string.dialog_text_convert_to_roll));
        dlgConvert.setPositiveListener((dialogInterface, i) -> {
            final NoteRepo noteRepo = vm.getNoteRepo();
            final NoteItem noteItem = noteRepo.getNoteItem();
            final String[] textToRoll = noteItem.getText().split("\n");   //Получаем пункты списка

            db = RoomDb.provideDb(context);
            final List<RollItem> listRoll = db.daoRoll().insert(noteItem.getId(), textToRoll);

            noteItem.setChange(Help.Time.getCurrentTime(context));
            noteItem.setType(TypeDef.roll);
            noteItem.setText(0, listRoll.size());

            db.daoNote().update(noteItem);
            db.close();

            noteRepo.setNoteItem(noteItem);
            noteRepo.setListRoll(listRoll);

            vm.setNoteRepo(noteRepo);

            final ActivityNoteViewModel viewModel = noteCallback.getViewModel();
            viewModel.setNoteRepo(noteRepo);
            noteCallback.setViewModel(viewModel);

            noteCallback.setupFragment(false);
        });
    }

    private void setupEnter() {
        Log.i(TAG, "setupEnter");

        final EditText nameEnter = frgView.findViewById(R.id.name_enter);
        final EditText textEnter = frgView.findViewById(R.id.text_enter);

        nameEnter.setOnEditorActionListener((textView, i, keyEvent) -> {
            if (i == EditorInfo.IME_ACTION_NEXT) {
                textEnter.requestFocus();
                return true;
            }
            return false;
        });
    }

    /**
     * Нажатие на клавишу назад
     */
    @Override
    public void onClick(View view) {
        Log.i(TAG, "onClick");

        Help.hideKeyboard(context, activity.getCurrentFocus());

        final ActivityNoteViewModel viewModel = noteCallback.getViewModel();
        final NoteSt noteSt = viewModel.getNoteSt();

        NoteRepo noteRepo = vm.getNoteRepo();
        NoteItem noteItem = noteRepo.getNoteItem();

        //Если редактирование и текст в хранилище не пустой
        if (!noteSt.isCreate() && noteSt.isEdit() && !noteItem.getText().equals("")) {
            menuControl.setStartColor(noteItem.getColor());

            db = RoomDb.provideDb(context);
            noteRepo = db.daoNote().get(context, noteItem.getId());
            noteItem = noteRepo.getNoteItem();
            db.close();

            vm.setNoteRepo(noteRepo);

            viewModel.setNoteRepo(noteRepo);
            noteCallback.setViewModel(viewModel);

            onMenuEditClick(false);

            menuControl.startTint(noteItem.getColor());
        } else {
            final SaveControl saveControl = noteCallback.getSaveControl();
            saveControl.setNeedSave(false);
            noteCallback.setSaveControl(saveControl);

            activity.finish();
        }
    }

    @Override
    public boolean onMenuSaveClick(boolean editModeChange) {
        Log.i(TAG, "onMenuSaveClick");

        final NoteRepo noteRepo = vm.getNoteRepo();
        final NoteItem noteItem = noteRepo.getNoteItem();
        if (!noteItem.getText().equals("")) {
            noteItem.setChange(Help.Time.getCurrentTime(context));

            if (editModeChange) {
                Help.hideKeyboard(context, activity.getCurrentFocus());
                onMenuEditClick(false);
            }

            db = RoomDb.provideDb(context);

            final ActivityNoteViewModel viewModel = noteCallback.getViewModel();
            final NoteSt noteSt = viewModel.getNoteSt();
            if (noteSt.isCreate()) {
                noteSt.setCreate(false);
                viewModel.setNoteSt(noteSt);

                if (!editModeChange) {
                    menuControl.setDrawable(true, true);
                }

                final long id = db.daoNote().insert(noteItem);
                noteItem.setId(id);
            } else {
                db.daoNote().update(noteItem);
            }
            db.daoRank().update(noteItem.getId(), noteItem.getRankId());
            db.close();

            noteRepo.setNoteItem(noteItem);

            vm.setNoteRepo(noteRepo);

            viewModel.setNoteRepo(noteRepo);
            noteCallback.setViewModel(viewModel);
            return true;
        } else return false;
    }

    @Override
    public void onMenuEditClick(boolean editMode) {
        Log.i(TAG, "onMenuEditClick: " + editMode);

        final ActivityNoteViewModel viewModel = noteCallback.getViewModel();
        final NoteSt noteSt = viewModel.getNoteSt();
        noteSt.setEdit(editMode);

        menuControl.setDrawable(editMode && !noteSt.isCreate(),
                !noteSt.isCreate() && !noteSt.isFirst());
        menuControl.setMenuGroupVisible(noteSt.isBin(), editMode, !noteSt.isBin() && !editMode);

        bind(editMode);

        viewModel.setNoteSt(noteSt);
        noteCallback.setViewModel(viewModel);

        final SaveControl saveControl = noteCallback.getSaveControl();
        saveControl.setSaveHandlerEvent(editMode);
        noteCallback.setSaveControl(saveControl);
    }

    @Override
    public void onMenuCheckClick() {
        Log.i(TAG, "onMenuCheckClick");

    }

}