package sgtmelon.scriptum.test.auto.note

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.screen.ui.note.TextNoteFragment
import sgtmelon.scriptum.test.ParentUiTest

/**
 * Test for [TextNoteFragment]
 */
@RunWith(AndroidJUnit4::class)
class TextNoteTest : ParentUiTest() {

    /**
     * Content
     */

    @Test fun contentOnBinWithoutName() = data.insertTextToBin(data.textNote.copy(name = "")).let {
        launch { mainScreen { openBinPage { openTextNote(it) } } }
    }

    @Test fun contentOnBinWithName() = data.insertTextToBin().let {
        launch { mainScreen { openBinPage { openTextNote(it) } } }
    }

    @Test fun contentOnCreate() = data.createText().let {
        launch { mainScreen { openAddDialog { createTextNote(it) } } }
    }

    @Test fun contentOnReadWithoutName() = data.insertText(data.textNote.copy(name = "")).let {
        launch { mainScreen { openNotesPage { openTextNote(it) } } }
    }

    @Test fun contentOnReadWithName() = data.insertText().let {
        launch { mainScreen { openNotesPage { openTextNote(it) } } }
    }

    /**
     * ToolbarArrow / BackPress
     */

    @Test fun closeOnBin()  = data.insertTextToBin().let {
        launch {
            mainScreen {
                openBinPage { openTextNote(it) { toolbar { onClickBack() } } }
                assert()
                openBinPage { openTextNote(it) { onPressBack() } }
                assert()
            }
        }
    }

    @Test fun closeOnCreate() = data.createText().let {
        launch {
            mainScreen {
                openAddDialog { createTextNote(it) { toolbar { onClickBack() } } }
                assert()
                openAddDialog { createTextNote(it) { onPressBack() } }
                assert()
            }
        }
    }

    @Test fun closeOnRead() = data.insertText().let {
        launch {
            mainScreen {
                openNotesPage { openTextNote(it) { toolbar { onClickBack() } } }
                assert()
                openNotesPage { openTextNote(it) { onPressBack() } }
                assert()
            }
        }
    }

    @Test fun saveOnCreate() = data.createText().let {
        launch {
            mainScreen {
                openAddDialog {
                    createTextNote(it) {
                        toolbar { onEnterName(data.uniqueString) }
                        onEnterText(data.uniqueString)
                        onPressBack()
                    }
                }
            }
        }
    }

    @Test fun saveOnEdit() = data.insertText().let {
        launch {
            mainScreen {
                openNotesPage {
                    openTextNote(it) {
                        controlPanel { onEdit() }
                        toolbar { onEnterName(data.uniqueString) }
                        onEnterText(data.uniqueString)
                        onPressBack()
                    }
                }
            }
        }
    }

    @Test fun cancelOnEdit() = data.insertText().let {
        launch {
            mainScreen {
                openNotesPage {
                    openTextNote(it) {
                        controlPanel { onEdit() }
                        onEnterText(data.uniqueString)
                        toolbar {
                            onEnterName(data.uniqueString)
                            onClickBack()
                        }
                    }
                }
            }
        }
    }

    /**
     * Panel action
     */

    @Test fun actionOnBinRestore()  = data.insertTextToBin().let {
        launch {
            mainScreen {
                openNotesPage(empty = true)

                openBinPage {
                    openTextNote(it) { controlPanel { onRestore() } }
                    assert(empty = true)
                }

                openNotesPage()
            }
        }
    }

    @Test fun actionOnBinRestoreOpen()  = data.insertTextToBin().let {
        launch {
            mainScreen {
                openNotesPage(empty = true)

                openBinPage {
                    openTextNote(it) {
                        controlPanel { onRestoreOpen() }
                        onPressBack()
                    }
                    assert(empty = true)
                }

                openNotesPage()
            }
        }
    }

