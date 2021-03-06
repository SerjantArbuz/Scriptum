package sgtmelon.scriptum.presentation.screen.vm.impl.note

import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import sgtmelon.extension.nextString
import sgtmelon.scriptum.FastMock
import sgtmelon.scriptum.FastTest
import sgtmelon.scriptum.R
import sgtmelon.scriptum.domain.interactor.callback.note.ITextNoteInteractor
import sgtmelon.scriptum.domain.model.annotation.InputAction
import sgtmelon.scriptum.domain.model.data.IntentData.Note.Default
import sgtmelon.scriptum.domain.model.item.InputItem
import sgtmelon.scriptum.domain.model.item.InputItem.Cursor.Companion.get
import sgtmelon.scriptum.domain.model.item.NoteItem
import sgtmelon.scriptum.domain.model.state.NoteState
import sgtmelon.scriptum.parent.ParentViewModelTest
import sgtmelon.scriptum.presentation.control.note.input.IInputControl
import sgtmelon.scriptum.presentation.control.note.input.InputControl
import sgtmelon.scriptum.presentation.control.note.save.ISaveControl
import sgtmelon.scriptum.presentation.screen.ui.callback.note.INoteConnector
import sgtmelon.scriptum.presentation.screen.ui.callback.note.ITextNoteFragment
import sgtmelon.scriptum.verifyDeepCopy
import kotlin.random.Random

/**
 * Test for [TextNoteViewModel].
 */
@ExperimentalCoroutinesApi
class TextNoteViewModelTest : ParentViewModelTest() {

    @MockK lateinit var callback: ITextNoteFragment
    @MockK lateinit var parentCallback: INoteConnector

    @MockK lateinit var interactor: ITextNoteInteractor

    @MockK lateinit var saveControl: ISaveControl
    @MockK lateinit var inputControl: IInputControl

    private val viewModel by lazy { TextNoteViewModel(application) }
    private val spyViewModel by lazy { spyk(viewModel, recordPrivateCalls = true) }

    private val fastTest by lazy {
        FastTest.ViewModel(
            callback, parentCallback, interactor,
            saveControl, inputControl, viewModel, spyViewModel, { FastMock.Note.deepCopy(it) },
            { verifyDeepCopy(it) }
        )
    }

    @Before override fun setup() {
        super.setup()

        viewModel.setCallback(callback)
        viewModel.setParentCallback(parentCallback)
        viewModel.setInteractor(interactor)

        viewModel.inputControl = inputControl
        viewModel.saveControl = saveControl

        assertEquals(Default.ID, viewModel.id)
        assertEquals(Default.COLOR, viewModel.color)
        assertTrue(viewModel.mayAnimateIcon)
        assertTrue(viewModel.rankDialogItemArray.isEmpty())

        assertNotNull(viewModel.callback)
        assertNotNull(viewModel.parentCallback)
    }

    @After override fun tearDown() {
        super.tearDown()

        confirmVerified(callback, parentCallback, interactor, inputControl, saveControl)
    }

    @Test override fun onDestroy() = fastTest.onDestroy()


    @Test fun cacheData() = fastTest.cacheData(mockk())

    @Test fun onSetup() = fastTest.onSetup()

    @Test fun getBundleData() = fastTest.getBundleData()

    @Test fun setupBeforeInitialize() {
        val color = Random.nextInt()

        viewModel.color = color
        viewModel.setupBeforeInitialize()

        verifySequence {
            callback.apply {
                setupBinding()
                setupToolbar(color)
                setupEnter(inputControl)
            }
        }
    }

