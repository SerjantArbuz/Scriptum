package sgtmelon.scriptum.test.content.note

import sgtmelon.extension.getCalendarWithAdd
import sgtmelon.extension.getText
import sgtmelon.scriptum.data.room.entity.RollEntity
import sgtmelon.scriptum.domain.model.annotation.Color
import sgtmelon.scriptum.domain.model.annotation.Sort
import sgtmelon.scriptum.domain.model.annotation.Theme
import sgtmelon.scriptum.domain.model.item.NoteItem
import sgtmelon.scriptum.domain.model.key.MainPage
import sgtmelon.scriptum.domain.model.key.NoteType
import sgtmelon.scriptum.presentation.adapter.NoteAdapter
import sgtmelon.scriptum.test.ParentUiTest

/**
 * Parent class for tests of [NoteAdapter]
 */
abstract class ParentNoteContentTest(private val page: MainPage) : ParentUiTest() {

    open fun colorTextLight() = startColorTest(NoteType.TEXT, Theme.LIGHT)

    open fun colorTextDark() = startColorTest(NoteType.TEXT, Theme.DARK)

    open fun colorRollLight() = startColorTest(NoteType.ROLL, Theme.LIGHT)

    open fun colorRollDark() = startColorTest(NoteType.ROLL, Theme.DARK)

    private fun startColorTest(type: NoteType, @Theme theme: Int) {
        preferenceRepo.theme = theme
        preferenceRepo.sort = Sort.COLOR

        onAssertList(ArrayList<NoteItem>().also { list ->
            Color.list.forEach {
                val note = when(type) {
                    NoteType.TEXT -> data.textNote.copy(color = it)
                    NoteType.ROLL -> data.rollNote.copy(color = it)
                }

                list.add(when(page) {
                    MainPage.RANK -> throw IllegalAccessException(PAGE_ERROR_TEXT)
                    MainPage.NOTES ->when (type) {
                        NoteType.TEXT -> data.insertText(note)
                        NoteType.ROLL -> data.insertRoll(note)
                    }
                    MainPage.BIN -> when (type) {
                        NoteType.TEXT -> data.insertTextToBin(note)
                        NoteType.ROLL -> data.insertRollToBin(note)
                    }
                })
            }
        })
    }


    open fun timeCreateText() = startTimeTest(NoteType.TEXT, Sort.CREATE)

    open fun timeCreateRoll() = startTimeTest(NoteType.ROLL, Sort.CREATE)

    open fun timeChangeText() = startTimeTest(NoteType.TEXT, Sort.CHANGE)

    open fun timeChangeRoll() = startTimeTest(NoteType.ROLL, Sort.CHANGE)

    private fun startTimeTest(type: NoteType, @Sort sort: Int) {
        preferenceRepo.sort = sort

        onAssertList(ArrayList<NoteItem>().also { list ->
            lastArray.forEach {
                val time = getCalendarWithAdd(it).getText()

                val note = when (type) {
                    NoteType.TEXT -> when (sort) {
                        Sort.CREATE -> data.textNote.copy(create = time)
                        Sort.CHANGE -> data.textNote.copy(change = time)
                        else -> throw IllegalAccessException(SORT_ERROR_TEXT)
                    }
                    NoteType.ROLL -> when (sort) {
                        Sort.CREATE -> data.rollNote.copy(create = time)
                        Sort.CHANGE -> data.rollNote.copy(change = time)
                        else -> throw IllegalAccessException(SORT_ERROR_TEXT)
                    }
                }

                list.add(when (page) {
                    MainPage.RANK -> throw IllegalAccessException(PAGE_ERROR_TEXT)
                    MainPage.NOTES -> when (type) {
                        NoteType.TEXT -> data.insertText(note)
                        NoteType.ROLL -> data.insertRoll(note)
                    }
                    MainPage.BIN -> when (type) {
                        NoteType.TEXT -> data.insertTextToBin(note)
                        NoteType.ROLL -> data.insertRollToBin(note)
                    }
                })
            }
        })
    }


    open fun rollRow1() = startRowTest(count = 1)

    open fun rollRow2() = startRowTest(count = 2)

    open fun rollRow3() = startRowTest(count = 3)

    open fun rollRow4() = startRowTest(count = 4)

    private fun startRowTest(count: Int) {
        val rollList = ArrayList<RollEntity>()

        (0 until count).forEach {
            rollList.add(data.rollEntity.apply {
                position = it
                text = "$it | $text"
            })
        }

        onAssertList( ArrayList<NoteItem>().apply {
            add(when (page) {
                MainPage.RANK -> throw IllegalAccessException(PAGE_ERROR_TEXT)
                MainPage.NOTES -> data.insertRoll(data.rollNote, rollList)
                MainPage.BIN -> data.insertRollToBin(data.rollNote, rollList)
            })
        })
    }


    open fun progressIndicator1() = startProgressTest(check = 10, size = 99)

    open fun progressIndicator2() = startProgressTest(check = 10, size = 100)

    open fun progressIndicator3() = startProgressTest(check = 99, size = 99)

    open fun progressIndicator4() = startProgressTest(check = 100, size = 100)

    private fun startProgressTest(check: Int, size: Int) {
        if (size < check) throw IllegalAccessException(OVERFLOW_ERROR_TEXT)

        val rollList = ArrayList<RollEntity>()

        (0 until size).forEach {
            rollList.add(data.rollEntity.apply {
                position = it
                text = "$it | $text"
            })
        }

        var done = 0
        for (entity in rollList) {
            entity.isCheck = true

            if (++done == check) break
        }

        onAssertList(ArrayList<NoteItem>().apply {
            add(when (page) {
                MainPage.RANK -> throw IllegalAccessException(PAGE_ERROR_TEXT)
                MainPage.NOTES -> data.insertRoll(data.rollNote, rollList)
                MainPage.BIN -> data.insertRollToBin(data.rollNote, rollList)
            })
        })
    }


    open fun rankSort() {
        preferenceRepo.sort = Sort.RANK

        onAssertList(ArrayList<NoteItem>().apply {
            add(when (page) {
                MainPage.RANK -> throw IllegalAccessException(PAGE_ERROR_TEXT)
                MainPage.NOTES -> data.insertText(data.textNote)
                MainPage.BIN -> data.insertTextToBin(data.textNote)
            })
        })

        TODO(reason = "#TEST write test")
    }

    open fun rankTextCancel() {
        TODO(reason = "#TEST write test")
    }

    open fun rankRollCancel() {
        TODO(reason = "#TEST write test")
    }

    private fun onAssertList(list: List<NoteItem>) {
        launch {
            mainScreen {
                when (page) {
                    MainPage.RANK -> throw IllegalAccessException(PAGE_ERROR_TEXT)
                    MainPage.NOTES -> notesScreen {
                        list.forEachIndexed { p, model -> onAssertItem(model, p) }
                    }
                    MainPage.BIN -> binScreen {
                        list.forEachIndexed { p, model -> onAssertItem(model, p) }
                    }
                }
            }
        }
    }

    companion object {
        private const val PAGE_ERROR_TEXT = "This class test only screens with [NoteAdapter]"
        private const val OVERFLOW_ERROR_TEXT = "Check count must be < than size"
        private const val SORT_ERROR_TEXT = "Wrong sort type"

        private const val LAST_HOUR = -60
        private const val LAST_DAY = LAST_HOUR * 24
        private const val LAST_MONTH = LAST_DAY * 30
        private const val LAST_YEAR = LAST_MONTH * 12

        val lastArray = arrayListOf(LAST_HOUR, LAST_DAY, LAST_MONTH, LAST_YEAR)
    }

}