package sgtmelon.scriptum.presentation.screen.vm.impl.note

import android.view.inputmethod.EditorInfo
import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import sgtmelon.extension.nextString
import sgtmelon.scriptum.FastMock
import sgtmelon.scriptum.FastTest
import sgtmelon.scriptum.ParentViewModelTest
import sgtmelon.scriptum.domain.interactor.callback.IBindInteractor
import sgtmelon.scriptum.domain.interactor.callback.note.IRollNoteInteractor
import sgtmelon.scriptum.domain.model.item.NoteItem
import sgtmelon.scriptum.domain.model.item.RollItem
import sgtmelon.scriptum.domain.model.state.NoteState
import sgtmelon.scriptum.extension.move
import sgtmelon.scriptum.extension.validRemoveAt
import sgtmelon.scriptum.presentation.control.note.input.IInputControl
import sgtmelon.scriptum.presentation.control.note.input.InputControl
import sgtmelon.scriptum.presentation.control.note.save.ISaveControl
import sgtmelon.scriptum.presentation.screen.ui.callback.note.INoteConnector
import sgtmelon.scriptum.presentation.screen.ui.callback.note.IRollNoteFragment
import sgtmelon.scriptum.verifyDeepCopy
import kotlin.random.Random

/**
 * Test for [RollNoteViewModel].
 */
@ExperimentalCoroutinesApi
class RollNoteViewModelTest : ParentViewModelTest() {

    @MockK lateinit var callback: IRollNoteFragment
    @MockK lateinit var parentCallback: INoteConnector

    @MockK lateinit var interactor: IRollNoteInteractor
    @MockK lateinit var bindInteractor: IBindInteractor

    @MockK lateinit var inputControl: IInputControl
    @MockK lateinit var saveControl: ISaveControl

    private val viewModel by lazy { RollNoteViewModel(application) }
    private val spyViewModel by lazy { spyk(viewModel, recordPrivateCalls = true) }

    private val fastTest by lazy {
        FastTest.ViewModel(
            callback, parentCallback, interactor, bindInteractor,
            inputControl, viewModel, spyViewModel, { FastMock.Note.deepCopy(it) },
            { verifyDeepCopy(it) }
        )
    }

    override fun setUp() {
        super.setUp()

        TODO()
        //        viewModel.setCallback(callback)
        //        viewModel.setParentCallback(parentCallback)
        //        viewModel.setInteractor(interactor, bindInteractor)
        //
        //        viewModel.inputControl = inputControl
        //
        //        assertEquals(NoteData.Default.ID, viewModel.id)
        //        assertEquals(NoteData.Default.COLOR, viewModel.color)
        //        assertTrue(viewModel.mayAnimateIcon)
        //        assertTrue(viewModel.rankDialogItemArray.isEmpty())
        ////        assertTrue(viewModel.isVisible)
        //
        //        assertNotNull(viewModel.callback)
        //        assertNotNull(viewModel.parentCallback)
    }

    override fun tearDown() {
        super.tearDown()

        confirmVerified(
            callback, parentCallback, interactor, bindInteractor, inputControl, saveControl
        )
    }

    @Test override fun onDestroy() = fastTest.onDestroy()


    @Test fun cacheData() = fastTest.cacheData(mockk())

    @Test fun onSetup() = fastTest.onSetup()

    @Test fun getBundleData() = fastTest.getBundleData()

    @Test fun setupBeforeInitialize() {
        val theme = Random.nextInt()
        val color = Random.nextInt()

        viewModel.color = color
        every { interactor.theme } returns theme

        viewModel.setupBeforeInitialize()

        assertFalse(viewModel.isFirstRun)

        verifySequence {
            interactor.theme
            callback.apply {
                setupBinding(theme)
                setupToolbar(theme, color)
                setupEnter(inputControl)
                setupRecycler(inputControl, isFirstRun = true)

                showToolbarVisibleIcon(isShow = false)
            }
        }
    }