    @Test fun tryInitializeNote() = startCoTest {
        val name = nextString()
        val itemArray = Array(size = 10) { nextString() }
        val defaultColor = Random.nextInt()
        val noteItem = mockk<NoteItem.Text>()
        val id = Random.nextLong()
        val isBin = Random.nextBoolean()

        every { spyViewModel.isNoteInitialized() } returns true

        assertTrue(spyViewModel.tryInitializeNote())

        every { spyViewModel.isNoteInitialized() } returns false
        every { parentCallback.getString(R.string.dialog_item_rank) } returns name
        coEvery { interactor.getRankDialogItemArray(name) } returns itemArray
        every { interactor.defaultColor } returns defaultColor
        mockkObject(NoteItem.Text)
        every { NoteItem.Text.getCreate(defaultColor) } returns noteItem
        every { spyViewModel.cacheData() } returns Unit

        assertTrue(spyViewModel.tryInitializeNote())

        coEvery { interactor.getItem(id) } returns null

        spyViewModel.id = id
        assertFalse(spyViewModel.tryInitializeNote())

        coEvery { interactor.getItem(id) } returns noteItem
        FastMock.Note.deepCopy(noteItem)
        every { noteItem.isBin } returns isBin

        assertTrue(spyViewModel.tryInitializeNote())

        coVerifySequence {
            spyViewModel.tryInitializeNote()
            spyViewModel.isNoteInitialized()

            spyViewModel.tryInitializeNote()
            spyViewModel.isNoteInitialized()
            spyViewModel.parentCallback
            parentCallback.getString(R.string.dialog_item_rank)
            spyViewModel.interactor
            interactor.getRankDialogItemArray(name)
            spyViewModel.rankDialogItemArray = itemArray
            spyViewModel.id
            spyViewModel.interactor
            interactor.defaultColor
            NoteItem.Text.getCreate(defaultColor)
            spyViewModel.noteItem = noteItem
            spyViewModel.cacheData()
            spyViewModel.noteState = NoteState(isCreate = true)

            spyViewModel.id = id
            spyViewModel.tryInitializeNote()
            spyViewModel.isNoteInitialized()
            spyViewModel.parentCallback
            parentCallback.getString(R.string.dialog_item_rank)
            spyViewModel.interactor
            interactor.getRankDialogItemArray(name)
            spyViewModel.rankDialogItemArray = itemArray
            spyViewModel.id
            spyViewModel.interactor
            spyViewModel.id
            interactor.getItem(id)
            spyViewModel.parentCallback
            parentCallback.finish()

            spyViewModel.tryInitializeNote()
            spyViewModel.isNoteInitialized()
            spyViewModel.parentCallback
            parentCallback.getString(R.string.dialog_item_rank)
            spyViewModel.interactor
            interactor.getRankDialogItemArray(name)
            spyViewModel.rankDialogItemArray = itemArray
            spyViewModel.id
            spyViewModel.interactor
            spyViewModel.id
            interactor.getItem(id)
            spyViewModel.noteItem = noteItem
            verifyDeepCopy(noteItem)
            spyViewModel.restoreItem = noteItem
            spyViewModel.callback
            callback.sendNotifyNotesBroadcast()
            spyViewModel.noteItem
            noteItem.isBin
            spyViewModel.noteState = NoteState(isBin = isBin)
        }
    }

    @Test fun setupAfterInitialize() = startCoTest {
        val noteState = mockk<NoteState>(relaxUnitFun = true)
        val isRankEmpty = Random.nextBoolean()
        val rankDialogItemArray = if (isRankEmpty) {
            arrayOf(nextString())
        } else {
            arrayOf(nextString(), nextString())
        }
        val isEdit = Random.nextBoolean()

        every { spyViewModel.setupEditMode(isEdit) } returns Unit
        every { noteState.isEdit } returns isEdit

        spyViewModel.rankDialogItemArray = rankDialogItemArray
        spyViewModel.noteState = noteState
        spyViewModel.setupAfterInitialize()

        coVerifySequence {
            spyViewModel.rankDialogItemArray = rankDialogItemArray
            spyViewModel.noteState = noteState
            spyViewModel.setupAfterInitialize()

            spyViewModel.callback
            spyViewModel.rankDialogItemArray
            callback.setupDialog(rankDialogItemArray)

            spyViewModel.mayAnimateIcon = false
            spyViewModel.noteState
            noteState.isEdit
            spyViewModel.setupEditMode(isEdit)
            spyViewModel.mayAnimateIcon = true

            spyViewModel.callback
            spyViewModel.rankDialogItemArray
            callback.onBindingLoad(isRankEmpty)
        }
    }

