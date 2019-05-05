package sgtmelon.scriptum.test.note

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.data.State
import sgtmelon.scriptum.model.key.NoteType
import sgtmelon.scriptum.screen.view.note.RollNoteFragment
import sgtmelon.scriptum.test.ParentTest
import sgtmelon.scriptum.ui.screen.main.MainScreen

/**
 * Тест для [RollNoteFragment]
 *
 * @author SerjantArbuz
 */
@RunWith(AndroidJUnit4::class)
class RollNoteTest : ParentTest() {

    override fun setUp() {
        super.setUp()

        preference.firstStart = false
        testData.clearAllData()
    }

    @Test fun toolbarNoteCreate() = afterLaunch {
        MainScreen {
            addDialogUi { onClickItem(NoteType.ROLL) }
            rollNoteScreen { toolbar { assert { onDisplayState(State.NEW, name = "") } } }
        }
    }

    @Test fun toolbarNoteWithoutName() {
        val noteItem = testData.insertRoll(testData.rollNote.apply { name = "" })

        afterLaunch {
            MainScreen {
                notesScreen {
                    rollNoteScreen(State.READ) {
                        toolbar { assert { onDisplayState(State.READ, noteItem.name) } }
                        controlPanel { onClickEdit() }
                        toolbar { assert { onDisplayState(State.EDIT, noteItem.name) } }
                    }
                }
            }
        }
    }

    @Test fun toolbarNoteFromBinWithoutName() {
        val noteItem = testData.insertRollToBin(testData.rollNote.apply { name = "" })

        afterLaunch {
            MainScreen {
                binScreen {
                    rollNoteScreen(State.BIN) {
                        toolbar { assert { onDisplayState(State.BIN, noteItem.name) } }
                    }
                }
            }
        }
    }

    @Test fun toolbarNoteWithName() {
        val noteItem = testData.insertRoll()

        afterLaunch {
            MainScreen {
                notesScreen {
                    rollNoteScreen(State.READ) {
                        toolbar { assert { onDisplayState(State.READ, noteItem.name) } }
                        controlPanel { onClickEdit() }
                        toolbar { assert { onDisplayState(State.EDIT, noteItem.name) } }
                    }
                }
            }
        }
    }

    @Test fun toolbarNoteFromBinWithName() {
        val noteItem = testData.insertRollToBin()

        afterLaunch {
            MainScreen {
                binScreen {
                    rollNoteScreen(State.BIN) {
                        toolbar { assert { onDisplayState(State.BIN, noteItem.name) } }
                    }
                }
            }
        }
    }


    @Test fun toolbarNoteSaveAfterCreate() = afterLaunch {
        val noteItem = testData.rollNote
        val listRoll = testData.listRoll

        MainScreen {
            addDialogUi { onClickItem(NoteType.ROLL) }
            rollNoteScreen {
                toolbar { onEnterName(noteItem.name) }
                enterPanel { onAddRoll(listRoll[0].text) }
                controlPanel { onClickSave() }
                toolbar { assert { onDisplayState(State.READ, noteItem.name) } }
            }
        }
    }

    @Test fun toolbarNoteSaveAfterEdit() {
        val noteItem = testData.insertRoll()
        val newName = testData.uniqueString

        afterLaunch {
            MainScreen {
                notesScreen {
                    rollNoteScreen(State.READ) {
                        toolbar { assert { onDisplayState(State.READ, noteItem.name) } }
                        controlPanel { onClickEdit() }
                        toolbar { onEnterName(newName) }
                        controlPanel { onClickSave() }
                        toolbar { assert { onDisplayState(State.READ, newName) } }
                    }
                }
            }
        }
    }

}