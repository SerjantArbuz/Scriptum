package sgtmelon.handynotes.ui.act;

import android.arch.persistence.room.Room;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import sgtmelon.handynotes.R;
import sgtmelon.handynotes.adapter.AdapterPager;
import sgtmelon.handynotes.db.DbRoom;
import sgtmelon.handynotes.interfaces.menu.MenuMainClick;
import sgtmelon.handynotes.model.state.StateNote;
import sgtmelon.handynotes.db.DbDesc;
import sgtmelon.handynotes.Help;
import sgtmelon.handynotes.control.menu.MenuMain;
import sgtmelon.handynotes.model.manager.ManagerRoll;
import sgtmelon.handynotes.model.manager.ManagerStatus;
import sgtmelon.handynotes.ui.frg.FrgBin;
import sgtmelon.handynotes.ui.frg.FrgNote;
import sgtmelon.handynotes.ui.frg.FrgRank;

public class ActMain extends AppCompatActivity implements MenuMainClick {

    //TODO: введи вторую ветвь для работы с БД

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("ActMain", "onResume");

        DbRoom db = Room.databaseBuilder(this, DbRoom.class, "HandyNotes")
                .allowMainThreadQueries()
                .build();

        managerRoll = db.daoRoll().getManagerRoll();
        managerStatus = db.daoNote().getManagerStatus(this);

        db.close();
    }

    public ManagerRoll managerRoll;
    public ManagerStatus managerStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_main);
        Log.i("ActMain", "onCreate");

        setupViewPager();
        setupNavMenu();
    }

    private ViewPager viewPager;
    private BottomNavigationView bottomNavigationView;

    public FrgRank frgRank;
    public FrgNote frgNote;
    public FrgBin frgBin;

    private void setupViewPager() {
        Log.i("ActMain", "setupViewPager");

        viewPager = findViewById(R.id.actMain_vp);
        bottomNavigationView = findViewById(R.id.actMain_bnv_menu);

        FragmentManager fragmentManager = getSupportFragmentManager();
        AdapterPager adapterPager = new AdapterPager(fragmentManager);

        frgRank = new FrgRank();
        frgNote = new FrgNote();
        frgBin = new FrgBin();

        adapterPager.addFragment(frgRank);
        adapterPager.addFragment(frgNote);
        adapterPager.addFragment(frgBin);

        viewPager.setAdapter(adapterPager);
        viewPager.setOffscreenPageLimit(adapterPager.getCount() - 1);
    }

    private MenuMain menuMain;

    private void setupNavMenu() {
        Log.i("ActMain", "setupNavMenu");

        menuMain = new MenuMain(viewPager, bottomNavigationView);
        menuMain.setMenuMainClick(this);

        viewPager.addOnPageChangeListener(menuMain);
        bottomNavigationView.setOnNavigationItemSelectedListener(menuMain);

        menuMain.setPage(MenuMain.pageNotes);
    }

    @Override
    public void onMenuNoteClick() {
        Log.i("ActMain", "onMenuNoteClick");

        String[] itemAddOpt = getResources().getStringArray(R.array.dialog_menu_add);
        AlertDialog.Builder alert = new AlertDialog.Builder(this, R.style.AppTheme_AlertDialog);
        alert.setTitle(getString(R.string.dialog_title_add_note))
                .setItems(itemAddOpt, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        Intent intent = new Intent(ActMain.this, ActNote.class);

                        intent.putExtra(StateNote.KEY_CREATE, true);
                        intent.putExtra(DbDesc.NT_TP, item == DbDesc.typeText ? DbDesc.typeText : DbDesc.typeRoll);
                        intent.putExtra(DbDesc.RK_VS, frgRank.managerRank.getVisible());

                        startActivity(intent);
                    }
                }).setCancelable(true);
        AlertDialog dialog = alert.create();
        dialog.show();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (menuMain.getPageCurrent() == MenuMain.pageRank && ev.getAction() == MotionEvent.ACTION_DOWN) {
            Log.i("ActMain", "dispatchTouchEvent");

            View view = getCurrentFocus();
            if (view != null) {
                View viewTmp = getCurrentFocus();
                View viewNew = viewTmp != null ? viewTmp : view;

                if (viewNew.equals(view)) {
                    Rect rect = new Rect();
                    int[] coordinate = new int[2];

                    view.getLocationOnScreen(coordinate);
                    rect.set(coordinate[0], coordinate[1], coordinate[0] + view.getWidth(), coordinate[1] + view.getHeight());

                    final int x = (int) ev.getX();
                    final int y = (int) ev.getY();

                    if (rect.contains(x, y)) return super.dispatchTouchEvent(ev);
                }

                Help.hideKeyboard(this, viewNew);
                viewNew.clearFocus();
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void onBackPressed() {
        Log.i("ActMain", "onBackPressed");

        int buttonCurrent = menuMain.getPageCurrent();

        if (buttonCurrent != MenuMain.pageNotes) {
            switch (buttonCurrent) {
                case MenuMain.pageRank:
                    if (frgRank.managerRank.needClearEnter()) frgRank.managerRank.clearEnter();
                    else menuMain.setPage(MenuMain.pageNotes);
                    break;
                default:
                    menuMain.setPage(MenuMain.pageNotes);
                    break;
            }
        } else super.onBackPressed();
    }

}