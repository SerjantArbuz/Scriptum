package sgtmelon.scriptum.app.control;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import sgtmelon.iconanim.office.intf.AnimIntf;
import sgtmelon.scriptum.R;
import sgtmelon.scriptum.app.database.RoomDb;
import sgtmelon.scriptum.office.Help;
import sgtmelon.scriptum.office.annot.def.ThemeDef;
import sgtmelon.scriptum.office.annot.def.db.TypeDef;
import sgtmelon.scriptum.office.intf.MenuIntf;

/**
 * Класс для контроля меню
 * Для версий API < 21
 */
public class MenuControl implements Toolbar.OnMenuItemClickListener, AnimIntf {

    protected final Context context;
    private final Window window;

    protected Toolbar toolbar;
    Drawable cancelOn, cancelOff;

    private Menu menu;
    private View indicator;

    private int type;
    private int valTheme;

    private int statusStartColor, statusEndColor;
    private int toolbarStartColor, toolbarEndColor;

    private MenuItem mItemStatus, mItemCheck;

    private MenuIntf.Note.DeleteMenuClick deleteMenuClick;
    private MenuIntf.Note.NoteMenuClick noteMenuClick;

    public MenuControl(Context context, Window window) {
        this.context = context;
        this.window = window;

        valTheme = Help.Pref.getTheme(context);
    }

    public void setToolbar(Toolbar toolbar) {
        this.toolbar = toolbar;
        menu = toolbar.getMenu();
    }

    public void setIndicator(View indicator) {
        this.indicator = indicator;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setDeleteMenuClick(MenuIntf.Note.DeleteMenuClick deleteMenuClick) {
        this.deleteMenuClick = deleteMenuClick;
    }

    public void setNoteMenuClick(MenuIntf.Note.NoteMenuClick noteMenuClick) {
        this.noteMenuClick = noteMenuClick;
    }

    //Установка цвета
    public void setColor(int color) {
        if (valTheme != ThemeDef.dark) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.setStatusBarColor(Help.Clr.get(context, color, true));
            }
            toolbar.setBackgroundColor(Help.Clr.get(context, color, false));
        }
        indicator.setBackgroundColor(Help.Clr.get(context, color, true));