    @Test fun isNoteInitialized() = fastTest.isNoteInitialized(mockk())


    @Test fun onSaveData() = fastTest.onSaveData()

    @Test fun onResume() = fastTest.onResume()

    @Test fun onPause() = fastTest.onPause()


    @Test fun onClickBackArrow() = fastTest.onClickBackArrow()

    @Test fun onPressBack() = fastTest.onPressBack()

    @Test fun onRestoreData() {
        assertFalse(spyViewModel.onRestoreData())

        val noteItem = mockk<NoteItem.Text>(relaxUnitFun = true)
        val restoreItem = mockk<NoteItem.Text>(relaxUnitFun = true)

        val id = Random.nextLong()
        val colorFrom = Random.nextInt()
        val colorTo = Random.nextInt()

        every { noteItem.color } returns colorFrom
        every { spyViewModel.setupEditMode(isEdit = false) } returns Unit

        FastMock.Note.deepCopy(restoreItem, color = colorTo)

        spyViewModel.id = id
        spyViewModel.noteItem = noteItem
        spyViewModel.restoreItem = restoreItem

        assertTrue(spyViewModel.onRestoreData())

        verifySequence {
            spyViewModel.onRestoreData()
            spyViewModel.id

            spyViewModel.id = id
            spyViewModel.noteItem = noteItem
            spyViewModel.restoreItem = restoreItem
            spyViewModel.onRestoreData()

            spyViewModel.id
            spyViewModel.noteItem
            noteItem.color
            spyViewModel.restoreItem
            verifyDeepCopy(restoreItem)
            spyViewModel.noteItem = restoreItem
            spyViewModel.noteItem
            restoreItem.color

            spyViewModel.setupEditMode(isEdit = false)
            spyViewModel.callback
            callback.tintToolbar(colorFrom, colorTo)
            spyViewModel.parentCallback
            parentCallback.onUpdateNoteColor(colorTo)
            spyViewModel.inputControl
            inputControl.reset()
        }
    }

    //region Dialog results

    @Test fun onResultColorDialog() = fastTest.onResultColorDialog(mockk())

    @Test fun onResultRankDialog() = fastTest.onResultRankDialog(mockk())

    @Test fun onResultDateDialog() = fastTest.onResultDateDialog()

    @Test fun onResultDateDialogClear() = fastTest.onResultDateDialogClear(mockk(), mockk())

    @Test fun onResultTimeDialog() = fastTest.onResultTimeDialog(mockk(), mockk())

    @Test fun onResultConvertDialog() = fastTest.onResultConvertDialog(mockk())

    //endregion

    @Test fun onReceiveUnbindNote() = fastTest.onReceiveUnbindNote(mockk(), mockk())

    //region Menu click

    @Test fun onMenuRestore() = fastTest.onMenuRestore(mockk())

    @Test fun onMenuRestoreOpen() = fastTest.onMenuRestoreOpen(mockk())

    @Test fun onMenuClear() = fastTest.onMenuClear(mockk())


    @Test fun onMenuUndo() = fastTest.onMenuUndo()

    @Test fun onMenuRedo() = fastTest.onMenuRedo()

    @Test fun onMenuUndoRedo() = fastTest.onMenuUndoRedo(mockk())

