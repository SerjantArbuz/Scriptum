package sgtmelon.scriptum.control.menu

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import android.view.View
import android.view.Window
import androidx.appcompat.widget.Toolbar
import sgtmelon.iconanim.IconAnimControl
import sgtmelon.scriptum.R
import sgtmelon.scriptum.office.annot.def.ColorDef
import sgtmelon.scriptum.office.annot.def.ThemeDef
import sgtmelon.scriptum.office.utils.ColorUtils
import sgtmelon.scriptum.office.utils.ColorUtils.blend
import sgtmelon.scriptum.office.utils.ColorUtils.getDrawable
import sgtmelon.scriptum.office.utils.Preference

/**
 * Класс для контроля меню | Для версий API < 21
 */
open class MenuControl(protected val context: Context,
                       private val window: Window,
                       protected val toolbar: Toolbar?,
                       private val indicator: View?
) : IconAnimControl.Callback {

    private val statusOnDark = Build.VERSION.SDK_INT < Build.VERSION_CODES.M

    protected val anim: ValueAnimator = ValueAnimator.ofFloat(0F, 1F)

    protected val cancelOn: Drawable? = context.getDrawable(R.drawable.ic_cancel_enter, R.attr.clContent)
    protected val cancelOff: Drawable? = context.getDrawable(R.drawable.ic_cancel_exit, R.attr.clContent)

    private val valTheme: Int = Preference(context).theme

    private var statusColorFrom: Int = 0
    private var statusColorTo: Int = 0
    private var toolbarColorFrom: Int = 0
    private var toolbarColorTo: Int = 0
    private var indicatorColorFrom: Int = 0
    private var indicatorColorTo: Int = 0

    init {
        val updateListener = ValueAnimator.AnimatorUpdateListener {
            val position = it.animatedFraction
            var blended: Int

            if (valTheme != ThemeDef.dark && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                blended = statusColorFrom.blend(statusColorTo, position)
                window.statusBarColor = blended
            }

            blended = toolbarColorFrom.blend(toolbarColorTo, position)
            if (valTheme != ThemeDef.dark) toolbar?.setBackgroundColor(blended)

            blended = indicatorColorFrom.blend(indicatorColorTo, position)
            if (valTheme == ThemeDef.dark) indicator?.setBackgroundColor(blended)
        }

        anim.addUpdateListener(updateListener)
        anim.duration = context.resources.getInteger(android.R.integer.config_shortAnimTime).toLong()
    }

    /**
     * Установка цвета для UI
     *
     * @param color - Начальный цвет
     */
    fun setColor(@ColorDef color: Int) {
        if (valTheme != ThemeDef.dark) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.statusBarColor = ColorUtils.get(context, color, statusOnDark)
            }
            toolbar?.setBackgroundColor(ColorUtils.get(context, color, false))
        }

        indicator?.setBackgroundColor(ColorUtils.get(context, color, true))

        setColorFrom(color)
    }

    /**
     * Установка начального цвета, [color] - начальный цвет
     */
    fun setColorFrom(@ColorDef color: Int) {
        statusColorFrom = ColorUtils.get(context, color, statusOnDark)
        toolbarColorFrom = ColorUtils.get(context, color, needDark = false)
        indicatorColorFrom = ColorUtils.get(context, color, needDark = true)
    }

    /**
     * Покраска UI элементов с анимацией, [color] - конечный цвет
     */
    fun startTint(@ColorDef color: Int) {
        statusColorTo = ColorUtils.get(context, color, statusOnDark)
        toolbarColorTo = ColorUtils.get(context, color, needDark = false)
        indicatorColorTo = ColorUtils.get(context, color, needDark = true)

        if (statusColorFrom != statusColorTo
                || toolbarColorFrom != toolbarColorTo
                || indicatorColorFrom != indicatorColorTo) anim.start()
    }

    override fun setDrawable(drawableOn: Boolean, needAnim: Boolean) {
        toolbar?.navigationIcon = if (drawableOn) cancelOn else cancelOff
    }

}