package sgtmelon.scriptum.screen.vm.notification

import io.mockk.impl.annotations.MockK
import io.mockk.verifySequence
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.*
import org.junit.Test
import sgtmelon.scriptum.ParentViewModelTest
import sgtmelon.scriptum.R
import sgtmelon.scriptum.TestData
import sgtmelon.scriptum.interactor.callback.IBindInteractor
import sgtmelon.scriptum.interactor.callback.notification.IAlarmInteractor
import sgtmelon.scriptum.interactor.callback.notification.ISignalInteractor
import sgtmelon.scriptum.model.annotation.Repeat
import sgtmelon.scriptum.screen.ui.callback.notification.IAlarmActivity
import kotlin.random.Random


/**
 * Test for [AlarmViewModel].
 */
@ExperimentalCoroutinesApi
class AlarmViewModelTest : ParentViewModelTest() {

    private val data = TestData.Note

    @MockK lateinit var callback: IAlarmActivity

    @MockK lateinit var interactor: IAlarmInteractor
    @MockK lateinit var signalInteractor: ISignalInteractor
    @MockK lateinit var bindInteractor: IBindInteractor

    private val viewModel by lazy { AlarmViewModel(application) }

    override fun setUp() {
        super.setUp()

        viewModel.setCallback(callback)
        viewModel.setInteractor(interactor, signalInteractor, bindInteractor)
    }

    @Test override fun onDestroy() {
        assertNotNull(viewModel.callback)
//        viewModel.onDestroy()
//        assertNull(viewModel.callback)
    }


    @Test fun onSetup() {
        TODO()
    }

    @Test fun onSaveData() {
        TODO()
    }

    @Test fun onStart() {
        TODO()
    }

    @Test fun onClickNote() {
        TODO()
    }

    @Test fun onClickDisable() {
        viewModel.onClickDisable()
        verifySequence { callback.finish() }
    }

    @Test fun onClickRepeat() {
        TODO()
    }

    @Test fun onResultRepeatDialog() {
        TODO()
    }

    @Test fun getRepeatById() {
        assertEquals(Repeat.MIN_10, viewModel.getRepeatById(R.id.item_repeat_0))
        assertEquals(Repeat.MIN_30, viewModel.getRepeatById(R.id.item_repeat_1))
        assertEquals(Repeat.MIN_60, viewModel.getRepeatById(R.id.item_repeat_2))
        assertEquals(Repeat.MIN_180, viewModel.getRepeatById(R.id.item_repeat_3))
        assertEquals(Repeat.MIN_1440, viewModel.getRepeatById(R.id.item_repeat_4))
        assertNull(viewModel.getRepeatById(itemId = -1))
    }

    @Test fun onReceiveUnbindNote() {
        TODO()
    }

}