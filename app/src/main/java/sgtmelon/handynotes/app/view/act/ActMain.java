package sgtmelon.handynotes.app.view.act;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import sgtmelon.handynotes.R;
import sgtmelon.handynotes.app.view.frg.FrgBin;
import sgtmelon.handynotes.app.view.frg.FrgNotes;
import sgtmelon.handynotes.app.view.frg.FrgRank;
import sgtmelon.handynotes.element.dialog.DlgSheetAdd;
import sgtmelon.handynotes.office.annot.def.DefDlg;
import sgtmelon.handynotes.office.annot.def.DefFrg;
import sgtmelon.handynotes.office.annot.def.DefNote;
import sgtmelon.handynotes.office.annot.def.DefPage;
import sgtmelon.handynotes.office.annot.def.db.DefType;
import sgtmelon.handynotes.office.blank.BlankAct;
import sgtmelon.handynotes.office.st.StPage;

public class ActMain extends BlankAct implements BottomNavigationView.OnNavigationItemSelectedListener, NavigationView.OnNavigationItemSelectedListener {

    //region Variable
    private static final String TAG = "ActMain";

    private FragmentManager fm;

    private StPage stPage;
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_main);
        Log.i(TAG, "onCreate");

        fm = getSupportFragmentManager();

        stPage = new StPage();

        setupNavigation(savedInstanceState != null
                ? savedInstanceState.getInt(DefPage.PAGE)
                : DefPage.notes);
    }

    private FrgRank frgRank;
    private FrgNotes frgNotes;
    private FrgBin frgBin;

    private DlgSheetAdd dlgSheetAdd;

    private void setupNavigation(@DefPage int page) {
        Log.i(TAG, "setupNavigation");

        frgRank = (FrgRank) fm.findFragmentByTag(DefFrg.RANK);
        if (frgRank == null) frgRank = new FrgRank();

        frgNotes = (FrgNotes) fm.findFragmentByTag(DefFrg.NOTES);
        if (frgNotes == null) frgNotes = new FrgNotes();

        frgBin = (FrgBin) fm.findFragmentByTag(DefFrg.BIN);
        if (frgBin == null) frgBin = new FrgBin();

        BottomNavigationView navigationView = findViewById(R.id.actMain_bnv_menu);
        navigationView.setOnNavigationItemSelectedListener(this);
        navigationView.setSelectedItemId(DefPage.itemId[page]);

        dlgSheetAdd = (DlgSheetAdd) fm.findFragmentByTag(DefDlg.SHEET_ADD);
        if (dlgSheetAdd == null) dlgSheetAdd = new DlgSheetAdd();
        dlgSheetAdd.setNavigationItemSelectedListener(menuItem -> {
            dlgSheetAdd.dismiss();

            Intent intent = new Intent(ActMain.this, ActNote.class);

            intent.putExtra(DefNote.CREATE, true);
            intent.putExtra(DefNote.TYPE, menuItem.getItemId() == R.id.menu_sheetAdd_text
                    ? DefType.text
                    : DefType.roll);

            startActivity(intent);
            return true;
        });
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        Log.i(TAG, "onNavigationItemSelected");

        boolean add = stPage.setPage(menuItem.getItemId());
        if (add) {
            if (!dlgSheetAdd.isVisible()) dlgSheetAdd.show(fm, DefDlg.SHEET_ADD);
        } else {
            FragmentTransaction transaction = fm.beginTransaction();
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);

            switch (stPage.getPage()) {
                case DefPage.rank:
                    transaction.replace(R.id.actMain_fl_container, frgRank, DefFrg.RANK);
                    break;
                case DefPage.notes:
                    transaction.replace(R.id.actMain_fl_container, frgNotes, DefFrg.NOTES);
                    break;
                case DefPage.bin:
                    transaction.replace(R.id.actMain_fl_container, frgBin, DefFrg.BIN);
                    break;
            }
            transaction.commit();
        }
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i(TAG, "onSaveInstanceState");

        outState.putInt(DefPage.PAGE, stPage.getPage());
    }

}