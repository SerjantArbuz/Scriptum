package sgtmelon.handynotes.app.ui.frg;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
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
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

import sgtmelon.handynotes.R;
import sgtmelon.handynotes.app.adapter.AdapterRank;
import sgtmelon.handynotes.app.db.DbRoom;
import sgtmelon.handynotes.app.model.item.ItemRank;
import sgtmelon.handynotes.app.model.repo.RepoRank;
import sgtmelon.handynotes.app.ui.act.ActMain;
import sgtmelon.handynotes.app.vm.VmFrgRank;
import sgtmelon.handynotes.databinding.FrgRankBinding;
import sgtmelon.handynotes.office.Help;
import sgtmelon.handynotes.office.intf.IntfItem;
import sgtmelon.handynotes.office.st.StDrag;
import sgtmelon.handynotes.view.alert.AlertRename;

public class FrgRank extends Fragment implements IntfItem.Click, IntfItem.LongClick,
        View.OnClickListener, View.OnLongClickListener {

    //region Variable
    private static final String TAG = "FrgRank";

    private DbRoom db;

    private FrgRankBinding binding;
    private View frgView;

    private Context context;
    private ActMain activity;

    public VmFrgRank vm;
//    public RepoRank repoRank;
    //endregion

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "onResume");

        updateAdapter();
        tintButton();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView");

        binding = DataBindingUtil.inflate(inflater, R.layout.frg_rank, container, false);
        frgView = binding.getRoot();

        context = getContext();
        activity = (ActMain) getActivity();

        vm = ViewModelProviders.of(this).get(VmFrgRank.class);

        setupToolbar();
        setupRecyclerView();

        return frgView;
    }

    private void bind(int listSize) {
        binding.setListEmpty(listSize == 0);
        binding.executePendingBindings();
    }

    private ImageButton rankCancel;
    private ImageButton rankAdd;
    private EditText rankEnter;

    private void setupToolbar() {
        Log.i(TAG, "setupToolbar");

        Toolbar toolbar = frgView.findViewById(R.id.incToolbar_tb);
        toolbar.setTitle(getString(R.string.title_frg_rank));

        rankCancel = frgView.findViewById(R.id.incToolbarRank_ib_cancel);
        rankAdd = frgView.findViewById(R.id.incToolbarRank_ib_add);
        rankEnter = frgView.findViewById(R.id.incToolbarRank_et_enter);

        rankEnter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                tintButton();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        rankEnter.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE) {
                    String name = rankEnter.getText().toString().toUpperCase();
                    if (!name.equals("") && !vm.getRepoRank().getListName().contains(name)) {
                        onClick(rankAdd);
                    }
                    return true;
                }
                return false;
            }
        });

        rankCancel.setOnClickListener(this);
        rankAdd.setOnClickListener(this);
        rankAdd.setOnLongClickListener(this);
    }

    private void tintButton() {
        String name = rankEnter.getText().toString().toUpperCase();

        Help.Icon.tintButton(context, rankCancel, R.drawable.ic_button_cancel, name);
        Help.Icon.tintButton(context, rankAdd, R.drawable.ic_menu_rank, name, !vm.getRepoRank().getListName().contains(name));
    }

    private String clearEnter() {
        String name = rankEnter.getText().toString();
        rankEnter.setText("");
        return name;
    }

    @Override
    public void onClick(View view) {
        Log.i(TAG, "onClick");

        switch (view.getId()) {
            case R.id.incToolbarRank_ib_cancel:
                clearEnter();
                break;
            case R.id.incToolbarRank_ib_add:
                RepoRank repoRank = vm.getRepoRank();

                int rankPs = repoRank.size();
                String name = clearEnter();

                ItemRank itemRank = new ItemRank(rankPs, name);

                db = DbRoom.provideDb(context);
                long rankId = db.daoRank().insert(itemRank);
                db.close();

                itemRank.setId(rankId);
                repoRank.add(rankPs, itemRank);

                vm.setRepoRank(repoRank);
                adapterRank.updateAdapter(repoRank.getListRank());

                if (repoRank.size() == 1) {
                    bind(repoRank.size());
                    adapterRank.notifyItemInserted(rankPs);
                } else {
                    if (layoutManager.findLastVisibleItemPosition() == rankPs - 1) {    //Если видимая позиция равна позиции куда добавили заметку
                        recyclerView.scrollToPosition(rankPs);                          //Прокручиваем до края, незаметно
                        adapterRank.notifyItemInserted(rankPs);                         //Добавляем элемент с анимацией
                    } else {
                        recyclerView.smoothScrollToPosition(rankPs);                    //Медленно прокручиваем, через весь список
                        adapterRank.notifyDataSetChanged();                             //Добавляем элемент без анимации
                    }
                }
                break;
        }
    }

    @Override
    public boolean onLongClick(View view) {
        Log.i(TAG, "onLongClick");

        int ps = 0;
        String name = clearEnter();

        ItemRank itemRank = new ItemRank(ps - 1, name);

        db = DbRoom.provideDb(context);
        long rankId = db.daoRank().insert(itemRank);
        db.daoRank().update(ps);
        db.close();

        itemRank.setId(rankId);

        RepoRank repoRank = vm.getRepoRank();
        repoRank.add(ps, itemRank);
        vm.setRepoRank(repoRank);

        adapterRank.updateAdapter(repoRank.getListRank());

        if (repoRank.size() == 1) bind(repoRank.size());
        else {
            if (layoutManager.findFirstVisibleItemPosition() == ps) {   //Если видимая позиция равна позиции куда добавили заметку
                recyclerView.scrollToPosition(ps);                      //Прокручиваем до края, незаметно
                adapterRank.notifyItemInserted(ps);                     //Добавляем элемент с анимацией
            } else {
                recyclerView.smoothScrollToPosition(ps);                //Медленно прокручиваем, через весь список
                adapterRank.notifyDataSetChanged();                         //Добавляем элемент без анимации
            }
        }
        return true;
    }

    //region RecyclerView variables
    private StDrag stDrag;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;

    private AdapterRank adapterRank;
    //endregion

    private void setupRecyclerView() {
        Log.i(TAG, "setupRecyclerView");

        stDrag = new StDrag();

        final DefaultItemAnimator recyclerViewEndAnim = new DefaultItemAnimator() {
            @Override
            public void onAnimationFinished(RecyclerView.ViewHolder viewHolder) {
                bind(vm.getRepoRank().size());
            }
        };

        recyclerView = frgView.findViewById(R.id.frgRank_rv);
        recyclerView.setItemAnimator(recyclerViewEndAnim);

        layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);

        adapterRank = new AdapterRank();
        recyclerView.setAdapter(adapterRank);

        adapterRank.setCallback(this, this, stDrag);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(touchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    public void updateAdapter() {
        Log.i(TAG, "updateAdapter");

        RepoRank repoRank = vm.loadData();

        adapterRank.updateAdapter(repoRank.getListRank());
        adapterRank.notifyDataSetChanged();

        bind(repoRank.size());
    }

    @Override
    public void onItemClick(View view, final int p) {
        Log.i(TAG, "onItemClick");

        final RepoRank repoRank = vm.getRepoRank();
        final ItemRank itemRank = repoRank.get(p);

        switch (view.getId()) {
            case R.id.itemRank_bv_visible:
                itemRank.setVisible(!itemRank.isVisible());

                repoRank.set(p, itemRank);

                vm.setRepoRank(repoRank);
                adapterRank.updateAdapter(p, itemRank);

                db = DbRoom.provideDb(context);
                db.daoRank().update(itemRank);
                db.close();

                activity.frgNotes.updateAdapter();
                activity.frgBin.updateAdapter();
                break;
            case R.id.itemRank_ll_click:
                final AlertRename alert = new AlertRename(context, R.style.AppTheme_AlertDialog);
                alert.setTitle(itemRank.getName())
                        .setPositiveButton(getString(R.string.dialog_btn_accept), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                itemRank.setName(alert.getRename());

                                db = DbRoom.provideDb(context);
                                db.daoRank().update(itemRank);
                                db.close();

                                repoRank.set(p, itemRank);

                                tintButton();

                                vm.setRepoRank(repoRank);

                                adapterRank.updateAdapter(p, itemRank);
                                adapterRank.notifyItemChanged(p);

                                dialog.cancel();
                            }
                        })
                        .setNegativeButton(getString(R.string.dialog_btn_cancel), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        })
                        .setCancelable(true);

                AlertDialog dialog = alert.create();
                dialog.show();

                alert.setTextChange(dialog, repoRank.getListName());
                break;
            case R.id.itemRank_ib_cancel:
                itemRank.setVisible(true);                  //Чтобы отобразить заметки в статус баре, если были скрыты

                db = DbRoom.provideDb(context);
                db.daoRank().delete(repoRank.get(p).getName());
                db.daoRank().update(p);
                db.close();

                repoRank.remove(p);

                vm.setRepoRank(repoRank);

                adapterRank.updateAdapter(repoRank.getListRank());
                adapterRank.notifyItemRemoved(p);

                activity.frgNotes.updateAdapter();
                activity.frgBin.updateAdapter();
                break;
        }
    }

    @Override
    public void onItemLongClick(View view, int p) {
        Log.i(TAG, "onItemLongClick");

        RepoRank repoRank = vm.getRepoRank();

        boolean[] iconStartAnim = adapterRank.getStartAnim();
        boolean clickVisible = repoRank.get(p).isVisible();

        for (int i = 0; i < repoRank.size(); i++) {
            if (i != p) {
                ItemRank itemRank = repoRank.get(i);
                boolean isVisible = itemRank.isVisible();

                if (clickVisible == isVisible) {
                    iconStartAnim[i] = true;
                    itemRank.setVisible(!isVisible);
                    repoRank.set(i, itemRank);
                }
            }
        }

        List<ItemRank> listRank = repoRank.getListRank();

        vm.setRepoRank(repoRank);

        adapterRank.updateAdapter(listRank, iconStartAnim);
        adapterRank.notifyDataSetChanged();

        db = DbRoom.provideDb(context);
        db.daoRank().updateRank(listRank);
        db.close();

        activity.frgNotes.updateAdapter();
        activity.frgBin.updateAdapter();
    }

    private final ItemTouchHelper.Callback touchCallback = new ItemTouchHelper.Callback() {

        private int dragStart;
        private int dragEnd;

        @Override
        public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            int flagsDrag = stDrag.isDrag()
                    ? ItemTouchHelper.UP | ItemTouchHelper.DOWN
                    : 0;

            int flagsSwipe = 0;

            return makeMovementFlags(flagsDrag, flagsSwipe);
        }

        @Override
        public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
            super.onSelectedChanged(viewHolder, actionState);

            switch (actionState) {
                case ItemTouchHelper.ACTION_STATE_DRAG:
                    dragStart = viewHolder.getAdapterPosition();
                    break;
            }
        }

        @Override
        public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            super.clearView(recyclerView, viewHolder);

            dragEnd = viewHolder.getAdapterPosition();
            if (dragStart != dragEnd) {
                db = DbRoom.provideDb(context);
                List<ItemRank> listRank = db.daoRank().update(dragStart, dragEnd);
                db.close();

                RepoRank repoRank = vm.getRepoRank();
                repoRank.setListRank(listRank);
                vm.setRepoRank(repoRank);

                adapterRank.updateAdapter(listRank);
                adapterRank.notifyDataSetChanged();

                activity.frgNotes.updateAdapter();
                activity.frgBin.updateAdapter();
            }
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

        }

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            int oldPs = viewHolder.getAdapterPosition();        //Старая позиция (откуда взяли)
            int newPs = target.getAdapterPosition();            //Новая позиция (куда отпустили)

            RepoRank repoRank = vm.getRepoRank();
            repoRank.move(oldPs, newPs);
            vm.setRepoRank(repoRank);

            adapterRank.updateAdapter(repoRank.getListRank());
            adapterRank.notifyItemMoved(oldPs, newPs);

            return true;
        }
    };

}