    @Test fun tryInitializeNote() = startCoTest {
        TODO()
        //        val name = nextString()
        //        val itemArray = Array(size = 10) { nextString() }
        //        val defaultColor = Random.nextInt()
        //        val noteItem = mockk<NoteItem.Roll>()
        //        val id = Random.nextLong()
        //        val isBin = Random.nextBoolean()
        //        val isVisible = Random.nextBoolean()
        //
        //        every { spyViewModel.isNoteInitialized() } returns true
        //
        //        assertTrue(spyViewModel.tryInitializeNote())
        //
        //        every { spyViewModel.isNoteInitialized() } returns false
        //        every { parentCallback.getString(R.string.dialog_item_rank) } returns name
        //        coEvery { interactor.getRankDialogItemArray(name) } returns itemArray
        //        every { interactor.defaultColor } returns defaultColor
        //        mockkObject(NoteItem.Roll)
        //        every { NoteItem.Roll.getCreate(defaultColor) } returns noteItem
        //        every { spyViewModel.cacheData() } returns Unit
        //
        //        assertTrue(spyViewModel.tryInitializeNote())
        //
        //        coEvery { interactor.getItem(id) } returns null
        //
        //        spyViewModel.id = id
        //        assertFalse(spyViewModel.tryInitializeNote())
        //
        //        coEvery { interactor.getItem(id) } returns noteItem
        //        FastMock.Note.deepCopy(noteItem)
        //        every { noteItem.isBin } returns isBin
        //        coEvery { interactor.getVisible(id) } returns isVisible
        //
        //        assertTrue(spyViewModel.tryInitializeNote())
        //
        //        coVerifySequence {
        //            spyViewModel.tryInitializeNote()
        //            spyViewModel.isNoteInitialized()
        //
        //            spyViewModel.tryInitializeNote()
        //            spyViewModel.isNoteInitialized()
        //            spyViewModel.parentCallback
        //            parentCallback.getString(R.string.dialog_item_rank)
        //            spyViewModel.interactor
        //            interactor.getRankDialogItemArray(name)
        //            spyViewModel.rankDialogItemArray = itemArray
        //            spyViewModel.id
        //            spyViewModel.interactor
        //            interactor.defaultColor
        //            NoteItem.Roll.getCreate(defaultColor)
        //            spyViewModel.noteItem = noteItem
        //            spyViewModel.cacheData()
        //            spyViewModel.noteState = NoteState(isCreate = true)
        //
        //            spyViewModel.id = id
        //            spyViewModel.tryInitializeNote()
        //            spyViewModel.isNoteInitialized()
        //            spyViewModel.parentCallback
        //            parentCallback.getString(R.string.dialog_item_rank)
        //            spyViewModel.interactor
        //            interactor.getRankDialogItemArray(name)
        //            spyViewModel.rankDialogItemArray = itemArray
        //            spyViewModel.id
        //            spyViewModel.interactor
        //            spyViewModel.id
        //            interactor.getItem(id)
        //            spyViewModel.parentCallback
        //            parentCallback.finish()
        //
        //            spyViewModel.tryInitializeNote()
        //            spyViewModel.isNoteInitialized()
        //            spyViewModel.parentCallback
        //            parentCallback.getString(R.string.dialog_item_rank)
        //            spyViewModel.interactor
        //            interactor.getRankDialogItemArray(name)
        //            spyViewModel.rankDialogItemArray = itemArray
        //            spyViewModel.id
        //            spyViewModel.interactor
        //            spyViewModel.id
        //            interactor.getItem(id)
        //            spyViewModel.noteItem = noteItem
        //            verifyDeepCopy(noteItem)
        //            spyViewModel.restoreItem = noteItem
        //            spyViewModel.noteItem
        //            noteItem.isBin
        //            spyViewModel.noteState = NoteState(isBin = isBin)
        //            spyViewModel.interactor
        //            spyViewModel.id
        //            interactor.getVisible(id)
        //            spyViewModel.isVisible = isVisible
        //        }
    }

    @Test fun setupAfterInitialize() = startCoTest {
        TODO()
        //        val noteItem = mockk<NoteItem.Roll>()
        //        val rollList = mockk<MutableList<RollItem>>()
        //        val noteState = mockk<NoteState>(relaxUnitFun = true)
        //
        //        val isVisible = Random.nextBoolean()
        //        val isRankEmpty = Random.nextBoolean()
        //        val rankDialogItemArray = if (isRankEmpty) {
        //            arrayOf(nextString())
        //        } else {
        //            arrayOf(nextString(), nextString())
        //        }
        //        val isEdit = Random.nextBoolean()
        //
        //        spyViewModel.noteItem = noteItem
        //        spyViewModel.rankDialogItemArray = rankDialogItemArray
        //        spyViewModel.noteState = noteState
        //        spyViewModel.isVisible = isVisible
        //
        //        every { spyViewModel.getList(noteItem) } returns rollList
        //        every { spyViewModel.onUpdateInfo() } returns Unit
        //        every { noteState.isEdit } returns isEdit
        //        every { spyViewModel.setupEditMode(isEdit) } returns Unit
        //
        //        spyViewModel.setupAfterInitialize()
        //
        //        coVerifySequence {
        //            spyViewModel.noteItem = noteItem
        //            spyViewModel.rankDialogItemArray = rankDialogItemArray
        //            spyViewModel.noteState = noteState
        //            spyViewModel.isVisible = isVisible
        //            spyViewModel.setupAfterInitialize()
        //
        //            spyViewModel.callback
        //            spyViewModel.rankDialogItemArray
        //            callback.setupDialog(rankDialogItemArray)
        //            spyViewModel.callback
        //            callback.setupProgress()
        //
        //            spyViewModel.mayAnimateIcon = false
        //            spyViewModel.noteState
        //            noteState.isEdit
        //            spyViewModel.setupEditMode(isEdit)
        //            spyViewModel.mayAnimateIcon = true
        //
        //            spyViewModel.callback
        //            callback.showToolbarVisibleIcon(isShow = true)
        //            callback.setToolbarVisibleIcon(isVisible, needAnim = false)
        //            spyViewModel.noteItem
        //            spyViewModel.getList(noteItem)
        //            callback.notifyDataSetChanged(rollList)
        //
        //            spyViewModel.onUpdateInfo()
        //
        //            spyViewModel.callback
        //            spyViewModel.rankDialogItemArray
        //            callback.onBindingLoad(isRankEmpty)
        //        }
    }