    @Test fun onMenuUndoRedoSelect() {
        val item = mockk<InputItem>()
        val isUndo = Random.nextBoolean()

        every { item.tag } returns InputAction.RANK
        every { spyViewModel.onMenuUndoRedoRank(item, isUndo) } returns Unit
        spyViewModel.onMenuUndoRedoSelect(item, isUndo)

        every { item.tag } returns InputAction.COLOR
        every { spyViewModel.onMenuUndoRedoColor(item, isUndo) } returns Unit
        spyViewModel.onMenuUndoRedoSelect(item, isUndo)

        every { item.tag } returns InputAction.NAME
        every { spyViewModel.onMenuUndoRedoName(item, isUndo) } returns Unit
        spyViewModel.onMenuUndoRedoSelect(item, isUndo)

        every { item.tag } returns InputAction.TEXT
        every { spyViewModel.onMenuUndoRedoText(item, isUndo) } returns Unit
        spyViewModel.onMenuUndoRedoSelect(item, isUndo)

        verifySequence {
            spyViewModel.onMenuUndoRedoSelect(item, isUndo)
            spyViewModel.inputControl
            inputControl.isEnabled = false
            item.tag
            spyViewModel.onMenuUndoRedoRank(item, isUndo)
            spyViewModel.inputControl
            inputControl.isEnabled = true

            spyViewModel.onMenuUndoRedoSelect(item, isUndo)
            spyViewModel.inputControl
            inputControl.isEnabled = false
            item.tag
            spyViewModel.onMenuUndoRedoColor(item, isUndo)
            spyViewModel.inputControl
            inputControl.isEnabled = true

            spyViewModel.onMenuUndoRedoSelect(item, isUndo)
            spyViewModel.inputControl
            inputControl.isEnabled = false
            item.tag
            spyViewModel.onMenuUndoRedoName(item, isUndo)
            spyViewModel.inputControl
            inputControl.isEnabled = true

            spyViewModel.onMenuUndoRedoSelect(item, isUndo)
            spyViewModel.inputControl
            inputControl.isEnabled = false
            item.tag
            spyViewModel.onMenuUndoRedoText(item, isUndo)
            spyViewModel.inputControl
            inputControl.isEnabled = true
        }
    }

    @Test fun onMenuUndoRedoRank() = fastTest.onMenuUndoRedoRank(mockk(relaxUnitFun = true))

    @Test fun onMenuUndoRedoColor() = fastTest.onMenuUndoRedoColor(mockk())

    @Test fun onMenuUndoRedoName() = fastTest.onMenuUndoRedoName()

    @Test fun onMenuUndoRedoText() {
        val item = mockk<InputItem>()
        val isUndo = Random.nextBoolean()
        val cursor = mockk<InputItem.Cursor>(relaxUnitFun = true)

        val text = nextString()
        val position = Random.nextInt()

        mockkObject(InputItem.Cursor)

        every { item[isUndo] } returns text
        every { item.cursor } returns cursor
        every { cursor[isUndo] } returns position

        viewModel.onMenuUndoRedoText(item, isUndo)

        verifySequence {
            item[isUndo]
            item.cursor
            cursor[isUndo]
            callback.changeText(text, position)
        }
    }

    @Test fun onMenuRank() = fastTest.onMenuRank(mockk())

    @Test fun onMenuColor() = fastTest.onMenuColor(mockk())

    @Test fun onMenuSave_startSkip() {
        val noteItem = mockk<NoteItem.Text>()
        val noteState = mockk<NoteState>(relaxUnitFun = true)

        viewModel.noteState = noteState
        viewModel.noteItem = noteItem

        every { callback.isDialogOpen } returns true
        assertFalse(viewModel.onMenuSave(changeMode = true))

        every { noteState.isEdit } returns false
        every { callback.isDialogOpen } returns false
        assertFalse(viewModel.onMenuSave(changeMode = true))

        assertFalse(viewModel.onMenuSave(changeMode = false))

        val changeMode = Random.nextBoolean()

        every { noteState.isEdit } returns true
        every { noteItem.isSaveEnabled() } returns false
        assertFalse(viewModel.onMenuSave(changeMode))

        coVerifyOrder {
            callback.isDialogOpen

            callback.isDialogOpen
            noteState.isEdit

            noteState.isEdit

            if (changeMode) {
                callback.isDialogOpen
            }
            noteState.isEdit
            noteItem.isSaveEnabled()
        }
    }

