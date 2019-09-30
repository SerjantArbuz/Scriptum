package sgtmelon.scriptum.test.control

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.basic.extension.waitAfter
import sgtmelon.scriptum.basic.extension.waitBefore
import sgtmelon.scriptum.test.ParentUiTest

/**
 * Test of animation info about empty list
 */
@RunWith(AndroidJUnit4::class)
class InfoAnimTest : ParentUiTest() {

    @Test fun rankShowAndHide() = data.rankEntity.let {
        launch {
            mainScreen {
                openRankPage(empty = true) {
                    repeat(times = 3) { _ ->
                        waitAfter(TIME) {
                            toolbar { onEnterName(it.name, isAddEnabled = true).onClickAdd() }
                        }

                        waitBefore(TIME) { onClickCancel() }
                    }
                }
            }
        }
    }


    @Test fun notesShow() = data.insertText().let {
        launch {
            mainScreen {
                waitAfter(TIME) { openNotesPage { openNoteDialog(it) { onClickDelete() } } }
            }
        }
    }

    @Test fun notesHide() = data.insertTextToBin().let {
        launch {
            mainScreen {
                openBinPage { openNoteDialog(it) { onClickRestore() } }
                waitAfter(TIME) { openNotesPage() }
            }
        }
    }


    @Test fun binShow() = data.insertTextToBin().let {
        launch {
            mainScreen {
                waitAfter(TIME) { openBinPage { openNoteDialog(it) { onClickClear() } } }
            }
        }
    }

    @Test fun binHide() = data.insertText().let {
        launch {
            mainScreen {
                openNotesPage { openNoteDialog(it) { onClickDelete() } }
                waitAfter(TIME) { openBinPage() }
            }
        }
    }


    @Test fun notificationShow() = data.insertNotification().let {
        launch {
            mainScreen {
                openNotesPage { openNotification { waitAfter(TIME) { onClickCancel() } } }
            }
        }
    }


    private companion object {
        const val TIME = 500L
    }

}