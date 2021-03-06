package sgtmelon.scriptum.presentation.control.toolbar.tint

import sgtmelon.scriptum.domain.model.annotation.Color

/**
 * Interface for [ToolbarTintControl].
 */
interface IToolbarTintControl {

    fun setColorFrom(@Color color: Int): IToolbarTintControl

    /**
     * Set end [color] and start animation if need.
     */
    fun startTint(@Color color: Int)

}