package sgtmelon.scriptum.presentation.view

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.widget.RelativeLayout
import androidx.annotation.ColorInt
import androidx.annotation.StringDef
import sgtmelon.scriptum.R
import sgtmelon.scriptum.domain.model.annotation.Theme
import java.util.*

/**
 * ViewGroup element for create ripple animation
 */
class RippleContainer : RelativeLayout {

    private var isAnimate = false

    private val animatorList = ArrayList<Animator>()
    private val animatorSet = AnimatorSet()

    private val viewList = ArrayList<RippleView>()

    private var params: RippleParams? = null

    constructor (context: Context) : super(context)

    constructor (context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor (context: Context, attrs: AttributeSet, style: Int) : super(context, attrs, style)

    /**
     * Call func before [startAnimation]
     *
     * Element which center will be start position for ripple pass throw [hookView]
     */
    fun setupAnimation(@Theme theme: Int, @ColorInt fillColor: Int, hookView: View) = apply {
        params = RippleParams(theme, parentView = this, hookView = hookView).also {
            animatorList.apply {
                add(hookView.getAnimator(Anim.SCALE_X, NO_DELAY, it.delay, *LOGO_PULSE))
                add(hookView.getAnimator(Anim.SCALE_Y, NO_DELAY, it.delay, *LOGO_PULSE))
            }

            val paint = Paint().apply {
                isAntiAlias = true

                style = if (theme == Theme.LIGHT) Paint.Style.STROKE else Paint.Style.FILL
                strokeWidth = resources.getDimension(R.dimen.stroke_4dp)
                color = fillColor
            }

            for (i in 0 until it.count) {
                val view = RippleView(context).apply { this.paint = paint }

                addView(view, it.childParams)
                viewList.add(view)

                val delay = i * it.delay

                animatorList.apply {
                    add(view.getAnimator(Anim.SCALE_X, delay, it.duration, SCALE_FROM, it.scaleTo))
                    add(view.getAnimator(Anim.SCALE_Y, delay, it.duration, SCALE_FROM, it.scaleTo))
                    add(view.getAnimator(Anim.ALPHA, delay, it.duration, ALPHA_FROM, ALPHA_TO))
                }
            }
        }

        animatorSet.playTogether(animatorList)
    }

    fun startAnimation() {
        if (!isAnimate) {
            isAnimate = true

            viewList.forEach { it.visibility = View.VISIBLE }
            animatorSet.start()
        }
    }

    fun stopAnimation() {
        if (isAnimate) {
            isAnimate = false

            viewList.forEach { it.visibility = View.INVISIBLE }
            animatorSet.end()
        }
    }


    @StringDef(Anim.SCALE_X, Anim.SCALE_Y, Anim.ALPHA)
    private annotation class Anim {
        companion object {
            const val SCALE_X = "ScaleX"
            const val SCALE_Y = "ScaleY"
            const val ALPHA = "Alpha"
        }
    }

    @Suppress("LiftReturnOrAssignment")
    private fun View.getAnimator(@Anim animName: String, startDelay: Long, duration: Long,
                                 vararg values: Float): ObjectAnimator =
            ObjectAnimator.ofFloat(this, animName, *values).apply {
                repeatCount = ObjectAnimator.INFINITE
                repeatMode = ObjectAnimator.RESTART

                when (animName) {
                    Anim.ALPHA -> interpolator = DecelerateInterpolator()
                    else -> interpolator = AccelerateDecelerateInterpolator()
                }

                this.startDelay = startDelay
                this.duration = duration
            }

    private companion object {
        const val NO_DELAY = 0L

        const val SCALE_FROM = 1f
        const val ALPHA_FROM = 0.7f
        const val ALPHA_TO = 0f

        val LOGO_PULSE = floatArrayOf(1f, 0.95f, 1f)
    }

}