package sgtmelon.scriptum.app.control;

import android.content.Context;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.os.Build;
import android.view.Window;

import androidx.annotation.RequiresApi;
import sgtmelon.iconanim.office.hdlr.HdlrAnim;
import sgtmelon.scriptum.R;
import sgtmelon.scriptum.office.Help;

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
public final class ControlMenu extends ControlMenuPreL {

    private HdlrAnim hdlrAnim;

    public ControlMenu(Context context, Window window) {
        super(context, window);
    }

    @Override
    public void setupDrawable() {
        super.setupDrawable();

        AnimatedVectorDrawable cancelOnAnim = (AnimatedVectorDrawable) Help.Draw.get(
                context, R.drawable.ic_cancel_on_anim, R.attr.clIcon
        );
        AnimatedVectorDrawable cancelOffAnim = (AnimatedVectorDrawable) Help.Draw.get(
                context, R.drawable.ic_cancel_off_anim, R.attr.clIcon
        );

        hdlrAnim = new HdlrAnim(context, cancelOnAnim, cancelOffAnim);
        hdlrAnim.setAnimation(this);
    }

    @Override
    public void setDrawable(boolean drawableOn, boolean needAnim) {
        if (!needAnim) {
            if (drawableOn) toolbar.setNavigationIcon(cancelOn);
            else toolbar.setNavigationIcon(cancelOff);
        } else {
            hdlrAnim.setAnimState(drawableOn);
            if (drawableOn) {
                toolbar.setNavigationIcon(hdlrAnim.getAnimOn());
                hdlrAnim.startAnimOn();
            } else {
                toolbar.setNavigationIcon(hdlrAnim.getAnimOff());
                hdlrAnim.startAnimOff();
            }
            hdlrAnim.waitAnimationEnd();
        }
    }

}
