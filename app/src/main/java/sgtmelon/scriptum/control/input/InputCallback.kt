package sgtmelon.scriptum.control.input

import sgtmelon.scriptum.model.item.InputItem

/**
 * Интерфейс для общения с [InputControl]
 */
interface InputCallback {

    var isEnabled: Boolean

    fun makeNotEnabled(func: () -> Unit)

    fun onRankChange(valueFrom: List<Long>, valueTo: List<Long>)

    fun onColorChange(valueFrom: Int, valueTo: Int)

    fun onNameChange(valueFrom: String, valueTo: String, cursor: InputItem.Cursor)

    fun onTextChange(valueFrom: String, valueTo: String, cursor: InputItem.Cursor)

    fun onRollChange(p: Int, valueFrom: String, valueTo: String, cursor: InputItem.Cursor)

    fun onRollAdd(p: Int, valueTo: String)

    fun onRollRemove(p: Int, valueFrom: String)

    fun onRollMove(valueFrom: Int, valueTo: Int)

}