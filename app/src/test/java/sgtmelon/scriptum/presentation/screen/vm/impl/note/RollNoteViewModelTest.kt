package sgtmelon.scriptum.presentation.screen.vm.impl.note

import android.os.Bundle
import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.*
import org.junit.Test
import sgtmelon.extension.beforeNow
import sgtmelon.extension.getCalendar
import sgtmelon.extension.nextString
import sgtmelon.scriptum.FastMock
import sgtmelon.scriptum.ParentViewModelTest
import sgtmelon.scriptum.domain.interactor.callback.IBindInteractor
import sgtmelon.scriptum.domain.interactor.callback.note.IRollNoteInteractor
import sgtmelon.scriptum.domain.model.data.NoteData
import sgtmelon.scriptum.domain.model.item.NoteItem
import sgtmelon.scriptum.domain.model.item.RollItem
import sgtmelon.scriptum.domain.model.state.IconState
import sgtmelon.scriptum.domain.model.state.NoteState
import sgtmelon.scriptum.extension.move
import sgtmelon.scriptum.extension.removeAtOrNull
import sgtmelon.scriptum.presentation.control.note.input.IInputControl
import sgtmelon.scriptum.presentation.control.note.input.InputControl
import sgtmelon.scriptum.presentation.screen.ui.callback.note.INoteConnector
import sgtmelon.scriptum.presentation.screen.ui.callback.note.roll.IRollNoteFragment
import java.util.*
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
    @MockK lateinit var iconState: IconState

    private val viewModel by lazy { RollNoteViewModel(application) }

    override fun setUp() {
        super.setUp()

        viewModel.setCallback(callback)
        viewModel.setParentCallback(parentCallback)
        viewModel.setInteractor(interactor, bindInteractor)

        viewModel.inputControl = inputControl
        viewModel.iconState = iconState

        assertEquals(NoteData.Default.ID, viewModel.id)
        assertEquals(NoteData.Default.COLOR, viewModel.color)
        assertTrue(viewModel.rankDialogItemArray.isEmpty())
    }

    @Test override fun onDestroy() {
        assertNotNull(viewModel.callback)
        assertNotNull(viewModel.parentCallback)

        viewModel.onDestroy()

        assertNull(viewModel.callback)
        assertNull(viewModel.parentCallback)

        verifySequence {
            interactor.onDestroy()

            TODO()
        }
    }


    @Test fun onSetup() {
        TODO()
    }

    @Test fun isNoteInitialized() {
        TODO()
    }


    @Test fun onSaveData() {
        val id = Random.nextLong()
        val color = Random.nextInt()
        val bundle = mockk<Bundle>()

        every { bundle.putLong(NoteData.Intent.ID, id) } returns Unit
        every { bundle.putInt(NoteData.Intent.COLOR, color) } returns Unit

        viewModel.id = id
        viewModel.color = color
        viewModel.onSaveData(bundle)

        verifySequence {
            bundle.putLong(NoteData.Intent.ID, id)
            bundle.putInt(NoteData.Intent.COLOR, color)
        }
    }

    @Test fun onResume() {
        TODO()
    }

    @Test fun onPause() {
        TODO()
    }


    @Test fun onClickBackArrow() {
        TODO()
    }

    @Test fun onPressBack() {
        TODO()
    }


    @Test fun onClickVisible() {
        TODO()
    }

    @Test fun onUpdateInfo() {
        TODO()
    }

    @Test fun onEditorClick() {
        TODO()
    }

    @Test fun onClickAdd() {
        TODO()
    }

    @Test fun onClickItemCheck() {
        TODO()
    }

    @Test fun onLongClickItemCheck() {
        TODO()
    }


    @Test fun onResultColorDialog() {
        val noteItem = mockk<NoteItem.Roll>()
        val oldColor = Random.nextInt()
        val newColor = Random.nextInt()
        val access = mockk<InputControl.Access>()

        every { noteItem.color } returns oldColor
        every { noteItem.color = newColor } returns Unit
        every { inputControl.access } returns access

        viewModel.noteItem = noteItem
        viewModel.onResultColorDialog(newColor)

        verifySequence {
            noteItem.color
            inputControl.onColorChange(oldColor, newColor)
            noteItem.color = newColor

            inputControl.access
            callback.onBindingInput(noteItem, access)
            callback.tintToolbar(newColor)
        }
    }

    @Test fun onResultRankDialog() {
        val noteItem = mockk<NoteItem.Roll>()

        val oldRankId = Random.nextLong()
        val oldRankPs = Random.nextInt()
        val newRankId = Random.nextLong()
        val newRankPs = Random.nextInt()

        val access = mockk<InputControl.Access>()

        coEvery { interactor.getRankId(newRankPs) } returns newRankId
        every { noteItem.rankId } returns oldRankId
        every { noteItem.rankPs } returns oldRankPs
        every { noteItem.rankId = newRankId } returns Unit
        every { noteItem.rankPs = newRankPs } returns Unit
        every { inputControl.access } returns access

        viewModel.noteItem = noteItem
        viewModel.onResultRankDialog(newRankPs)

        coVerifySequence {
            interactor.getRankId(newRankPs)

            noteItem.rankId
            noteItem.rankPs
            inputControl.onRankChange(oldRankId, oldRankPs, newRankId, newRankPs)

            noteItem.rankId = newRankId
            noteItem.rankPs = newRankPs

            inputControl.access
            callback.onBindingInput(noteItem, access)
            callback.onBindingNote(noteItem)
        }
    }

    @Test fun onResultDateDialog() {
        val calendar = mockk<Calendar>()
        val dateList = mockk<List<String>>()

        coEvery { interactor.getDateList() } returns dateList

        viewModel.onResultDateDialog(calendar)

        coVerifySequence {
            interactor.getDateList()
            callback.showTimeDialog(calendar, dateList)
        }
    }

    @Test fun onResultDateDialogClear() {
        val noteItem = mockk<NoteItem.Roll>()
        val restoreItem = mockk<NoteItem.Roll>()

        every { noteItem.clearAlarm() } returns noteItem
        mockDeepCopy(noteItem)

        viewModel.noteItem = noteItem
        viewModel.restoreItem = restoreItem

        viewModel.onResultDateDialogClear()

        coVerifySequence {
            interactor.clearDate(noteItem)
            bindInteractor.notifyInfoBind(callback)

            noteItem.clearAlarm()
            verifyDeepCopy(noteItem)

            callback.onBindingNote(noteItem)
        }

        assertEquals(noteItem, viewModel.restoreItem)
    }

    @Test fun onResultTimeDialog() {
        val calendar = mockk<Calendar>()
        val noteItem = mockk<NoteItem.Roll>()
        val restoreItem = mockk<NoteItem.Roll>()

        FastMock.timeExtension()
        mockDeepCopy(noteItem)

        viewModel.noteItem = noteItem
        viewModel.restoreItem = restoreItem

        every { calendar.beforeNow() } returns true
        viewModel.onResultTimeDialog(calendar)

        every { calendar.beforeNow() } returns false
        viewModel.onResultTimeDialog(calendar)

        coVerifySequence {
            calendar.beforeNow()

            calendar.beforeNow()
            interactor.setDate(noteItem, calendar)
            verifyDeepCopy(noteItem)
            callback.onBindingNote(noteItem)
            bindInteractor.notifyInfoBind(callback)
        }

        assertEquals(noteItem, viewModel.restoreItem)
    }

    @Test fun onResultConvertDialog() {
        val noteItem = mockk<NoteItem.Roll>()

        viewModel.noteItem = noteItem
        viewModel.onResultConvertDialog()

        coVerifySequence {
            interactor.convertNote(noteItem)
            parentCallback.onConvertNote()
        }
    }


    @Test fun onReceiveUnbindNote() {
        viewModel.onReceiveUnbindNote(Random.nextLong())

        val id = Random.nextLong()
        val noteItem = mockk<NoteItem.Roll>()
        val restoreItem = mockk<NoteItem.Roll>()

        every { noteItem.isStatus = false } returns Unit
        every { restoreItem.isStatus = false } returns Unit

        viewModel.id = id
        viewModel.noteItem = noteItem
        viewModel.restoreItem = restoreItem

        viewModel.onReceiveUnbindNote(id)

        verifySequence {
            noteItem.isStatus = false
            restoreItem.isStatus = false

            callback.onBindingNote(noteItem)
        }
    }


    @Test fun onMenuRestore() {
        val noteItem = mockk<NoteItem.Roll>()

        viewModel.noteItem = noteItem
        viewModel.onMenuRestore()

        coVerifySequence {
            interactor.restoreNote(noteItem)
            parentCallback.finish()
        }
    }

    @Test fun onMenuRestoreOpen() {
        TODO()
    }

    @Test fun onMenuClear() {
        val noteItem = mockk<NoteItem.Roll>()

        viewModel.noteItem = noteItem
        viewModel.onMenuClear()

        coVerifySequence {
            interactor.clearNote(noteItem)
            parentCallback.finish()
        }
    }


    @Test fun onMenuUndo() {
        TODO()
    }

    @Test fun onMenuRedo() {
        TODO()
    }

    @Test fun onMenuRank() {
        val noteItem = mockk<NoteItem.Roll>()
        val rankPs = Random.nextInt()

        val noteState = mockk<NoteState>()

        every { noteItem.rankPs } returns rankPs

        viewModel.noteItem = noteItem
        viewModel.noteState = noteState

        every { noteState.isEdit } returns false
        viewModel.onMenuRank()

        every { noteState.isEdit } returns true
        viewModel.onMenuRank()

        verifySequence {
            noteState.isEdit

            noteState.isEdit
            noteItem.rankPs
            callback.showRankDialog(check = rankPs + 1)
        }
    }

    @Test fun onMenuColor() {
        val noteItem = mockk<NoteItem.Roll>()
        val color = Random.nextInt()
        val theme = Random.nextInt()

        val noteState = mockk<NoteState>()

        every { noteItem.color } returns color
        every { interactor.theme } returns theme

        viewModel.noteItem = noteItem
        viewModel.noteState = noteState

        every { noteState.isEdit } returns false
        viewModel.onMenuColor()

        every { noteState.isEdit } returns true
        viewModel.onMenuColor()

        verifySequence {
            noteState.isEdit

            noteState.isEdit
            noteItem.color
            interactor.theme
            callback.showColorDialog(color, theme)
        }
    }

    @Test fun onMenuSave() {
        TODO()
    }

    @Test fun onMenuNotification() {
        val noteItem = mockk<NoteItem.Roll>()
        val alarmDate = Random.nextString()
        val haveAlarm = Random.nextBoolean()
        val calendar = mockk<Calendar>()

        val noteState = mockk<NoteState>()

        every { noteItem.alarmDate } returns alarmDate
        every { noteItem.haveAlarm() } returns haveAlarm

        FastMock.timeExtension()
        every { alarmDate.getCalendar() } returns calendar

        viewModel.noteItem = noteItem
        viewModel.noteState = noteState

        every { noteState.isEdit } returns false
        viewModel.onMenuNotification()

        every { noteState.isEdit } returns true
        viewModel.onMenuNotification()

        verifySequence {
            noteState.isEdit

            noteItem.alarmDate
            alarmDate.getCalendar()
            noteItem.haveAlarm()
            callback.showDateDialog(calendar, haveAlarm)

            noteState.isEdit
        }
    }

    @Test fun onMenuBind() {
        val noteItem = mockk<NoteItem.Roll>()
        val restoreItem = mockk<NoteItem.Roll>()

        val noteState = mockk<NoteState>()

        every { noteItem.switchStatus() } returns noteItem
        mockDeepCopy(noteItem)

        viewModel.noteItem = noteItem
        viewModel.restoreItem = restoreItem
        viewModel.noteState = noteState

        every { callback.isDialogOpen } returns true
        every { noteState.isEdit } returns true
        viewModel.onMenuBind()

        assertEquals(restoreItem, viewModel.restoreItem)

        every { callback.isDialogOpen } returns true
        every { noteState.isEdit } returns false
        viewModel.onMenuBind()

        assertEquals(restoreItem, viewModel.restoreItem)

        every { callback.isDialogOpen } returns false
        every { noteState.isEdit } returns true
        viewModel.onMenuBind()

        assertEquals(restoreItem, viewModel.restoreItem)

        every { callback.isDialogOpen } returns false
        every { noteState.isEdit } returns false
        viewModel.onMenuBind()

        coVerifySequence {
            callback.isDialogOpen
            callback.isDialogOpen
            callback.isDialogOpen
            noteState.isEdit

            callback.isDialogOpen
            noteState.isEdit
            noteItem.switchStatus()
            verifyDeepCopy(noteItem)
            noteState.isEdit
            callback.onBindingEdit(noteItem, isEditMode = false)
            interactor.updateNote(noteItem, updateBind = true)
        }

        assertEquals(noteItem, viewModel.restoreItem)
    }

    @Test fun onMenuConvert() {
        val noteState = mockk<NoteState>()

        viewModel.noteState = noteState

        every { noteState.isEdit } returns false
        viewModel.onMenuConvert()

        every { noteState.isEdit } returns true
        viewModel.onMenuConvert()

        verifySequence {
            noteState.isEdit
            callback.showConvertDialog()

            noteState.isEdit
        }
    }

    @Test fun onMenuDelete() {
        val noteItem = mockk<NoteItem.Roll>()
        val noteState = mockk<NoteState>()

        viewModel.noteItem = noteItem
        viewModel.noteState = noteState

        every { callback.isDialogOpen } returns true
        every { noteState.isEdit } returns true
        viewModel.onMenuDelete()

        every { callback.isDialogOpen } returns true
        every { noteState.isEdit } returns false
        viewModel.onMenuDelete()

        every { callback.isDialogOpen } returns false
        every { noteState.isEdit } returns true
        viewModel.onMenuDelete()

        every { callback.isDialogOpen } returns false
        every { noteState.isEdit } returns false
        viewModel.onMenuDelete()

        coVerifySequence {
            callback.isDialogOpen
            callback.isDialogOpen
            callback.isDialogOpen
            noteState.isEdit

            callback.isDialogOpen
            noteState.isEdit
            interactor.deleteNote(noteItem)
            bindInteractor.notifyInfoBind(callback)
            parentCallback.finish()
        }
    }

    @Test fun onMenuEdit() {
        TODO()
    }


    @Test fun onResultSaveControl() {
        TODO()
    }

    @Test fun onInputTextChange() {
        val noteItem = mockk<NoteItem.Roll>()
        val access = mockk<InputControl.Access>()

        every { inputControl.access } returns access

        viewModel.noteItem = noteItem
        viewModel.onInputTextChange()

        verifySequence {
            inputControl.access
            callback.onBindingInput(noteItem, access)
        }
    }

    @Test fun onInputRollChange() {
        val p = Random.nextInt()
        val text = Random.nextString()

        val noteItem = mockk<NoteItem.Roll>()
        val list = MutableList<RollItem>(size = 5) { mockk() }
        val correctPosition = list.indices.random()
        val item = list[correctPosition]
        val newList = mockk<MutableList<RollItem>>()
        val access = mockk<InputControl.Access>()

        viewModel.noteItem = noteItem

        mockkObject(RollNoteViewModel)

        every { RollNoteViewModel.getCorrectPosition(p, noteItem) } returns -1
        every { noteItem.list } returns list
        every { RollNoteViewModel.getList(noteItem) } returns newList
        every { inputControl.access } returns access

        viewModel.onInputRollChange(p, text)

        every { RollNoteViewModel.getCorrectPosition(p, noteItem) } returns correctPosition
        every { item.text = text } returns Unit

        viewModel.onInputRollChange(p, text)

        verifySequence {
            RollNoteViewModel.getCorrectPosition(p, noteItem)
            noteItem.list

            RollNoteViewModel.getList(noteItem)
            callback.setList(newList)
            inputControl.access
            callback.onBindingInput(noteItem, access)


            RollNoteViewModel.getCorrectPosition(p, noteItem)
            noteItem.list
            item.text = text

            RollNoteViewModel.getList(noteItem)
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
        val itemJson = Random.nextString()

        val inputAccess = mockk<InputControl.Access>()

        viewModel.noteItem = noteItem

        FastMock.listExtension()
        mockkObject(RollNoteViewModel)

        every { RollNoteViewModel.getCorrectPosition(p, noteItem) } returns correctPosition
        every { noteItem.list } returns list

        every { list.removeAtOrNull(correctPosition) } returns null

        viewModel.onTouchSwiped(p)

        every { list.removeAtOrNull(correctPosition) } returns item
        every { item.toJson() } returns itemJson
        every { inputControl.access } returns inputAccess
        every { RollNoteViewModel.getList(noteItem) } returns newList

        viewModel.onTouchSwiped(p)

        verifySequence {
            RollNoteViewModel.getCorrectPosition(p, noteItem)
            noteItem.list
            list.removeAtOrNull(correctPosition)

            RollNoteViewModel.getCorrectPosition(p, noteItem)
            noteItem.list
            list.removeAtOrNull(correctPosition)
            item.toJson()
            inputControl.onRollRemove(correctPosition, itemJson)

            inputControl.access
            callback.onBindingInput(noteItem, inputAccess)
            RollNoteViewModel.getList(noteItem)
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

        viewModel.noteItem = noteItem

        FastMock.listExtension()
        mockkObject(RollNoteViewModel)

        every { RollNoteViewModel.getCorrectPosition(from, noteItem) } returns correctFrom
        every { RollNoteViewModel.getCorrectPosition(to, noteItem) } returns correctTo

        every { noteItem.list } returns list
        every { list.move(correctFrom, correctTo) } returns Unit

        every { RollNoteViewModel.getList(noteItem) } returns newList

        assertTrue(viewModel.onTouchMove(from, to))

        verifySequence {
            RollNoteViewModel.getCorrectPosition(from, noteItem)
            RollNoteViewModel.getCorrectPosition(to, noteItem)

            noteItem.list
            list.move(correctFrom, correctTo)

            RollNoteViewModel.getList(noteItem)
        }
    }

    @Test fun onTouchMoveResult() {
        val from = Random.nextInt()
        val correctFrom = Random.nextInt()
        val to = Random.nextInt()
        val correctTo = Random.nextInt()

        val noteItem = mockk<NoteItem.Roll>()
        val inputAccess = mockk<InputControl.Access>()

        viewModel.noteItem = noteItem

        mockkObject(RollNoteViewModel)
        every { RollNoteViewModel.getCorrectPosition(from, noteItem) } returns correctFrom
        every { RollNoteViewModel.getCorrectPosition(to, noteItem) } returns correctTo
        every { inputControl.access } returns inputAccess

        viewModel.onTouchMoveResult(from, to)

        verifySequence {
            RollNoteViewModel.getCorrectPosition(from, noteItem)
            RollNoteViewModel.getCorrectPosition(to, noteItem)

            inputControl.onRollMove(correctFrom, correctTo)

            inputControl.access
            callback.onBindingInput(noteItem, inputAccess)
        }
    }



    @Test fun notifyListByVisible() {
        TODO()
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


    private fun mockDeepCopy(item: NoteItem.Roll) {
        val list = MutableList(size = 5) { mockk<RollItem>() }

        every { item.id } returns Random.nextLong()
        every { item.create } returns Random.nextString()
        every { item.change } returns Random.nextString()
        every { item.name } returns Random.nextString()
        every { item.text } returns Random.nextString()
        every { item.color } returns Random.nextInt()
        every { item.rankId } returns Random.nextLong()
        every { item.rankPs } returns Random.nextInt()
        every { item.isBin } returns Random.nextBoolean()
        every { item.isStatus } returns Random.nextBoolean()
        every { item.alarmId } returns Random.nextLong()
        every { item.alarmDate } returns Random.nextString()
        every { item.list } returns list

        list.forEach {
            every { it.copy(any(), any(), any(), any()) } returns it
        }

        every {
            item.deepCopy(
                    any(), any(), any(), any(), any(), any(),
                    any(), any(), any(), any(), any(), any(), any()
            )
        } returns item
    }

    private fun MockKVerificationScope.verifyDeepCopy(item: NoteItem.Roll) {
        item.id
        item.create
        item.change
        item.name
        item.text
        item.color
        item.rankId
        item.rankPs
        item.isBin
        item.isStatus
        item.alarmId
        item.alarmDate

        item.deepCopy(
                any(), any(), any(), any(), any(), any(),
                any(), any(), any(), any(), any(), any(), any()
        )
    }

}