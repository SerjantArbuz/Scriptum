package sgtmelon.scriptum.app.view.parent;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import sgtmelon.safedialog.library.ColorDialog;
import sgtmelon.safedialog.library.MessageDialog;
import sgtmelon.safedialog.library.MultiplyDialog;
import sgtmelon.scriptum.R;
import sgtmelon.scriptum.app.control.InputControl;
import sgtmelon.scriptum.app.control.MenuControl;
import sgtmelon.scriptum.app.control.MenuControlAnim;
import sgtmelon.scriptum.app.database.RoomDb;
import sgtmelon.scriptum.app.injection.component.DaggerFragmentComponent;
import sgtmelon.scriptum.app.injection.component.FragmentComponent;
import sgtmelon.scriptum.app.injection.module.FragmentArchModule;
import sgtmelon.scriptum.app.injection.module.blank.FragmentBlankModule;
import sgtmelon.scriptum.app.model.NoteRepo;
import sgtmelon.scriptum.app.model.item.NoteItem;
import sgtmelon.scriptum.app.view.callback.NoteCallback;
import sgtmelon.scriptum.app.view.fragment.RollFragment;
import sgtmelon.scriptum.app.view.fragment.TextFragment;
import sgtmelon.scriptum.app.vm.fragment.FragmentNoteViewModel;
import sgtmelon.scriptum.office.annot.def.ColorDef;
import sgtmelon.scriptum.office.annot.def.DialogDef;
import sgtmelon.scriptum.office.annot.def.InputDef;
import sgtmelon.scriptum.office.annot.def.IntentDef;
import sgtmelon.scriptum.office.intf.BindIntf;
import sgtmelon.scriptum.office.intf.InputTextWatcher;
import sgtmelon.scriptum.office.intf.MenuIntf;
import sgtmelon.scriptum.office.st.NoteSt;
import sgtmelon.scriptum.office.utils.HelpUtils;

/**
 * Класс родитель для фрагментов редактирования заметок
 * {@link TextFragment}, {@link RollFragment}
 */
