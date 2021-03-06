package sgtmelon.scriptum.presentation.adapter.holder

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.transition.Fade
import androidx.transition.TransitionManager
import sgtmelon.scriptum.R
import sgtmelon.scriptum.domain.model.annotation.Color
import sgtmelon.scriptum.extension.addIdlingListener
import sgtmelon.scriptum.extension.bindIndicatorColor
import sgtmelon.scriptum.extension.getCompatColor
import sgtmelon.scriptum.presentation.adapter.ColorAdapter

/**
 * Holder for app color, use in [ColorAdapter]
 */
class ColorHolder(itemView: View) : ParentHolder(itemView) {

    private val context = itemView.context

    private val parentContainer: ViewGroup = itemView.findViewById(R.id.color_parent_container)
    private val backgroundView: View = itemView.findViewById(R.id.color_background_view)

    private val checkImage: ImageView = itemView.findViewById(R.id.color_check_image)
    val clickView: View = itemView.findViewById(R.id.color_click_view)

    fun bindColor(@Color color: Int) {
        val colorItem = backgroundView.bindIndicatorColor(color)
        if (colorItem != null) {
            checkImage.setColorFilter(context.getCompatColor(colorItem.content))
        }

        val context = itemView.context ?: return
        val colorName = context.resources.getStringArray(R.array.pref_text_note_color)[color]

        clickView.contentDescription = context.getString(R.string.description_item_color, colorName)
    }

    fun checkShow() {
        checkImage.visibility = View.VISIBLE
    }

    fun checkHide() {
        checkImage.visibility = View.GONE
    }

    fun animateCheckShow() = prepareCheckTransition { checkShow() }

    fun animateCheckHide() = prepareCheckTransition { checkHide() }

    private fun prepareCheckTransition(func: () -> Unit) {
        val time = context.resources.getInteger(R.integer.color_fade_time)
        val transition = Fade().setDuration(time.toLong())
            .addTarget(checkImage)
            .addIdlingListener()

        TransitionManager.beginDelayedTransition(parentContainer, transition)

        func()
    }

}