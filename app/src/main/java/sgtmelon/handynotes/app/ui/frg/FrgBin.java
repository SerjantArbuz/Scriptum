package sgtmelon.handynotes.app.ui.frg;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import sgtmelon.handynotes.R;
import sgtmelon.handynotes.app.adapter.AdapterNote;
import sgtmelon.handynotes.app.db.DbRoom;
import sgtmelon.handynotes.app.model.item.ItemNote;
import sgtmelon.handynotes.app.model.repo.RepoNote;
import sgtmelon.handynotes.app.ui.act.ActMain;
import sgtmelon.handynotes.app.ui.act.ActNote;
import sgtmelon.handynotes.app.vm.VmFrgBin;
import sgtmelon.handynotes.databinding.FrgBinBinding;
import sgtmelon.handynotes.office.Help;
import sgtmelon.handynotes.office.annot.Db;
import sgtmelon.handynotes.office.annot.def.db.DefBin;
import sgtmelon.handynotes.office.intf.IntfAlert;
import sgtmelon.handynotes.office.intf.IntfItem;
import sgtmelon.handynotes.view.alert.AlertOption;

public class FrgBin extends Fragment implements Toolbar.OnMenuItemClickListener,
        IntfItem.Click, IntfItem.LongClick, IntfAlert.OptionBin {

    //region Variable
    private static final String TAG = "FrgBin";

    private DbRoom db;

    private FrgBinBinding binding;
    private View frgView;

    private Context context;
    private ActMain activity;

    private VmFrgBin vm;
    //endregion

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "onResume");

        updateAdapter();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView");

        binding = DataBindingUtil.inflate(inflater, R.layout.frg_bin, container, false);

        frgView = binding.getRoot();

        context = getContext();
        activity = (ActMain) getActivity();

        vm = ViewModelProviders.of(this).get(VmFrgBin.class);

        setupToolbar();
        setupRecyclerView();

        return frgView;
    }

    private void bind(int listSize) {
        binding.setListEmpty(listSize == 0);
        binding.executePendingBindings();
    }

    private MenuItem mItemClearBin;

    private void setupToolbar() {
        Log.i(TAG, "setupToolbar");

        Toolbar toolbar = frgView.findViewById(R.id.incToolbar_tb);
        toolbar.setTitle(getString(R.string.title_frg_bin));

        toolbar.inflateMenu(R.menu.menu_frg_bin);
        toolbar.setOnMenuItemClickListener(this);

        Menu menu = toolbar.getMenu();
        mItemClearBin = menu.findItem(R.id.menu_frgBin_clear);

        Help.Icon.tintMenuIcon(context, mItemClearBin);
    }

    private void setMenuItemClearVisible() {
        Log.i(TAG, "setMenuItemClearVisible");

        if (vm.getListRepo().size() == 0) mItemClearBin.setVisible(false);
        else mItemClearBin.setVisible(true);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        Log.i(TAG, "onMenuItemClick");

        switch (item.getItemId()) {
            case R.id.menu_frgBin_clear:
                AlertDialog.Builder alert = new AlertDialog.Builder(context, R.style.AppTheme_AlertDialog);
                alert.setTitle(getString(R.string.dialog_title_clear_bin))
                        .setMessage(getString(R.string.dialog_text_clear_bin))
                        .setPositiveButton(getString(R.string.dialog_btn_yes), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {

                                db = DbRoom.provideDb(context);
                                db.daoNote().clearBin();
                                db.close();

                                vm.setListRepo();

                                adapterNote.updateAdapter(vm.getListRepo());
                                adapterNote.notifyDataSetChanged();

                                setMenuItemClearVisible();
                                bind(0);

                                activity.frgRank.updateAdapter();

                                dialog.cancel();
                            }
                        })
                        .setNegativeButton(getString(R.string.dialog_btn_no), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        })
                        .setCancelable(true);

                AlertDialog dialog = alert.create();
                dialog.show();
                return true;
        }
        return false;
    }

    private AdapterNote adapterNote;

    private void setupRecyclerView() {
        Log.i(TAG, "setupRecyclerView");

        final DefaultItemAnimator recyclerViewEndAnim = new DefaultItemAnimator() {
            @Override
            public void onAnimationFinished(RecyclerView.ViewHolder viewHolder) {
                bind(vm.getListRepo().size());
            }
        };

        RecyclerView recyclerView = frgView.findViewById(R.id.frgBin_rv);
        recyclerView.setItemAnimator(recyclerViewEndAnim);

        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);

        adapterNote = new AdapterNote();
        recyclerView.setAdapter(adapterNote);

        adapterNote.setCallback(this, this);
    }

    public void updateAdapter() {
        Log.i(TAG, "updateAdapter");

        List<RepoNote> listRepo = vm.loadData(DefBin.in);

        adapterNote.updateAdapter(listRepo);
        adapterNote.notifyDataSetChanged();

        setMenuItemClearVisible();
        bind(listRepo.size());
    }

    @Override
    public void onItemClick(View view, int p) {
        Log.i(TAG, "onItemClick");

        ItemNote itemNote = vm.getListRepo().get(p).getItemNote();

        Intent intent = new Intent(context, ActNote.class);

        intent.putExtra(Db.NT_ID, itemNote.getId());
        intent.putExtra(Db.RK_VS, activity.frgRank.vm.getRepoRank().getVisible());
        intent.putExtra(DefPages.CREATE, false);

        startActivity(intent);
    }

    @Override
    public void onItemLongClick(View view, int p) {
        Log.i(TAG, "onItemLongClick");

        AlertOption alertOption = new AlertOption(context, vm.getListRepo().get(p).getItemNote(), p);
        alertOption.setOptionBin(this);
        alertOption.showOptionBin();
    }

    @Override
    public void onOptionRestoreClick(ItemNote itemNote, int p) {
        Log.i(TAG, "onOptionRestoreClick");

        db = DbRoom.provideDb(context);
        db.daoNote().update(itemNote.getId(), Help.Time.getCurrentTime(context), false);
        db.close();

        List<RepoNote> listRepo = vm.getListRepo();
        listRepo.remove(p);
        vm.setListRepo(listRepo);

        adapterNote.updateAdapter(listRepo);
        adapterNote.notifyItemRemoved(p);

        setMenuItemClearVisible();

        activity.frgNotes.updateAdapter();
    }

    @Override
    public void onOptionClearClick(ItemNote itemNote, int p) {
        Log.i(TAG, "onOptionClearClick");

        db = DbRoom.provideDb(context);
        db.daoNote().delete(itemNote.getId());
        db.close();

        List<RepoNote> listRepo = vm.getListRepo();
        listRepo.remove(p);
        vm.setListRepo(listRepo);

        adapterNote.updateAdapter(listRepo);
        adapterNote.notifyItemRemoved(p);

        setMenuItemClearVisible();

        activity.frgRank.updateAdapter();
    }

}