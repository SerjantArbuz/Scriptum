package sgtmelon.scriptum.basic.automator

import android.app.Instrumentation
import androidx.test.uiautomator.UiDevice
import sgtmelon.scriptum.basic.extension.waitBefore
import kotlin.random.Random

class RotateAutomator(instrumentation: Instrumentation) {

    private val uiDevice = UiDevice.getInstance(instrumentation)

    fun rotateSide() {
        if (Random.nextBoolean()) {
            uiDevice.setOrientationLeft()
        } else {
            uiDevice.setOrientationRight()
        }

        waitBefore(time = 3000)
    }

    fun rotateNatural() {
        uiDevice.setOrientationNatural()
    }

}