    @Test fun onMenuSave_notChangeMode() {
        val noteItem = mockk<NoteItem.Text>()
        val noteState = mockk<NoteState>(relaxUnitFun = true)
        val color = Random.nextInt()

        viewModel.noteState = noteState
        viewModel.noteItem = noteItem

        every { noteState.isEdit } returns true
        every { noteItem.isSaveEnabled() } returns true
        every { noteItem.onSave() } returns noteItem
        every { noteState.isCreate } returns false
        every { noteItem.color } returns color
        coEvery { spyViewModel.saveBackgroundWork() } returns Unit
        assertTrue(spyViewModel.onMenuSave(changeMode = false))

        every { noteState.isCreate } returns true
        assertTrue(spyViewModel.onMenuSave(changeMode = false))

        coVerifyOrder {
            spyViewModel.onMenuSave(changeMode = false)
            spyViewModel.noteState
            noteState.isEdit
            spyViewModel.noteItem
            noteItem.isSaveEnabled()
            spyViewModel.noteItem
            noteItem.onSave()
            spyViewModel.noteState
            noteState.isCreate
            spyViewModel.parentCallback
            spyViewModel.noteItem
            noteItem.color
            parentCallback.onUpdateNoteColor(color)
            spyViewModel.saveBackgroundWork()

            spyViewModel.onMenuSave(changeMode = false)
            spyViewModel.noteState
            noteState.isEdit
            spyViewModel.noteItem
            noteItem.isSaveEnabled()
            spyViewModel.noteItem
            noteItem.onSave()
            spyViewModel.noteState
            noteState.isCreate
            spyViewModel.callback
            callback.setToolbarBackIcon(isCancel = true, needAnim = true)
            spyViewModel.parentCallback
            spyViewModel.noteItem
            noteItem.color
            parentCallback.onUpdateNoteColor(color)
            spyViewModel.saveBackgroundWork()
        }
    }

    @Test fun onMenuSave_changeMode() {
        val noteItem = mockk<NoteItem.Text>()
        val noteState = mockk<NoteState>(relaxUnitFun = true)
        val color = Random.nextInt()

        viewModel.noteState = noteState
        viewModel.noteItem = noteItem

        every { callback.isDialogOpen } returns false
        every { noteState.isEdit } returns true
        every { noteItem.isSaveEnabled() } returns true
        every { noteItem.onSave() } returns noteItem
        every { spyViewModel.setupEditMode(isEdit = false) } returns Unit
        every { noteItem.color } returns color
        coEvery { spyViewModel.saveBackgroundWork() } returns Unit
        assertTrue(spyViewModel.onMenuSave(changeMode = true))

        coVerifyOrder {
            spyViewModel.onMenuSave(changeMode = true)
            spyViewModel.callback
            callback.isDialogOpen
            spyViewModel.noteState
            noteState.isEdit
            spyViewModel.noteItem
            noteItem.isSaveEnabled()
            spyViewModel.noteItem
            noteItem.onSave()
            spyViewModel.callback
            callback.hideKeyboard()
            spyViewModel.setupEditMode(isEdit = false)
            spyViewModel.inputControl
            inputControl.reset()
            spyViewModel.parentCallback
            spyViewModel.noteItem
            noteItem.color
            parentCallback.onUpdateNoteColor(color)
            spyViewModel.saveBackgroundWork()
        }
    }