    @Test fun isNoteInitialized() = fastTest.isNoteInitialized(mockk())


    @Test fun onSaveData() = fastTest.onSaveData()

    @Test fun onResume() = fastTest.onResume()

    @Test fun onPause() = fastTest.onPause()


    @Test fun onClickBackArrow() = fastTest.onClickBackArrow()

    @Test fun onPressBack() = fastTest.onPressBack()

    @Test fun onRestoreData() {
        assertFalse(spyViewModel.onRestoreData())

        val noteItem = mockk<NoteItem.Roll>(relaxUnitFun = true)
        val restoreItem = mockk<NoteItem.Roll>(relaxUnitFun = true)
        val rollList = mockk<MutableList<RollItem>>()

        val id = Random.nextLong()
        val colorFrom = Random.nextInt()
        val colorTo = Random.nextInt()

        every { noteItem.color } returns colorFrom
        every { spyViewModel.setupEditMode(isEdit = false) } returns Unit
        every { spyViewModel.onUpdateInfo() } returns Unit
        every { spyViewModel.getList(restoreItem) } returns rollList

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

            spyViewModel.callback
            spyViewModel.noteItem
            spyViewModel.getList(restoreItem)
            callback.notifyDataSetChanged(rollList)

            spyViewModel.setupEditMode(isEdit = false)
            spyViewModel.onUpdateInfo()

            spyViewModel.callback
            callback.tintToolbar(colorFrom, colorTo)
            spyViewModel.parentCallback
            parentCallback.onUpdateNoteColor(colorTo)
            spyViewModel.inputControl
            inputControl.reset()
        }
    }


    @Test fun onClickVisible_onEdit() {
        TODO()
        //        val visibleFrom = Random.nextBoolean()
        //        val visibleTo = !visibleFrom
        //
        //        val noteState = mockk<NoteState>(relaxUnitFun = true)
        //        val noteItem = mockk<NoteItem.Roll>()
        //        val id = Random.nextLong()
        //
        //        every { spyViewModel.notifyListByVisible() } returns Unit
        //        every { noteState.isCreate } returns false
        //        every { noteItem.id } returns id
        //
        //        spyViewModel.isVisible = visibleFrom
        //        spyViewModel.noteState = noteState
        //        spyViewModel.noteItem = noteItem
        //
        //        spyViewModel.onClickVisible()
        //
        //        coVerifyOrder {
        //            spyViewModel.isVisible = visibleTo
        //
        //            callback.setToolbarVisibleIcon(visibleTo, needAnim = true)
        //            spyViewModel.notifyListByVisible()
        //            noteState.isCreate
        //            noteItem.id
        //            interactor.setVisible(id, visibleTo)
        //        }
    }

    @Test fun onClickVisible_onCreate() {
        TODO()
        //        val visibleFrom = Random.nextBoolean()
        //        val visibleTo = !visibleFrom
        //
        //        val noteState = mockk<NoteState>(relaxUnitFun = true)
        //
        //        every { spyViewModel.notifyListByVisible() } returns Unit
        //        every { noteState.isCreate } returns true
        //
        //        spyViewModel.isVisible = visibleFrom
        //        spyViewModel.noteState = noteState
        //
        //        spyViewModel.onClickVisible()
        //
        //        coVerifyOrder {
        //            spyViewModel.isVisible = visibleTo
        //
        //            callback.setToolbarVisibleIcon(visibleTo, needAnim = true)
        //            spyViewModel.notifyListByVisible()
        //            noteState.isCreate
        //        }
    }

    @Test fun onUpdateInfo() {
        //        val noteItem = mockk<NoteItem.Roll>(relaxUnitFun = true)
        //        val list = mockk<MutableList<RollItem>>(relaxUnitFun = true)
        //        val hideList = mockk<MutableList<RollItem>>(relaxUnitFun = true)
        //
        //        every { noteItem.list } returns list
        //        every { spyViewModel.hide(list) } returns hideList
        //
        //        spyViewModel.noteItem = noteItem
        //        spyViewModel.isVisible = false
        //
        //        every { list.size } returns 1
        //        every { hideList.size } returns 1
        //        spyViewModel.onUpdateInfo()
        //
        //        every { hideList.size } returns 0
        //        spyViewModel.onUpdateInfo()
        //
        //        every { list.size } returns 0
        //        spyViewModel.isVisible = true
        //        spyViewModel.onUpdateInfo()
        //
        //        every { list.size } returns 0
        //        spyViewModel.isVisible = false
        //        every { hideList.size } returns 0
        //        spyViewModel.onUpdateInfo()
        //
        //        verifySequence {
        //            spyViewModel.noteItem = noteItem
        //            spyViewModel.isVisible = false
        //            spyViewModel.onUpdateInfo()
        //            spyViewModel.noteItem
        //            noteItem.list
        //            list.size
        //            spyViewModel.noteItem
        //            noteItem.list
        //            spyViewModel.hide(list)
        //            hideList.size
        //            spyViewModel.callback
        //            callback.animateInfoVisible()
        //
        //            spyViewModel.onUpdateInfo()
        //            spyViewModel.noteItem
        //            noteItem.list
        //            list.size
        //            spyViewModel.noteItem
        //            noteItem.list
        //            spyViewModel.hide(list)
        //            hideList.size
        //            spyViewModel.callback
        //            callback.onBindingInfo(isListEmpty = false, isListHide = true)
        //            spyViewModel.callback
        //            callback.animateInfoVisible()
        //
        //            spyViewModel.isVisible = true
        //            spyViewModel.onUpdateInfo()
        //            spyViewModel.noteItem
        //            noteItem.list
        //            list.size
        //            spyViewModel.callback
        //            callback.onBindingInfo(isListEmpty = true, isListHide = false)
        //            spyViewModel.callback
        //            callback.animateInfoVisible()
        //
        //            spyViewModel.isVisible = false
        //            spyViewModel.onUpdateInfo()
        //            spyViewModel.noteItem
        //            noteItem.list
        //            list.size
        //            spyViewModel.noteItem
        //            noteItem.list
        //            spyViewModel.hide(list)
        //            hideList.size
        //            spyViewModel.callback
        //            callback.onBindingInfo(isListEmpty = true, isListHide = true)
        //            spyViewModel.callback
        //            callback.animateInfoVisible()
        //        }
        TODO()
    }

    @Test fun onEditorClick() {
        assertFalse(viewModel.onEditorClick(Random.nextInt()))

        val i = EditorInfo.IME_ACTION_DONE

        every { spyViewModel.onMenuSave(changeMode = true) } returns Random.nextBoolean()
        every { spyViewModel.onClickAdd(simpleClick = true) } returns Unit

        every { callback.getEnterText() } returns nextString()
        assertTrue(spyViewModel.onEditorClick(i))

        every { callback.getEnterText() } returns "   "
        assertTrue(spyViewModel.onEditorClick(i))

        every { callback.getEnterText() } returns ""
        assertTrue(spyViewModel.onEditorClick(i))

        verifySequence {
            spyViewModel.onEditorClick(i)
            spyViewModel.callback
            callback.getEnterText()
            spyViewModel.onClickAdd(simpleClick = true)

            repeat(times = 2) {
                spyViewModel.onEditorClick(i)
                spyViewModel.callback
                callback.getEnterText()
                spyViewModel.onMenuSave(changeMode = true)
            }
        }
    }

    @Test fun onClickAdd_onEmptyText() {
        val noteState = mockk<NoteState>(relaxUnitFun = true)

        viewModel.noteState = noteState

        every { callback.isDialogOpen } returns true
        every { noteState.isEdit } returns false
        viewModel.onClickAdd(Random.nextBoolean())

        every { callback.isDialogOpen } returns true
        every { noteState.isEdit } returns true
        viewModel.onClickAdd(Random.nextBoolean())

        every { callback.isDialogOpen } returns false
        every { noteState.isEdit } returns false
        viewModel.onClickAdd(Random.nextBoolean())

        every { callback.getEnterText() } returns "   "
        every { callback.isDialogOpen } returns false
        every { noteState.isEdit } returns true
        viewModel.onClickAdd(Random.nextBoolean())

        every { callback.getEnterText() } returns ""
        viewModel.onClickAdd(Random.nextBoolean())

        verifySequence {
            callback.isDialogOpen
            callback.isDialogOpen
            callback.isDialogOpen
            noteState.isEdit

            callback.isDialogOpen
            noteState.isEdit
            callback.getEnterText()

            callback.isDialogOpen
            noteState.isEdit
            callback.getEnterText()
        }
    }

    @Test fun onClickAdd_onNormalText() {
        val noteState = mockk<NoteState>(relaxUnitFun = true)
        val noteItem = mockk<NoteItem.Roll>(relaxUnitFun = true)

        val enterText = nextString()
        val optimalList = mockk<MutableList<RollItem>>()
        val normalList = mockk<MutableList<RollItem>>(relaxUnitFun = true)
        val size = Random.nextInt()
        val access = mockk<InputControl.Access>()

        every { callback.isDialogOpen } returns false
        every { noteState.isEdit } returns true
        every { callback.getEnterText() } returns enterText
        every { noteItem.list } returns normalList
        every { normalList.size } returns size
        every { inputControl.access } returns access
        every { spyViewModel.getList(noteItem) } returns optimalList

        spyViewModel.noteState = noteState
        spyViewModel.noteItem = noteItem

        spyViewModel.onClickAdd(simpleClick = false)
        spyViewModel.onClickAdd(simpleClick = true)

        verifySequence {
            spyViewModel.noteState = noteState
            spyViewModel.noteItem = noteItem

            spyViewModel.onClickAdd(simpleClick = false)
            spyViewModel.callback
            callback.isDialogOpen
            spyViewModel.noteState
            noteState.isEdit
            spyViewModel.callback
            callback.getEnterText()
            spyViewModel.callback
            callback.clearEnterText()
            val firstRollItem = RollItem(position = 0, text = enterText)
            spyViewModel.inputControl
            inputControl.onRollAdd(p = 0, valueTo = firstRollItem.toJson())
            spyViewModel.noteItem
            noteItem.list
            normalList.add(0, firstRollItem)
            spyViewModel.callback
            spyViewModel.noteItem
            spyViewModel.inputControl
            inputControl.access
            callback.onBindingInput(noteItem, access)
            spyViewModel.noteItem
            spyViewModel.getList(noteItem)
            callback.scrollToItem(simpleClick = false, p = 0, list = optimalList)


            spyViewModel.onClickAdd(simpleClick = true)
            spyViewModel.callback
            callback.isDialogOpen
            spyViewModel.noteState
            noteState.isEdit
            spyViewModel.callback
            callback.getEnterText()
            spyViewModel.callback
            callback.clearEnterText()
            val secondRollItem = RollItem(position = size, text = enterText)
            spyViewModel.noteItem
            noteItem.list
            normalList.size
            spyViewModel.inputControl
            inputControl.onRollAdd(p = size, valueTo = secondRollItem.toJson())
            spyViewModel.noteItem
            noteItem.list
            normalList.add(size, secondRollItem)
            spyViewModel.callback
            spyViewModel.noteItem
            spyViewModel.inputControl
            inputControl.access
            callback.onBindingInput(noteItem, access)
            spyViewModel.noteItem
            spyViewModel.getList(noteItem)
            callback.scrollToItem(simpleClick = true, p = size, list = optimalList)
        }
    }

    @Test fun onClickItemCheck() {
        //        val p = Random.nextInt()
        //        val noteState = mockk<NoteState>(relaxUnitFun = true)
        //        val noteItem = mockk<NoteItem.Roll>(relaxUnitFun = true)
        //
        //        val correctPosition = Random.nextInt()
        //        val optimalList = mockk<MutableList<RollItem>>()
        //        val normalList = mockk<MutableList<RollItem>>()
        //        val check = Random.nextInt()
        //        val size = Random.nextInt()
        //
        //        every { spyViewModel.getCorrectPosition(p, noteItem) } returns correctPosition
        //        every { spyViewModel.getList(noteItem) } returns optimalList
        //        every { spyViewModel.cacheData() } returns Unit
        //        every { noteItem.getCheck() } returns check
        //        every { noteItem.list } returns normalList
        //        every { normalList.size } returns size
        //
        //        spyViewModel.noteState = noteState
        //        spyViewModel.noteItem = noteItem
        //
        //        every { noteState.isEdit } returns true
        //        spyViewModel.onClickItemCheck(p)
        //
        //        every { noteState.isEdit } returns false
        //        spyViewModel.isVisible = false
        //        spyViewModel.onClickItemCheck(p)
        //
        //        spyViewModel.isVisible = true
        //        spyViewModel.onClickItemCheck(p)
        //
        //        coVerifyOrder {
        //            spyViewModel.noteState = noteState
        //            spyViewModel.noteItem = noteItem
        //
        //            spyViewModel.onClickItemCheck(p)
        //            spyViewModel.noteState
        //            noteState.isEdit
        //
        //
        //            spyViewModel.isVisible = false
        //            spyViewModel.onClickItemCheck(p)
        //            spyViewModel.noteState
        //            noteState.isEdit
        //            spyViewModel.noteItem
        //            spyViewModel.getCorrectPosition(p, noteItem)
        //            spyViewModel.noteItem
        //            noteItem.onItemCheck(correctPosition)
        //            spyViewModel.cacheData()
        //
        //            spyViewModel.callback
        //            spyViewModel.noteItem
        //            spyViewModel.getList(noteItem)
        //            callback.notifyItemRemoved(optimalList, p)
        //
        //            spyViewModel.noteItem
        //            spyViewModel.callback
        //            noteItem.getCheck()
        //            noteItem.list
        //            normalList.size
        //            callback.updateProgress(check, size)
        //
        //            spyViewModel.noteItem
        //            interactor.updateRollCheck(noteItem, correctPosition)
        //
        //
        //            spyViewModel.isVisible = true
        //            spyViewModel.onClickItemCheck(p)
        //            spyViewModel.noteState
        //            noteState.isEdit
        //            spyViewModel.noteItem
        //            spyViewModel.getCorrectPosition(p, noteItem)
        //            spyViewModel.noteItem
        //            noteItem.onItemCheck(correctPosition)
        //            spyViewModel.cacheData()
        //
        //            spyViewModel.callback
        //            spyViewModel.noteItem
        //            spyViewModel.getList(noteItem)
        //            callback.notifyItemChanged(optimalList, p)
        //
        //            spyViewModel.noteItem
        //            spyViewModel.callback
        //            noteItem.getCheck()
        //            noteItem.list
        //            normalList.size
        //            callback.updateProgress(check, size)
        //
        //            spyViewModel.noteItem
        //            interactor.updateRollCheck(noteItem, correctPosition)
        //        }
        TODO()
    }

    @Test fun onLongClickItemCheck() {
        //        val noteState = mockk<NoteState>(relaxUnitFun = true)
        //        val noteItem = mockk<NoteItem.Roll>(relaxUnitFun = true)
        //
        //        val isCheck = Random.nextBoolean()
        //        val optimalList = mockk<MutableList<RollItem>>()
        //        val normalList = mockk<MutableList<RollItem>>()
        //        val check = Random.nextInt()
        //        val size = Random.nextInt()
        //
        //        every { noteItem.onItemLongCheck() } returns isCheck
        //        every { spyViewModel.cacheData() } returns Unit
        //        every { spyViewModel.getList(noteItem) } returns optimalList
        //        every { noteItem.getCheck() } returns check
        //        every { noteItem.list } returns normalList
        //        every { normalList.size } returns size
        //        every { spyViewModel.notifyListByVisible() } returns Unit
        //
        //        spyViewModel.noteState = noteState
        //        spyViewModel.noteItem = noteItem
        //
        //        every { noteState.isEdit } returns true
        //        spyViewModel.onLongClickItemCheck()
        //
        //        every { noteState.isEdit } returns false
        //        spyViewModel.isVisible = false
        //        spyViewModel.onLongClickItemCheck()
        //
        //        spyViewModel.isVisible = true
        //        spyViewModel.onLongClickItemCheck()
        //
        //        coVerifyOrder {
        //            spyViewModel.noteState = noteState
        //            spyViewModel.noteItem = noteItem
        //
        //            spyViewModel.onLongClickItemCheck()
        //            spyViewModel.noteState
        //            noteState.isEdit
        //
        //
        //            spyViewModel.isVisible = false
        //            spyViewModel.onLongClickItemCheck()
        //            spyViewModel.noteState
        //            noteState.isEdit
        //            spyViewModel.noteItem
        //            noteItem.onItemLongCheck()
        //            spyViewModel.cacheData()
        //
        //            spyViewModel.callback
        //            callback.changeCheckToggle(state = true)
        //            spyViewModel.noteItem
        //            spyViewModel.getList(noteItem)
        //            callback.notifyDataRangeChanged(optimalList)
        //            callback.changeCheckToggle(state = false)
        //            spyViewModel.noteItem
        //            noteItem.getCheck()
        //            noteItem.list
        //            normalList.size
        //            callback.updateProgress(check, size)
        //
        //            spyViewModel.notifyListByVisible()
        //            spyViewModel.noteItem
        //            interactor.updateRollCheck(noteItem, isCheck)
        //
        //
        //            spyViewModel.isVisible = true
        //            spyViewModel.onLongClickItemCheck()
        //            spyViewModel.noteState
        //            noteState.isEdit
        //            spyViewModel.noteItem
        //            noteItem.onItemLongCheck()
        //            spyViewModel.cacheData()
        //
        //            spyViewModel.callback
        //            callback.changeCheckToggle(state = true)
        //            spyViewModel.noteItem
        //            spyViewModel.getList(noteItem)
        //            callback.notifyDataRangeChanged(optimalList)
        //            callback.changeCheckToggle(state = false)
        //            spyViewModel.noteItem
        //            noteItem.getCheck()
        //            noteItem.list
        //            normalList.size
        //            callback.updateProgress(check, size)
        //
        //            spyViewModel.noteItem
        //            interactor.updateRollCheck(noteItem, isCheck)
        //        }
        TODO()
    }


    @Test fun onResultColorDialog() = fastTest.onResultColorDialog(mockk())

    @Test fun onResultRankDialog() = fastTest.onResultRankDialog(mockk())

    @Test fun onResultDateDialog() = fastTest.onResultDateDialog()

    @Test fun onResultDateDialogClear() = fastTest.onResultDateDialogClear(mockk(), mockk())

    @Test fun onResultTimeDialog() = fastTest.onResultTimeDialog(mockk(), mockk())

    @Test fun onResultConvertDialog() = fastTest.onResultConvertDialog(mockk())


    @Test fun onReceiveUnbindNote() = fastTest.onReceiveUnbindNote(mockk(), mockk())


    @Test fun onMenuRestore() = fastTest.onMenuRestore(mockk())

    @Test fun onMenuRestoreOpen() = fastTest.onMenuRestoreOpen(mockk())

    @Test fun onMenuClear() = fastTest.onMenuClear(mockk())


    @Test fun onMenuUndo() = fastTest.onMenuUndo()

    @Test fun onMenuRedo() = fastTest.onMenuRedo()

    @Test fun onMenuUndoRedo() = fastTest.onMenuUndoRedo(mockk())

    @Test fun onMenuUndoRedoRank() = fastTest.onMenuUndoRedoRank(mockk(relaxUnitFun = true))

    @Test fun onMenuUndoRedoColor() = fastTest.onMenuUndoRedoColor(mockk())

    @Test fun onMenuUndoRedoName() = fastTest.onMenuUndoRedoName()

    @Test fun onMenuUndoRedoRoll() {
        TODO()
    }

    @Test fun onMenuUndoRedoAdd() {
        TODO()
    }

    @Test fun onMenuUndoRedoRemove() {
        TODO()
    }

    @Test fun onMenuUndoRedoMove() {
        TODO()
    }

    @Test fun onMenuRank() = fastTest.onMenuRank(mockk())

    @Test fun onMenuColor() = fastTest.onMenuColor(mockk())

    @Test fun onMenuSave() {
        TODO()
    }

    @Test fun onMenuNotification() = fastTest.onMenuNotification(mockk())

    @Test fun onMenuBind() = fastTest.onMenuBind(mockk(), mockk())

    @Test fun onMenuConvert() = fastTest.onMenuConvert()

    @Test fun onMenuDelete() = fastTest.onMenuDelete(mockk())

    @Test fun onMenuEdit() = fastTest.onMenuEdit()

    @Test fun setupEditMode() {
        TODO()
    }


    @Test fun onResultSaveControl() = fastTest.onResultSaveControl()

    @Test fun onInputTextChange() = fastTest.onInputTextChange(mockk())

    @Test fun onInputRollChange() {
        val p = Random.nextInt()
        val text = nextString()

        val noteItem = mockk<NoteItem.Roll>()
        val list = MutableList<RollItem>(size = 5) { mockk() }
        val correctPosition = list.indices.random()
        val item = list[correctPosition]
        val newList = mockk<MutableList<RollItem>>()
        val access = mockk<InputControl.Access>()

        spyViewModel.noteItem = noteItem

        every { spyViewModel.getCorrectPosition(p, noteItem) } returns -1
        every { noteItem.list } returns list
        every { spyViewModel.getList(noteItem) } returns newList
        every { inputControl.access } returns access

        spyViewModel.onInputRollChange(p, text)

        every { spyViewModel.getCorrectPosition(p, noteItem) } returns correctPosition
        every { item.text = text } returns Unit

        spyViewModel.onInputRollChange(p, text)

        verifyOrder {
            spyViewModel.getCorrectPosition(p, noteItem)
            noteItem.list

            spyViewModel.getList(noteItem)
            callback.setList(newList)
            inputControl.access
            callback.onBindingInput(noteItem, access)


            spyViewModel.getCorrectPosition(p, noteItem)
            noteItem.list
            item.text = text

            spyViewModel.getList(noteItem)
            callback.setList(newList)
            inputControl.access
            callback.onBindingInput(noteItem, access)
        }
    }

    @Test fun onRollActionNext() {
        viewModel.onRollActionNext()

        verifySequence {
            callback.onFocusEnter()
        }
    }


    @Test fun onTouchAction() {
        val inAction = Random.nextBoolean()

        viewModel.onTouchAction(inAction)

        verifySequence {
            callback.setTouchAction(inAction)
        }
    }

    @Test fun onTouchGetDrag() {
        val noteState = mockk<NoteState>()

        viewModel.noteState = noteState

        every { noteState.isEdit } returns false
        assertFalse(viewModel.onTouchGetDrag())

        every { noteState.isEdit } returns true
        assertTrue(viewModel.onTouchGetDrag())

        verifySequence {
            noteState.isEdit

            noteState.isEdit
            callback.hideKeyboard()
        }
    }

    @Test fun onTouchGetSwipe() {
        val noteState = mockk<NoteState>()

        viewModel.noteState = noteState

        every { noteState.isEdit } returns false
        assertFalse(viewModel.onTouchGetSwipe())

        every { noteState.isEdit } returns true
        assertTrue(viewModel.onTouchGetSwipe())

        verifySequence {
            noteState.isEdit
            noteState.isEdit
        }
    }

    @Test fun onTouchSwiped() {
        val p = Random.nextInt()
        val correctPosition = Random.nextInt()
        val noteItem = mockk<NoteItem.Roll>()
        val list = mockk<MutableList<RollItem>>()
        val newList = mockk<MutableList<RollItem>>()
        val item = mockk<RollItem>()
        val itemJson = nextString()

        val inputAccess = mockk<InputControl.Access>()

        spyViewModel.noteItem = noteItem

        FastMock.listExtension()

        every { spyViewModel.getCorrectPosition(p, noteItem) } returns correctPosition
        every { noteItem.list } returns list

        every { list.validRemoveAt(correctPosition) } returns null

        spyViewModel.onTouchSwiped(p)

        every { list.validRemoveAt(correctPosition) } returns item
        every { item.toJson() } returns itemJson
        every { inputControl.access } returns inputAccess
        every { spyViewModel.getList(noteItem) } returns newList

        spyViewModel.onTouchSwiped(p)

        verifyOrder {
            spyViewModel.getCorrectPosition(p, noteItem)
            noteItem.list
            list.validRemoveAt(correctPosition)

            spyViewModel.getCorrectPosition(p, noteItem)
            noteItem.list
            list.validRemoveAt(correctPosition)
            item.toJson()
            inputControl.onRollRemove(correctPosition, itemJson)

            inputControl.access
            callback.onBindingInput(noteItem, inputAccess)
            spyViewModel.getList(noteItem)
            callback.notifyItemRemoved(newList, p)
        }
    }

    @Test fun onTouchMove() {
        val from = Random.nextInt()
        val correctFrom = Random.nextInt()
        val to = Random.nextInt()
        val correctTo = Random.nextInt()

        val noteItem = mockk<NoteItem.Roll>()
        val list = mockk<MutableList<RollItem>>()
        val newList = mockk<MutableList<RollItem>>()

        spyViewModel.noteItem = noteItem

        FastMock.listExtension()

        every { spyViewModel.getCorrectPosition(from, noteItem) } returns correctFrom
        every { spyViewModel.getCorrectPosition(to, noteItem) } returns correctTo

        every { noteItem.list } returns list
        every { list.move(correctFrom, correctTo) } returns Unit

        every { spyViewModel.getList(noteItem) } returns newList

        assertTrue(spyViewModel.onTouchMove(from, to))

        verifySequence {
            spyViewModel.noteItem = noteItem
            spyViewModel.onTouchMove(from, to)

            spyViewModel.noteItem
            spyViewModel.getCorrectPosition(from, noteItem)
            spyViewModel.noteItem
            spyViewModel.getCorrectPosition(to, noteItem)

            spyViewModel.noteItem
            noteItem.list
            list.move(correctFrom, correctTo)

            spyViewModel.callback
            spyViewModel.noteItem
            spyViewModel.getList(noteItem)
            callback.notifyItemMoved(newList, from, to)

            spyViewModel.callback
            callback.hideKeyboard()
        }
    }

    @Test fun onTouchMoveResult() {
        val from = Random.nextInt()
        val correctFrom = Random.nextInt()
        val to = Random.nextInt()
        val correctTo = Random.nextInt()

        val noteItem = mockk<NoteItem.Roll>()
        val inputAccess = mockk<InputControl.Access>()

        spyViewModel.noteItem = noteItem

        every { spyViewModel.getCorrectPosition(from, noteItem) } returns correctFrom
        every { spyViewModel.getCorrectPosition(to, noteItem) } returns correctTo
        every { inputControl.access } returns inputAccess

        spyViewModel.onTouchMoveResult(from, to)

        verifyOrder {
            spyViewModel.getCorrectPosition(from, noteItem)
            spyViewModel.getCorrectPosition(to, noteItem)

            inputControl.onRollMove(correctFrom, correctTo)

            inputControl.access
            callback.onBindingInput(noteItem, inputAccess)
        }
    }


    @Test fun getCorrectPosition() {
        TODO()
    }

    @Test fun hide() {
        TODO()
    }

    @Test fun getList() {
        TODO()
    }

    @Test fun notifyListByVisible() {
        TODO()
    }
}