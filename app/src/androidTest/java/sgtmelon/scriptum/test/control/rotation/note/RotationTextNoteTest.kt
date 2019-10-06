package sgtmelon.scriptum.test.control.rotation.note

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.screen.ui.note.TextNoteFragment
import sgtmelon.scriptum.test.ParentRotationTest

/**
 * Test of [TextNoteFragment] work with phone rotation
 */
@RunWith(AndroidJUnit4::class)
class RotationTextNoteTest : ParentRotationTest() {

    /**
     * Content
     */

    @Test fun contentOnBin() = data.insertTextToBin().let {
        launch { mainScreen { openBinPage { openTextNote(it) { onRotate { assert() } } } } }
    }

    @Test fun contentOnRestoreOpen() = data.insertTextToBin().let {
        launch {
            mainScreen {
                openBinPage {
                    openTextNote(it) {
                        controlPanel { onRestoreOpen() }
                        onRotate { assert() }
                    }
                }
            }
        }
    }

    @Test fun contentOnCreate() = data.createText().let {
        launch { mainScreen { openAddDialog { createTextNote(it) { onRotate { assert() } } } } }
    }

    @Test fun contentOnRead() = data.insertText().let {
        launch { mainScreen { openNotesPage { openTextNote(it) { onRotate { assert() } } } } }
    }

    @Test fun contentOnEdit() = data.insertText().let {
        launch {
            mainScreen {
                openNotesPage {
                    openTextNote(it) { onRotate({ controlPanel { onEdit() } }) { assert() } }
                }
            }
        }
    }

    /**
     * Dialogs
     */

    @Test fun convertDialog() = data.insertText().let {
        launch {
            mainScreen {
                openNotesPage {
                    openTextNote(it) { controlPanel { onConvert { onRotate { assert() } } } }
                }
            }
        }
    }

    @Test fun convertDialogResult() = data.insertText().let {
        launch {
            mainScreen {
                openNotesPage {
                    openTextNote(it) {
                        controlPanel {
                            onConvert { onClickYes() }
                            onRotate {
                                // TODO #TEST convert assert
                            }
                        }
                    }
                }
            }
        }
    }

    @Test fun colorDialog() = data.createText().let {
        launch {
            mainScreen {
                openAddDialog {
                    createTextNote(it) {
                        controlPanel {
                            onColor {
                                onClickItem()
                                onRotate { assert() }
                            }
                        }
                    }
                }
            }
        }
    }

}