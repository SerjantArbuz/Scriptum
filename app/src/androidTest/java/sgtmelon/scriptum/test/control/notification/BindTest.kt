package sgtmelon.scriptum.test.control.notification

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.model.NoteModel
import sgtmelon.scriptum.test.ParentNotificationTest
import kotlin.random.Random

/**
 * Test of note bind in status bar
 */
@RunWith(AndroidJUnit4::class)
class BindTest : ParentNotificationTest() {

    /**
     * Notify on start is implied
     */

    @Test fun notesUnbindReceiver() = with(data) {
        insertText(textNote.copy(isStatus = true))
    }.let {
        launch {
            mainScreen {
                notesScreen {
                    onOpen { onAssertItem(it.apply { noteEntity.isStatus = false }) }
                }
            }
        }
    }

    @Test fun textNoteUnbindReceiver() = with(data) {
        insertText(textNote.copy(isStatus = true))
    }.let {
        launch {
            mainScreen {
                notesScreen {
                    openTextNote(it) {
                        onOpen { apply { it.noteEntity.isStatus = false }.fullAssert() }
                    }
                }
            }
        }
    }

    @Test fun rollNoteUnbindReceiver() = with(data) {
        insertRoll(rollNote.copy(isStatus = true))
    }.let {
        launch {
            mainScreen {
                notesScreen {
                    openRollNote(it) {
                        onOpen { apply { it.noteEntity.isStatus = false }.fullAssert() }
                    }
                }
            }
        }
    }


    @Test fun notesTextBindUnbind() = startNotesBindUnbindTest(data.insertText())

    @Test fun notesRollBindUnbind() = startNotesBindUnbindTest(data.insertRoll())

    private fun startNotesBindUnbindTest(model: NoteModel) = launch {
        mainScreen {
            notesScreen {
                apply { onSee() }.openNoteDialog(model) { onBind() }
                apply { onSee() }.openNoteDialog(model) { onBind() }
            }
        }
    }


    @Test fun notesTextUpdateOnConvert() = startNotesUpdateOnConvertText(with(data) {
        insertText(textNote.copy(isStatus = true))
    })

    @Test fun notesRollUpdateOnConvert() = startNotesUpdateOnConvertText(with(data) {
        insertRoll(rollNote.copy(isStatus = true))
    })

    private fun startNotesUpdateOnConvertText(model: NoteModel) = launch {
        mainScreen {
            notesScreen {
                apply { onSee() }.openNoteDialog(model) { onConvert() }
                apply { onSee() }.openNoteDialog(model) { onDelete() }
            }
        }
    }


    @Test fun notesTextUnbindOnDelete() = startNotesUnbindOnDeleteTest(with(data) {
        insertText(textNote.copy(isStatus = true))
    })

    @Test fun notesRollUnbindOnDelete() = startNotesUnbindOnDeleteTest(with(data) {
        insertRoll(rollNote.copy(isStatus = true))
    })

    private fun startNotesUnbindOnDeleteTest(model: NoteModel) = launch {
        mainScreen {
            notesScreen { openNoteDialog(model) { onSee { onDelete() } } }
            binScreen { onSee { onAssertItem(0, model) } }
        }
    }


    @Test fun rankHideShowOnClick() = insertRankWithStatusNote().let {
        launch {
            mainScreen {
                notesScreen()
                rankScreen { onSee { onClickVisible() } }
                notesScreen(empty = true, hide = true)
                rankScreen { onSee { onClickVisible() } }
                notesScreen { openNoteDialog(it) { onSee { onBind() } } }
            }
        }
    }

    @Test fun rankHideShowOnLongClick() = insertRankWithStatusNote().let {
        launch {
            mainScreen {
                notesScreen()
                rankScreen { onSee { onClickVisible() } }
                notesScreen(empty = true, hide = true)
                rankScreen { onSee { onLongClickVisible() } }
                notesScreen { openNoteDialog(it) { onSee { onBind() } } }
            }
        }
    }

    @Test fun rankCancel() = insertRankWithStatusNote().let {
        launch {
            mainScreen {
                rankScreen { onSee { onClickVisible() } }
                notesScreen(empty = true, hide = true)
                rankScreen { onSee { onClickCancel() } }
                notesScreen { openNoteDialog(it) { onSee { onBind() } } }
            }
        }
    }

    private fun insertRankWithStatusNote(): NoteModel {
        val noteModel = with(data) {
            return@with if (Random.nextBoolean()) {
                insertText(textNote.copy(isStatus = true))
            } else {
                insertRoll(rollNote.copy(isStatus = true))
            }
        }

        val rankEntity = with(data) {
            insertRank(rankEntity.copy(noteId = arrayListOf(noteModel.noteEntity.id)))
        }

        data.inRoom {
            iNoteDao.update(noteModel.noteEntity.apply {
                rankId = rankEntity.id
                rankPs = rankEntity.position
            })
        }

        return noteModel
    }


    @Test fun textNoteBindUnbind() = data.insertText().let {
        launch {
            mainScreen {
                notesScreen {
                    openTextNote(it) {
                        controlPanel {
                            onBind().apply { onSee() }.onBind().apply { onSee() }
                        }
                    }
                }
            }
        }
    }

    @Test fun rollNoteBindUnbind() = data.insertRoll().let {
        launch {
            mainScreen {
                notesScreen {
                    openRollNote(it) {
                        controlPanel {
                            onBind().apply { onSee() }.onBind().apply { onSee() }
                        }
                    }
                }
            }
        }
    }

    @Test fun textNoteUpdateOnConvert() = with(data) {
        insertText(textNote.copy(isStatus = true))
    }.let {
        launch {
            mainScreen {
                notesScreen {
                    openTextNote(it) {
                        controlPanel {
                            onConvert { onSee { onClickYes() } }
                            onSee { onBind() }
                        }
                    }
                }
            }
        }
    }

    @Test fun rollNoteUpdateOnConvert() = with(data) {
        insertRoll(rollNote.copy(isStatus = true))
    }.let {
        launch {
            mainScreen {
                notesScreen {
                    openRollNote(it) {
                        controlPanel {
                            onConvert { onSee { onClickYes() } }
                            onSee { onBind() }
                        }
                    }
                }
            }
        }
    }

    @Test fun textNoteUnbindOnDelete() = with(data) {
        insertText(textNote.copy(isStatus = true))
    }.let {
        launch {
            mainScreen {
                notesScreen {
                    openTextNote(it) { controlPanel { onDelete() } }
                    onSee { assert(empty = true) }
                }
            }
        }
    }

    @Test fun rollNoteUnbindOnDelete() = with(data) {
        insertRoll(rollNote.copy(isStatus = true))
    }.let {
        launch {
            mainScreen {
                notesScreen {
                    openRollNote(it) { controlPanel { onDelete() } }
                    onSee { assert(empty = true) }
                }
            }
        }
    }

}