        setStartColor(color);
    }

    //Меняем начальный цвет
    public void setStartColor(int color) {
        statusStartColor = Help.Clr.get(context, color, true);
        toolbarStartColor = Help.Clr.get(context, color, false);
    }

    //Покраска с анимацией
    public void startTint(int color) {
        statusEndColor = Help.Clr.get(context, color, true);
        toolbarEndColor = Help.Clr.get(context, color, false);

        if (statusStartColor != statusEndColor && toolbarStartColor != toolbarEndColor) {
            ValueAnimator anim = ValueAnimator.ofFloat(0, 1);

            anim.addUpdateListener(animation -> {
                float position = animation.getAnimatedFraction();

                int blended = Help.Clr.blend(statusStartColor, statusEndColor, position);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && valTheme != ThemeDef.dark) {
                    window.setStatusBarColor(blended);
                }

                ColorDrawable background = new ColorDrawable(blended);
                indicator.setBackground(background);

                blended = Help.Clr.blend(toolbarStartColor, toolbarEndColor, position);
                background = new ColorDrawable(blended);
                if (valTheme != ThemeDef.dark) toolbar.setBackground(background);

            });

            anim.setDuration(context.getResources().getInteger(android.R.integer.config_shortAnimTime))
                    .start();
        }
    }

    public void setupDrawable() {
        cancelOn = Help.Draw.get(context, R.drawable.ic_cancel_on, R.attr.clIcon);
        cancelOff = Help.Draw.get(context, R.drawable.ic_cancel_off, R.attr.clIcon);
    }

    @Override
    public void setDrawable(boolean drawableOn, boolean needAnim) {
        if (drawableOn) toolbar.setNavigationIcon(cancelOn);
        else toolbar.setNavigationIcon(cancelOff);
    }

    public void setupMenu(boolean status) {
        MenuItem mItemRestore = menu.findItem(R.id.restore_item);
        MenuItem mItemRestoreOpen = menu.findItem(R.id.restore_open_item);
        MenuItem mItemClear = menu.findItem(R.id.clear_item);

        MenuItem mItemMoreR = menu.findItem(R.id.read_more_item);
        mItemStatus = menu.findItem(R.id.bind_item);
        MenuItem mItemConvert = menu.findItem(R.id.convert_item);
        mItemCheck = menu.findItem(R.id.check_item);
        MenuItem mItemDelete = menu.findItem(R.id.delete_item);

        MenuItem mItemMoreE = menu.findItem(R.id.edit_more_item);
        MenuItem mItemRank = menu.findItem(R.id.rank_item);
        MenuItem mItemColor = menu.findItem(R.id.color_item);

        setStatusTitle(status);

        boolean isRoll = type == TypeDef.roll;

        mItemStatus.setIcon(Help.Draw.get(context, isRoll
                        ? R.drawable.ic_bind_roll
                        : R.drawable.ic_bind_text,
                R.attr.clIcon));

        mItemConvert.setTitle(isRoll
                ? R.string.menu_note_convert_to_text
                : R.string.menu_note_convert_to_roll);
        mItemCheck.setVisible(isRoll);

        MenuItem[] mItems = new MenuItem[]{mItemRestore, mItemRestoreOpen, mItemClear,
                mItemMoreR, mItemStatus, mItemConvert, mItemCheck, mItemDelete,
                mItemMoreE, mItemRank, mItemColor};

        for (MenuItem mItem : mItems) Help.Tint.menuIcon(context, mItem);

        RoomDb db = RoomDb.provideDb(context);
        mItemRank.setVisible(db.daoRank().getCount() != 0);
        db.close();
    }

    public void setMenuGroupVisible(boolean grDelete, boolean grEdit, boolean grRead) {
        menu.setGroupVisible(R.id.delete_group, grDelete);
        menu.setGroupVisible(R.id.edit_group, grEdit);
        menu.setGroupVisible(R.id.read_group, grRead);
    }

    public void setCheckTitle(boolean isAllCheck) {
        mItemCheck.setTitle(isAllCheck
                ? context.getString(R.string.menu_note_check_zero)
                : context.getString(R.string.menu_note_check_all)
        );
    }

    public void setStatusTitle(boolean status) {
        mItemStatus.setTitle(status
                ? context.getString(R.string.menu_note_status_unbind)
                : context.getString(R.string.menu_note_status_bind)
        );
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.restore_item:
                deleteMenuClick.onMenuRestoreClick();
                return true;
            case R.id.restore_open_item:
                deleteMenuClick.onMenuRestoreOpenClick();
                return true;
            case R.id.clear_item:
                deleteMenuClick.onMenuClearClick();
                return true;
            case R.id.save_item:
                if (!noteMenuClick.onMenuSaveClick(true)) {
                    Toast.makeText(context, R.string.toast_note_save_warning, Toast.LENGTH_SHORT).show();
                }
                return true;
            case R.id.rank_item:
                noteMenuClick.onMenuRankClick();
                return true;
            case R.id.color_item:
                noteMenuClick.onMenuColorClick();
                return true;
            case R.id.edit_item:
                noteMenuClick.onMenuEditClick(true);
                return true;
            case R.id.check_item:
                noteMenuClick.onMenuCheckClick();
                return true;
            case R.id.bind_item:
                noteMenuClick.onMenuBindClick();
                break;
            case R.id.convert_item:
                noteMenuClick.onMenuConvertClick();
                return true;
            case R.id.delete_item:
                deleteMenuClick.onMenuDeleteClick();
                return true;
        }
        return false;
    }

}