public abstract class NoteFragmentParent extends Fragment implements View.OnClickListener, BindIntf,
        MenuIntf.Note.NoteMenuClick {

    private static final String TAG = NoteFragmentParent.class.getSimpleName();

    protected final InputControl inputControl = new InputControl();

    // TODO: 17.12.2018 сделать долгое нажатие undo/redo

    protected Context context;
    protected Activity activity;
    protected NoteCallback noteCallback;

    @Inject
    @Named(DialogDef.CONVERT)
    protected MessageDialog dlgConvert;

    protected EditText nameEnter;

    @Inject protected FragmentManager fm;
    protected RoomDb db;
    protected View frgView;
    @Inject protected FragmentNoteViewModel vm;

    protected boolean rankEmpty;

    protected MenuControl menuControl;
    protected MenuIntf.Note.DeleteMenuClick deleteMenuClick;

    @Inject ColorDialog colorDialog;
    @Inject
    @Named(DialogDef.RANK)
    MultiplyDialog dlgRank;

    @Override
    public void onAttach(Context context) {
        Log.i(TAG, "onAttach");
        super.onAttach(context);

        this.context = context;
        activity = getActivity();

        if (context instanceof NoteCallback) {
            noteCallback = (NoteCallback) context;
        } else {
            throw new ClassCastException(NoteCallback.class.getSimpleName() +
                    " interface not installed in " + TAG);
        }

        if (context instanceof MenuIntf.Note.DeleteMenuClick) {
            deleteMenuClick = (MenuIntf.Note.DeleteMenuClick) context;
        } else {
            throw new ClassCastException(MenuIntf.Note.DeleteMenuClick.class.getSimpleName() +
                    " interface not installed in " + TAG);
        }

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView");

        final FragmentComponent fragmentComponent = DaggerFragmentComponent.builder()
                .fragmentBlankModule(new FragmentBlankModule(this))
                .fragmentArchModule(new FragmentArchModule(inflater, container))
                .build();
        fragmentComponent.inject(this);

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        final Bundle bundle = getArguments();
        if (bundle != null) {
            rankEmpty = bundle.getBoolean(IntentDef.RANK_EMPTY);
        } else if (savedInstanceState != null) {
            rankEmpty = savedInstanceState.getBoolean(IntentDef.RANK_EMPTY);
        }

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(IntentDef.RANK_EMPTY, rankEmpty);
    }

    /**
     * Установка в биндинг слоя неизменяемых данных, как интерфейсы
     */
    public abstract void setupBinding();

    /**
     * Биндинг отображения элементов управления для конкретного режима редактирования
     *
     * @param editMode - Режим редактирования
     */
    public abstract void bindEdit(boolean editMode);

    @CallSuper
    protected void setupToolbar() {
        Log.i(TAG, "setupToolbar");

        final Toolbar toolbar = frgView.findViewById(R.id.toolbar);
        final View indicator = frgView.findViewById(R.id.color_view);
        final NoteItem noteItem = vm.getNoteRepo().getNoteItem();

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            menuControl = new MenuControl(context);
        } else {
            menuControl = new MenuControlAnim(context);
        }

        menuControl.setWindow(activity.getWindow());
        menuControl.setToolbar(toolbar);
        menuControl.setIndicator(indicator);
        menuControl.setColor(noteItem.getColor());

        final NoteSt noteSt = noteCallback.getViewModel().getNoteSt();

        menuControl.setupDrawable();
        menuControl.setDrawable(noteSt.isEdit() && !noteSt.isCreate(), false);

        toolbar.setNavigationOnClickListener(this);
    }

    @CallSuper
    protected void setupDialog() {
        Log.i(TAG, "setupDialog");

        colorDialog.setTitle(getString(R.string.dialog_title_color));
        colorDialog.setPositiveListener((dialogInterface, i) -> {
            int check = colorDialog.getCheck();

            final NoteRepo noteRepo = vm.getNoteRepo();
            final NoteItem noteItem = noteRepo.getNoteItem();

            inputControl.onColorChange(noteItem.getColor(), check);
            bindInput();

            noteItem.setColor(check);
            noteRepo.setNoteItem(noteItem);

            vm.setNoteRepo(noteRepo);

            menuControl.startTint(check);
        });

        db = RoomDb.provideDb(context);
        final String[] name = db.daoRank().getName();
        db.close();

        dlgRank.setRows(name);
        dlgRank.setPositiveListener((dialogInterface, i) -> {
            final boolean[] check = dlgRank.getCheck();

            db = RoomDb.provideDb(context);
            final Long[] id = db.daoRank().getId();
            db.close();

            final List<Long> rankId = new ArrayList<>();
            final List<Long> rankPs = new ArrayList<>();

            for (int j = 0; j < id.length; j++) {
                if (check[j]) {
                    rankId.add(id[j]);
                    rankPs.add((long) j);
                }
            }

            final NoteRepo noteRepo = vm.getNoteRepo();
            final NoteItem noteItem = noteRepo.getNoteItem();

            noteItem.setRankId(rankId);
            noteItem.setRankPs(rankPs);
            noteRepo.setNoteItem(noteItem);

            vm.setNoteRepo(noteRepo);

            inputControl.onRankChange(noteItem.getRankId(), rankId);
            bindInput();
        });
    }

    @CallSuper
    protected void setupEnter() {
        Log.i(TAG, "setupEnter");

        nameEnter = frgView.findViewById(R.id.name_enter);
        nameEnter.addTextChangedListener(
                new InputTextWatcher(nameEnter, InputDef.name, this, inputControl)
        );
    }

    public final void startTintToolbar(@ColorDef int startColor, @ColorDef int endColor) {
        menuControl.setStartColor(startColor);
        menuControl.startTint(endColor);
    }

    public final FragmentNoteViewModel getViewModel() {
        return vm;
    }

    public final void setViewModel(FragmentNoteViewModel viewModel) {
        vm = viewModel;
    }

    @Override
    public final void onMenuRankClick() {
        Log.i(TAG, "onMenuRankClick");

        HelpUtils.hideKeyboard(context, activity.getCurrentFocus());

        final NoteItem noteItem = vm.getNoteRepo().getNoteItem();

        db = RoomDb.provideDb(context);
        final boolean[] check = db.daoRank().getCheck(noteItem.getRankId());
        db.close();

        dlgRank.setArguments(check);
        dlgRank.show(fm, DialogDef.RANK);
    }

    @Override
    public final void onMenuColorClick() {
        Log.i(TAG, "onMenuColorClick");

        HelpUtils.hideKeyboard(context, activity.getCurrentFocus());

        final NoteItem noteItem = vm.getNoteRepo().getNoteItem();

        colorDialog.setArguments(noteItem.getColor());
        colorDialog.show(fm, DialogDef.COLOR);

        menuControl.setStartColor(noteItem.getColor());
    }

    @Override
    public final void onMenuBindClick() {
        Log.i(TAG, "onMenuBindClick");

        final NoteRepo noteRepo = vm.getNoteRepo();
        final NoteItem noteItem = noteRepo.getNoteItem();

        if (!noteItem.isStatus()) {
            noteItem.setStatus(true);
            noteRepo.update(true);
        } else {
            noteItem.setStatus(false);
            noteRepo.update(false);
        }

        bindEdit(false);

        db = RoomDb.provideDb(context);
        db.daoNote().update(noteItem.getId(), noteItem.isStatus());
        db.close();

        noteRepo.setNoteItem(noteItem);

        vm.setNoteRepo(noteRepo);

        noteCallback.getViewModel().setNoteRepo(noteRepo);
    }

    @Override
    public final void onMenuConvertClick() {
        Log.i(TAG, "onMenuConvertClick");

        dlgConvert.show(fm, DialogDef.CONVERT);
    }

}