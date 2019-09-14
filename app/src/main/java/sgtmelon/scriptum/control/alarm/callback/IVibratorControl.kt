package sgtmelon.scriptum.control.alarm.callback

import sgtmelon.scriptum.control.alarm.VibratorControl

/**
 * Interface for communication with [VibratorControl]
 */
interface IVibratorControl {

    fun start(pattern: LongArray)

    fun cancel()

}