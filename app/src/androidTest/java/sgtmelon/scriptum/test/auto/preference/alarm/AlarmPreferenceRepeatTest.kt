package sgtmelon.scriptum.test.auto.preference.alarm

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.domain.model.annotation.Repeat
import sgtmelon.scriptum.presentation.screen.ui.impl.preference.AlarmPreferenceFragment
import sgtmelon.scriptum.test.parent.ParentUiTest
import sgtmelon.scriptum.ui.dialog.preference.RepeatDialogUi

/**
 * Test for [AlarmPreferenceFragment] and [RepeatDialogUi].
 */
@RunWith(AndroidJUnit4::class)
class AlarmPreferenceRepeatTest : ParentUiTest() {

    // TODO fix all

    @Test fun dialogClose() = launch {
        mainScreen {
            notesScreen(isEmpty = true) {
                openPreference {
                    TODO()
                    //                    openRepeatDialog { onCloseSoft() }.assert()
                    //                    assert()
                    //                    openRepeatDialog { onClickCancel() }.assert()
                    //                    assert()
                }
            }
        }
    }


    @Test fun selectRepeatMin10() = startSelectRepeat(Repeat.MIN_10)

    @Test fun selectRepeatMin30() = startSelectRepeat(Repeat.MIN_30)

    @Test fun selectRepeatMin60() = startSelectRepeat(Repeat.MIN_60)

    @Test fun selectRepeatMin180() = startSelectRepeat(Repeat.MIN_180)

    @Test fun selectRepeatMin1440() = startSelectRepeat(Repeat.MIN_1440)

    private fun startSelectRepeat(@Repeat repeat: Int) = launch({ turnRepeat(repeat) }) {
        mainScreen {
            notesScreen(isEmpty = true) {
                openPreference {
                    TODO()
                    //                    openRepeatDialog { onClickItem(repeat).onClickApply() }
                }
            }
        }
    }

    /**
     * Switch [Repeat] to another one.
     */
    private fun turnRepeat(@Repeat repeat: Int) {
        val list = Repeat.list

        while (preferenceRepo.repeat == repeat) {
            preferenceRepo.repeat = list.random()
        }
    }

}