    @Test fun saveBackgroundWork() = startCoTest {
        val noteItem = mockk<NoteItem.Text>()
        val noteState = mockk<NoteState>()
        val id = Random.nextLong()

        every { spyViewModel.cacheData() } returns Unit
        every { noteState.isCreate } returns false

        spyViewModel.noteItem = noteItem
        spyViewModel.noteState = noteState
        spyViewModel.saveBackgroundWork()
        assertEquals(Default.ID, spyViewModel.id)

        every { noteState.isCreate } returns true
        every { noteState.isCreate = NoteState.ND_CREATE } returns Unit
        every { noteItem.id } returns id

        spyViewModel.saveBackgroundWork()
        assertEquals(id, spyViewModel.id)

        coVerifySequence {
            spyViewModel.noteItem = noteItem
            spyViewModel.noteState = noteState
            spyViewModel.saveBackgroundWork()
            spyViewModel.interactor
            spyViewModel.noteItem
            spyViewModel.noteState
            noteState.isCreate
            interactor.saveNote(noteItem, isCreate = false)
            spyViewModel.cacheData()
            spyViewModel.noteState
            noteState.isCreate
            spyViewModel.callback
            callback.sendNotifyNotesBroadcast()
            spyViewModel.id

            spyViewModel.saveBackgroundWork()
            spyViewModel.interactor
            spyViewModel.noteItem
            spyViewModel.noteState
            noteState.isCreate
            interactor.saveNote(noteItem, isCreate = true)
            spyViewModel.cacheData()
            spyViewModel.noteState
            noteState.isCreate
            spyViewModel.noteState
            noteState.isCreate = NoteState.ND_CREATE
            spyViewModel.noteItem
            noteItem.id
            spyViewModel.id = id
            spyViewModel.parentCallback
            spyViewModel.id
            parentCallback.onUpdateNoteId(id)
            spyViewModel.callback
            callback.sendNotifyNotesBroadcast()
            spyViewModel.id
        }
    }

    @Test fun onMenuNotification() = fastTest.onMenuNotification(mockk())

    @Test fun onMenuBind() = fastTest.onMenuBind(mockk(), mockk())

    @Test fun onMenuConvert() = fastTest.onMenuConvert()

    @Test fun onMenuDelete() = fastTest.onMenuDelete(mockk())

    @Test fun onMenuEdit() = fastTest.onMenuEdit()

    @Test fun setupEditMode() {
        val noteItem = mockk<NoteItem.Text>()
        val noteState = mockk<NoteState>(relaxUnitFun = true)

        val isCreate = Random.nextBoolean()
        val mayAnimateIcon = Random.nextBoolean()
        val access = mockk<InputControl.Access>()

        every { noteState.isCreate } returns isCreate
        every { inputControl.access } returns access

        viewModel.noteItem = noteItem
        viewModel.noteState = noteState
        viewModel.mayAnimateIcon = mayAnimateIcon

        viewModel.setupEditMode(isEdit = false)
        viewModel.setupEditMode(isEdit = true)

        verifySequence {
            inputControl.isEnabled = false
            noteState.isEdit = false
            noteState.isCreate
            callback.setToolbarBackIcon(
                isCancel = false,
                needAnim = !isCreate && mayAnimateIcon
            )
            callback.onBindingEdit(noteItem, isEditMode = false)
            inputControl.access
            callback.onBindingInput(noteItem, access)
            saveControl.needSave = true
            saveControl.setSaveEvent(isWork = false)
            inputControl.isEnabled = true

            inputControl.isEnabled = false
            noteState.isEdit = true
            noteState.isCreate
            noteState.isCreate
            callback.setToolbarBackIcon(
                isCancel = !isCreate,
                needAnim = !isCreate && mayAnimateIcon
            )
            callback.onBindingEdit(noteItem, isEditMode = true)
            inputControl.access
            callback.onBindingInput(noteItem, access)
            noteState.isCreate
            callback.focusOnEdit(isCreate)
            saveControl.needSave = true
            saveControl.setSaveEvent(isWork = true)
            inputControl.isEnabled = true
        }
    }

    //endregion

    @Test fun onResultSaveControl() = fastTest.onResultSaveControl()

    @Test fun onInputTextChange() = fastTest.onInputTextChange(mockk())

}