    @Test fun actionOnBinClear() = data.insertTextToBin().let {
        launch {
            mainScreen {
                openNotesPage(empty = true)

                openBinPage {
                    openTextNote(it) { controlPanel { onClear() } }
                    assert(empty = true)
                }

                openNotesPage(empty = true)
            }
        }
    }


    // TODO #TEST write test
    @Test fun actionOnReadNotification() {}

    @Test fun actionOnReadBind() = bindTestPrototype(isStatus = false)

    @Test fun actionOnReadUnbind() = bindTestPrototype(isStatus = true)

    private fun bindTestPrototype(isStatus: Boolean) {
        val model = data.insertText(data.textNote.copy(isStatus = isStatus))

        launch {
            mainScreen {
                openNotesPage {
                    openTextNote(model) {
                        controlPanel { onBind() }
                        onPressBack()
                    }

                    openNoteDialog(model)
                }
            }
        }
    }

    @Test fun actionOnReadConvert() = data.insertText().let {
        launch {
            mainScreen { openNotesPage { openTextNote(it) { controlPanel { onConvert() } } } }
        }
    }

    @Test fun actionOnReadDelete() = data.insertText().let {
        launch {
            mainScreen {
                openBinPage(empty = true)

                openNotesPage {
                    openTextNote(it) { controlPanel { onDelete() } }
                    assert(empty = true)
                }

                openBinPage()
            }
        }
    }

    @Test fun actionOnReadEdit() = data.insertText().let {
        launch { mainScreen { openNotesPage { openTextNote(it) { controlPanel { onEdit() } } } } }
    }


    // TODO #TEST write test
    @Test fun actionOnEditUndoRedo() {}

    // TODO #TEST write test
    @Test fun actionOnEditRank() {}

    @Test fun actionOnCreateColor() = data.createText().let {
        launch {
            mainScreen { openAddDialog { createTextNote(it) { controlPanel { onColor() } } } }
        }
    }

    @Test fun actionOnEditColor() = data.insertText().let {
        launch {
            mainScreen {
                openNotesPage { openTextNote(it) { controlPanel { onEdit().onColor() } } }
            }
        }
    }

    @Test fun actionOnCreateSave() = data.createText().let {
        launch {
            mainScreen {
                openAddDialog {
                    createTextNote(it) {
                        toolbar { onEnterName(data.uniqueString) }
                        onEnterText(data.uniqueString)
                        onEnterText()
                        onEnterText(data.uniqueString)
                        controlPanel { onSave() }
                    }
                }
            }
        }
    }

    @Test fun actionOnEditSave() = data.insertText().let {
        launch {
            mainScreen {
                openNotesPage {
                    openTextNote(it) {
                        controlPanel { onEdit() }
                        onEnterText()
                        toolbar { onEnterName(data.uniqueString) }
                        onEnterText(data.uniqueString)
                        controlPanel { onSave() }
                    }
                }
            }
        }
    }

    /**
     * Dialogs
     */

    // TODO #TEST end assert
    @Test fun convertDialogCloseAndWork() = data.insertText().let {
        launch {
            mainScreen {
                openNotesPage {
                    openTextNote(it) {
                        controlPanel { onConvert { onCloseSoft() } }
                        assert()
                        controlPanel { onConvert { onClickNo() } }
                        assert()
                        controlPanel { onConvert { onClickYes() } }
                    }
                }
            }
        }
    }

    // TODO #TEST add note to rank and check it hide
    @Test fun rankDialogCloseAndWork() {}

    @Test fun colorDialogCloseAndWork() = data.createText().let {
        launch {
            mainScreen {
                openAddDialog {
                    createTextNote(it) {
                        controlPanel { onColor { onCloseSoft() } }
                        assert()
                        controlPanel { onColor { onClickCancel() } }
                        assert()
                        controlPanel {
                            onColor { onClickEveryItem().onClickItem().onClickAccept() }
                        }
                    }
                }
            }
        }
    }

}