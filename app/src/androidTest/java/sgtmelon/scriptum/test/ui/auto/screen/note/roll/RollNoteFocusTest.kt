package sgtmelon.scriptum.test.ui.auto.screen.note.roll

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.extension.nextString
import sgtmelon.scriptum.presentation.screen.ui.impl.note.RollNoteFragment
import sgtmelon.scriptum.test.parent.ParentUiTest

/**
 * Test enter focus for [RollNoteFragment].
 */
@RunWith(AndroidJUnit4::class)
class RollNoteFocusTest : ParentUiTest() {

    @Test fun focusOnCreate() = data.createRoll().let {
        launch {
            mainScreen {
                notesScreen(isEmpty = true) {
                    openAddDialog { createRoll(it) { toolbar { assertFocus() } } }
                }
            }
        }
    }

    @Test fun focusOnEdit() = data.insertRoll().let {
        launch {
            mainScreen {
                notesScreen {
                    openRollNote(it) {
                        controlPanel { onEdit() }
                        enterPanel { onEnterText(nextString()) }

                        controlPanel { onSave().onEdit() }
                        enterPanel { assertFocus() }
                    }
                }
            }
        }
    }

}