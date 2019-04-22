package sgtmelon.scriptum.model.item

import androidx.annotation.IdRes
import sgtmelon.scriptum.dialog.ColorDialog

/**
 * Модель для списка цветов в [ColorDialog], элемент списка в [ColorAdapter]
 *
 * @author SerjantArbuz
 */
class ColorItem(@IdRes val fill: Int, @IdRes val stroke: Int, @IdRes val check: Int)