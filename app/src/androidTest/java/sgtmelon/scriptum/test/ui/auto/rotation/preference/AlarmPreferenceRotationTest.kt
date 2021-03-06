package sgtmelon.scriptum.test.ui.auto.rotation.preference

import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.domain.model.annotation.Repeat
import sgtmelon.scriptum.domain.model.item.MelodyItem
import sgtmelon.scriptum.presentation.screen.ui.impl.preference.AlarmPreferenceFragment
import sgtmelon.scriptum.test.ui.auto.screen.preference.alarm.IAlarmPreferenceTest
import sgtmelon.scriptum.test.parent.ParentRotationTest
import sgtmelon.scriptum.ui.dialog.preference.VolumeDialogUi
import kotlin.random.Random

/**
 * Test of [AlarmPreferenceFragment] work with phone rotation.
 */
@RunWith(AndroidJUnit4::class)
class AlarmPreferenceRotationTest : ParentRotationTest(), IAlarmPreferenceTest {

    @Test fun content() = runTest({
        preferenceRepo.repeat = Repeat.list.random()

        val signalArray = getLogic().getRandomSignal()
        getLogic().alarmInteractor.updateSignal(signalArray)

        val melodyList = runBlocking { getLogic().signalInteractor.getMelodyList() }
        preferenceRepo.melodyUri = melodyList.random().uri

        preferenceRepo.volume = VolumeDialogUi.random()
        preferenceRepo.volumeIncrease = Random.nextBoolean()
    }) {
        automator.rotateSide()
        assert()
    }

    @Test fun repeatDialog() {
        val initValue = Repeat.list.random()
        val value = getRepeatClick(initValue)

        assertNotEquals(initValue, value)

        runTest({ preferenceRepo.repeat = initValue }) {
            openRepeatDialog {
                onClickItem(value)
                automator.rotateSide()
                assert()
                onClickApply()
            }
            assert()
        }

        assertEquals(value, preferenceRepo.repeat)
    }

    @Repeat private fun getRepeatClick(@Repeat initValue: Int): Int {
        val newValue = Repeat.list.random()
        return if (newValue == initValue) getRepeatClick(initValue) else newValue
    }

    @Test fun signalDialog() {
        val initValue = getLogic().getRandomSignal()
        val value = getSignalClick(initValue)

        assertFalse(initValue.contentEquals(value))
        assertEquals(initValue.size, value.size)

        runTest({ getLogic().alarmInteractor.updateSignal(initValue) }) {
            openSignalDialog {
                onClickItem(value)
                automator.rotateSide()
                assert()
                onClickApply()
            }
            assert()
        }

        assertTrue(getLogic().signalInteractor.typeCheck.contentEquals(value))
    }

    private fun getSignalClick(initArray: BooleanArray): BooleanArray {
        val newArray = getLogic().getRandomSignal()
        return if (initArray.contentEquals(newArray)) getSignalClick(initArray) else newArray
    }

    @Test fun melodyDialog() {
        val list = runBlocking { getLogic().signalInteractor.getMelodyList() }
        val initValue = list.random()
        val value = getMelodyClick(list, initValue)

        assertNotEquals(initValue, value)

        runTest({
            getLogic().alarmInteractor.updateSignal(booleanArrayOf(true, Random.nextBoolean()))
            preferenceRepo.melodyUri = initValue.uri
        }) {
            openMelodyDialog {
                onClickItem(list.indexOf(value))
                automator.rotateSide()
            }
            assert()
        }

        assertEquals(initValue.uri, preferenceRepo.melodyUri)
    }

    private fun getMelodyClick(list: List<MelodyItem>, initValue: MelodyItem): MelodyItem {
        val newValue = list.random()
        return if (newValue == initValue) getMelodyClick(list, initValue) else newValue
    }

    @Test fun volumeDialog() {
        val initValue = VolumeDialogUi.random()
        val value = getVolumeClick(initValue)

        assertNotEquals(initValue, value)

        runTest({
            getLogic().alarmInteractor.updateSignal(booleanArrayOf(true, Random.nextBoolean()))
            preferenceRepo.volume = initValue
        }) {
            openVolumeDialog {
                seekTo(value)
                automator.rotateSide()
                assert()
                onClickApply()
            }
            assert()
        }

        assertEquals(value, preferenceRepo.volume)
    }

    private fun getVolumeClick(initValue: Int): Int {
        val newValue = VolumeDialogUi.random()
        return if (newValue == initValue) getVolumeClick(initValue) else newValue
    }
}