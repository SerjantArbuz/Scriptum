package sgtmelon.scriptum.presentation.screen.vm.impl

import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.*
import org.junit.Test
import sgtmelon.extension.nextString
import sgtmelon.scriptum.ParentViewModelTest
import sgtmelon.scriptum.R
import sgtmelon.scriptum.TestData
import sgtmelon.scriptum.domain.interactor.callback.IBackupInteractor
import sgtmelon.scriptum.domain.interactor.callback.IPreferenceInteractor
import sgtmelon.scriptum.domain.interactor.callback.notification.ISignalInteractor
import sgtmelon.scriptum.domain.model.item.FileItem
import sgtmelon.scriptum.domain.model.key.PermissionResult
import sgtmelon.scriptum.domain.model.result.ExportResult
import sgtmelon.scriptum.domain.model.result.ImportResult
import sgtmelon.scriptum.domain.model.state.SignalState
import sgtmelon.scriptum.presentation.screen.ui.callback.IPreferenceFragment
import kotlin.random.Random

/**
 * Test fo [PreferenceViewModel].
 */
@ExperimentalCoroutinesApi
class PreferenceViewModelTest : ParentViewModelTest() {

    @MockK lateinit var interactor: IPreferenceInteractor
    @MockK lateinit var signalInteractor: ISignalInteractor
    @MockK lateinit var backupInteractor: IBackupInteractor
    @MockK lateinit var callback: IPreferenceFragment

    private val melodyList = TestData.Melody.melodyList
    private val fileList = TestData.Backup.fileList

    private val viewModel by lazy {
        PreferenceViewModel(interactor, signalInteractor, backupInteractor, callback)
    }
    private val spyViewModel by lazy { spyk(viewModel) }

    @Test override fun onDestroy() {
        assertNotNull(viewModel.callback)
        viewModel.onDestroy()
        assertNull(viewModel.callback)
    }


    @Test fun onSetup() {
        val themeSummary = Random.nextString()
        val sortSummary = Random.nextString()
        val defaultColorSummary = Random.nextString()
        val savePeriodSummary = Random.nextString()
        val repeatSummary = Random.nextString()
        val typeCheck = BooleanArray(size = 3) { Random.nextBoolean() }
        val signalSummary = Random.nextString()
        val volumeSummary = Random.nextString()

        coEvery { spyViewModel.setupMelody() } returns Unit
        coEvery { spyViewModel.setupBackup() } returns Unit

        every { interactor.getThemeSummary() } returns themeSummary
        every { interactor.getSortSummary() } returns sortSummary
        every { interactor.getDefaultColorSummary() } returns defaultColorSummary
        every { interactor.getSavePeriodSummary() } returns savePeriodSummary
        every { interactor.getRepeatSummary() } returns repeatSummary
        every { signalInteractor.typeCheck } returns typeCheck
        every { interactor.getSignalSummary(typeCheck) } returns signalSummary
        every { interactor.getVolumeSummary() } returns volumeSummary

        spyViewModel.onSetup()

        coVerifySequence {
            spyViewModel.onSetup()

            callback.apply {
                setupApp()
                setupBackup()
                setupNote()
                setupNotification()
                setupSave()
                setupOther()

                interactor.getThemeSummary()
                updateThemeSummary(themeSummary)

                updateImportEnabled(isEnabled = false)

                interactor.getSortSummary()
                updateSortSummary(sortSummary)
                interactor.getDefaultColorSummary()
                updateColorSummary(defaultColorSummary)
                interactor.getSavePeriodSummary()
                updateSavePeriodSummary(savePeriodSummary)

                interactor.getRepeatSummary()
                updateRepeatSummary(repeatSummary)
                signalInteractor.typeCheck
                interactor.getSignalSummary(typeCheck)
                updateSignalSummary(signalSummary)

                updateMelodyGroupEnabled(isEnabled = false)
                interactor.getVolumeSummary()
                updateVolumeSummary(volumeSummary)
            }

            spyViewModel.setupMelody()
            spyViewModel.setupBackup()
        }
    }

    @Test fun setupMelody_notMelody() = startCoTest {
        val state = SignalState(isMelody = false, isVibration = Random.nextBoolean())
        val item = melodyList.random()
        val index = melodyList.indexOf(item)

        every { signalInteractor.state } returns null

        viewModel.setupMelody()

        every { signalInteractor.state } returns state
        coEvery { signalInteractor.getMelodyCheck() } returns null

        viewModel.setupMelody()

        coEvery { signalInteractor.getMelodyCheck() } returns index
        coEvery { signalInteractor.getMelodyList() } returns emptyList()

        viewModel.setupMelody()

        coEvery { signalInteractor.getMelodyList() } returns melodyList

        viewModel.setupMelody()

        coVerifySequence {
            signalInteractor.state

            signalInteractor.state
            signalInteractor.getMelodyCheck()

            signalInteractor.state
            signalInteractor.getMelodyCheck()
            signalInteractor.getMelodyList()

            signalInteractor.state
            signalInteractor.getMelodyCheck()
            signalInteractor.getMelodyList()
            callback.updateMelodyGroupEnabled(state.isMelody)
            callback.updateMelodySummary(item.title)
        }
    }

    @Test fun setupMelody_isMelody() = startCoTest {
        val state = SignalState(isMelody = true, isVibration = Random.nextBoolean())
        val item = melodyList.random()
        val index = melodyList.indexOf(item)

        every { signalInteractor.state } returns null

        viewModel.setupMelody()

        every { signalInteractor.state } returns state
        coEvery { signalInteractor.getMelodyCheck() } returns null

        viewModel.setupMelody()

        coEvery { signalInteractor.getMelodyCheck() } returns index
        coEvery { signalInteractor.getMelodyList() } returns emptyList()

        viewModel.setupMelody()

        coEvery { signalInteractor.getMelodyList() } returns melodyList

        viewModel.setupMelody()

        coVerifySequence {
            signalInteractor.state

            signalInteractor.state
            signalInteractor.getMelodyCheck()
            callback.showToast(R.string.pref_toast_melody_empty)

            signalInteractor.state
            signalInteractor.getMelodyCheck()
            signalInteractor.getMelodyList()
            callback.showToast(R.string.pref_toast_melody_empty)

            signalInteractor.state
            signalInteractor.getMelodyCheck()
            signalInteractor.getMelodyList()
            callback.updateMelodyGroupEnabled(state.isMelody)
            callback.updateMelodySummary(item.title)
        }
    }

    @Test fun setupBackup() = startCoTest {
        coEvery { backupInteractor.getFileList() } returns emptyList()

        viewModel.setupBackup()

        coEvery { backupInteractor.getFileList() } returns fileList

        viewModel.setupBackup()

        coVerifySequence {
            backupInteractor.getFileList()

            backupInteractor.getFileList()
            callback.updateImportEnabled(isEnabled = true)
        }
    }

    @Test fun onPause() {
        viewModel.onPause()

        verifySequence {
            signalInteractor.resetMelodyList()
            backupInteractor.resetFileList()
        }
    }


    @Test fun onClickTheme() {
        val value = Random.nextInt()

        every { interactor.theme } returns value

        assertTrue(viewModel.onClickTheme())

        verifySequence {
            interactor.theme
            callback.showThemeDialog(value)
        }
    }

    @Test fun onResultTheme() {
        val value = Random.nextInt()
        val summary = Random.nextString()

        every { interactor.updateTheme(value) } returns summary

        viewModel.onResultTheme(value)

        verifySequence {
            interactor.updateTheme(value)
            callback.updateThemeSummary(summary)
        }
    }


    @Test fun onClickExport() {
        coEvery { spyViewModel.startExport() } returns Unit

        PermissionResult.values().forEach { assertTrue(spyViewModel.onClickExport(it)) }

        coVerifySequence {
            spyViewModel.onClickExport(PermissionResult.LOW_API)
            spyViewModel.startExport()

            spyViewModel.onClickExport(PermissionResult.ALLOWED)
            spyViewModel.callback
            callback.showExportPermissionDialog()

            spyViewModel.onClickExport(PermissionResult.FORBIDDEN)
            spyViewModel.callback
            callback.showExportDenyDialog()

            spyViewModel.onClickExport(PermissionResult.GRANTED)
            spyViewModel.startExport()
        }
    }

    @Test fun startExport() = startCoTest {
        val path = Random.nextString()

        coEvery { backupInteractor.export() } returns ExportResult.Error

        spyViewModel.startExport()

        coEvery { backupInteractor.export() } returns ExportResult.Success(path)
        coEvery { backupInteractor.getFileList() } returns List<FileItem>(size = 5) { mockk() }

        spyViewModel.startExport()

        coEvery { backupInteractor.getFileList() } returns emptyList()
        coEvery { spyViewModel.setupBackup() } returns Unit

        spyViewModel.startExport()

        coVerifySequence {
            spyViewModel.startExport()
            backupInteractor.export()
            spyViewModel.callback
            callback.showToast(R.string.pref_toast_export_error)

            spyViewModel.startExport()
            backupInteractor.export()
            spyViewModel.callback
            callback.showExportPathToast(path)
            backupInteractor.getFileList()

            spyViewModel.startExport()
            backupInteractor.export()
            spyViewModel.callback
            callback.showExportPathToast(path)
            backupInteractor.getFileList()
            backupInteractor.resetFileList()
            spyViewModel.setupBackup()
        }
    }

    @Test fun onClickImport() {
        coEvery { spyViewModel.prepareImportDialog() } returns Unit

        PermissionResult.values().forEach { assertTrue(spyViewModel.onClickImport(it)) }

        coVerifySequence {
            spyViewModel.onClickImport(PermissionResult.LOW_API)
            spyViewModel.prepareImportDialog()

            spyViewModel.onClickImport(PermissionResult.ALLOWED)
            spyViewModel.callback
            callback.showImportPermissionDialog()

            spyViewModel.onClickImport(PermissionResult.FORBIDDEN)
            spyViewModel.prepareImportDialog()

            spyViewModel.onClickImport(PermissionResult.GRANTED)
            spyViewModel.prepareImportDialog()
        }
    }

    @Test fun prepareImportDialog() = startCoTest {
        val titleArray = fileList.map { it.name }.toTypedArray()

        coEvery { backupInteractor.getFileList() } returns emptyList()

        viewModel.prepareImportDialog()

        coEvery { backupInteractor.getFileList() } returns fileList

        viewModel.prepareImportDialog()

        coVerifySequence {
            backupInteractor.getFileList()
            callback.updateImportEnabled(isEnabled = false)

            backupInteractor.getFileList()
            callback.showImportDialog(titleArray)
        }
    }

    @Test fun onResultImport() {
        val name = Random.nextString()
        val skipCount = Random.nextInt()

        coEvery { backupInteractor.import(name) } returns ImportResult.Simple

        viewModel.onResultImport(name)

        coEvery { backupInteractor.import(name) } returns ImportResult.Skip(skipCount)

        viewModel.onResultImport(name)

        coEvery { backupInteractor.import(name) } returns ImportResult.Error

        viewModel.onResultImport(name)

        coVerifySequence {
            backupInteractor.import(name)
            callback.showToast(R.string.pref_toast_import_result)

            backupInteractor.import(name)
            callback.showImportSkipToast(skipCount)

            backupInteractor.import(name)
            callback.showToast(R.string.pref_toast_import_error)
        }
    }


    @Test fun onClickSort() {
        val value = Random.nextInt()

        every { interactor.sort } returns value

        assertTrue(viewModel.onClickSort())

        verifySequence {
            interactor.sort
            callback.showSortDialog(value)
        }
    }

    @Test fun onResultNoteSort() {
        val value = Random.nextInt()
        val summary = Random.nextString()

        every { interactor.updateSort(value) } returns summary

        viewModel.onResultNoteSort(value)

        verifySequence {
            interactor.updateSort(value)
            callback.updateSortSummary(summary)
        }
    }

    @Test fun onClickNoteColor() {
        val valueColor = Random.nextInt()
        val valueTheme = Random.nextInt()

        every { interactor.defaultColor } returns valueColor
        every { interactor.theme } returns valueTheme

        assertTrue(viewModel.onClickNoteColor())

        verifySequence {
            interactor.defaultColor
            interactor.theme
            callback.showColorDialog(valueColor, valueTheme)
        }
    }

    @Test fun onResultNoteColor() {
        val value = Random.nextInt()
        val summary = Random.nextString()

        every { interactor.updateDefaultColor(value) } returns summary

        viewModel.onResultNoteColor(value)

        verifySequence {
            interactor.updateDefaultColor(value)
            callback.updateColorSummary(summary)
        }
    }

    @Test fun onClickSaveTime() {
        val value = Random.nextInt()

        every { interactor.savePeriod } returns value

        assertTrue(viewModel.onClickSaveTime())

        verifySequence {
            interactor.savePeriod
            callback.showSaveTimeDialog(value)
        }
    }

    @Test fun onResultSaveTime() {
        val value = Random.nextInt()
        val summary = Random.nextString()

        every { interactor.updateSavePeriod(value) } returns summary

        viewModel.onResultSaveTime(value)

        verifySequence {
            interactor.updateSavePeriod(value)
            callback.updateSavePeriodSummary(summary)
        }
    }


    @Test fun onClickRepeat() {
        val value = Random.nextInt()

        every { interactor.repeat } returns value

        assertTrue(viewModel.onClickRepeat())

        verifySequence {
            interactor.repeat
            callback.showRepeatDialog(value)
        }
    }

    @Test fun onResultRepeat() {
        val value = Random.nextInt()
        val summary = Random.nextString()

        every { interactor.updateRepeat(value) } returns summary

        viewModel.onResultRepeat(value)

        verifySequence {
            interactor.updateRepeat(value)
            callback.updateRepeatSummary(summary)
        }
    }

    @Test fun onClickSignal() {
        val valueArray = BooleanArray(size = 3) { Random.nextBoolean() }

        every { signalInteractor.typeCheck } returns valueArray

        assertTrue(viewModel.onClickSignal())

        verifySequence {
            signalInteractor.typeCheck
            callback.showSignalDialog(valueArray)
        }
    }

    @Test fun onResultSignal_isMelody() {
        val valueArray = BooleanArray(size = 3) { Random.nextBoolean() }
        val state = SignalState(isMelody = true, isVibration = Random.nextBoolean())
        val summary = Random.nextString()

        every { interactor.updateSignal(valueArray) } returns summary

        every { signalInteractor.state } returns null
        viewModel.onResultSignal(valueArray)

        every { signalInteractor.state } returns state
        coEvery { signalInteractor.getMelodyList() } returns emptyList()
        viewModel.onResultSignal(valueArray)

        coEvery { signalInteractor.getMelodyList() } returns melodyList
        viewModel.onResultSignal(valueArray)

        coVerifySequence {
            interactor.updateSignal(valueArray)
            callback.updateSignalSummary(summary)
            signalInteractor.state

            interactor.updateSignal(valueArray)
            callback.updateSignalSummary(summary)
            signalInteractor.state
            signalInteractor.getMelodyList()
            callback.showToast(R.string.pref_toast_melody_empty)

            interactor.updateSignal(valueArray)
            callback.updateSignalSummary(summary)
            signalInteractor.state
            signalInteractor.getMelodyList()
            callback.updateMelodyGroupEnabled(isEnabled = true)
        }
    }

    @Test fun onResultSignal_notMelody() {
        val valueArray = BooleanArray(size = 3) { Random.nextBoolean() }
        val state = SignalState(isMelody = false, isVibration = Random.nextBoolean())
        val summary = Random.nextString()

        every { interactor.updateSignal(valueArray) } returns summary

        every { signalInteractor.state } returns null
        viewModel.onResultSignal(valueArray)

        every { signalInteractor.state } returns state
        viewModel.onResultSignal(valueArray)

        coVerifySequence {
            interactor.updateSignal(valueArray)
            callback.updateSignalSummary(summary)
            signalInteractor.state

            interactor.updateSignal(valueArray)
            callback.updateSignalSummary(summary)
            signalInteractor.state
            callback.updateMelodyGroupEnabled(isEnabled = false)
        }
    }


    @Test fun onClickMelody() {
        coEvery { spyViewModel.prepareMelodyDialog() } returns Unit

        assertTrue(spyViewModel.onClickMelody(PermissionResult.LOW_API))
        assertTrue(spyViewModel.onClickMelody(PermissionResult.ALLOWED))
        assertTrue(spyViewModel.onClickMelody(PermissionResult.FORBIDDEN))
        assertTrue(spyViewModel.onClickMelody(PermissionResult.GRANTED))

        coVerifySequence {
            spyViewModel.onClickMelody(PermissionResult.LOW_API)
            spyViewModel.prepareMelodyDialog()

            spyViewModel.onClickMelody(PermissionResult.ALLOWED)
            spyViewModel.callback
            callback.showMelodyPermissionDialog()

            spyViewModel.onClickMelody(PermissionResult.FORBIDDEN)
            spyViewModel.prepareMelodyDialog()

            spyViewModel.onClickMelody(PermissionResult.GRANTED)
            spyViewModel.prepareMelodyDialog()
        }
    }

    @Test fun prepareMelodyDialog() = startCoTest {
        val index = Random.nextInt()
        val titleArray = melodyList.map { it.title }.toTypedArray()

        coEvery { signalInteractor.getMelodyList() } returns emptyList()
        coEvery { signalInteractor.getMelodyCheck() } returns null

        viewModel.prepareMelodyDialog()

        coEvery { signalInteractor.getMelodyCheck() } returns Random.nextInt()

        viewModel.prepareMelodyDialog()

        coEvery { signalInteractor.getMelodyList() } returns melodyList
        coEvery { signalInteractor.getMelodyCheck() } returns null

        viewModel.prepareMelodyDialog()

        coEvery { signalInteractor.getMelodyCheck() } returns index

        viewModel.prepareMelodyDialog()

        coVerifySequence {
            repeat(times = 3) {
                signalInteractor.getMelodyList()
                signalInteractor.getMelodyCheck()
                callback.updateMelodyGroupEnabled(isEnabled = false)
            }

            signalInteractor.getMelodyList()
            signalInteractor.getMelodyCheck()
            callback.showMelodyDialog(titleArray, index)
        }
    }

    @Test fun onSelectMelody_onNull() {
        val index = -1

        coEvery { signalInteractor.getMelodyList() } returns melodyList

        viewModel.onSelectMelody(index)

        coVerifySequence {
            signalInteractor.getMelodyList()
        }
    }

    @Test fun onSelectMelody() {
        val item = melodyList.random()
        val index = melodyList.indexOf(item)

        coEvery { signalInteractor.getMelodyList() } returns melodyList

        viewModel.onSelectMelody(index)

        coVerifySequence {
            signalInteractor.getMelodyList()

            callback.playMelody(item.uri)
        }
    }

    @Test fun onResultMelody() {
        val item = melodyList.random()

        coEvery { signalInteractor.getMelodyList() } returns melodyList
        coEvery { signalInteractor.setMelodyUri(item.title) } returns item.title

        viewModel.onResultMelody(item.title)

        coVerifySequence {
            signalInteractor.setMelodyUri(item.title)

            callback.updateMelodySummary(item.title)
        }
    }

    @Test fun onResultMelody_onNotEquals() {
        val item = melodyList.random()
        val newTitle = Random.nextString()

        coEvery { signalInteractor.getMelodyList() } returns melodyList
        coEvery { signalInteractor.setMelodyUri(item.title) } returns newTitle

        viewModel.onResultMelody(item.title)

        coVerifySequence {
            signalInteractor.setMelodyUri(item.title)

            callback.updateMelodySummary(newTitle)
            callback.showToast(R.string.pref_toast_melody_replace)
        }
    }

    @Test fun onResultMelody_onNull() {
        val item = melodyList.random()

        coEvery { signalInteractor.getMelodyList() } returns melodyList
        coEvery { signalInteractor.setMelodyUri(item.title) } returns null

        viewModel.onResultMelody(item.title)

        coVerifySequence {
            signalInteractor.setMelodyUri(item.title)

            callback.showToast(R.string.pref_toast_melody_empty)
        }
    }

    @Test fun onClickVolume() {
        val value = Random.nextInt()

        every { interactor.volume } returns value

        assertTrue(viewModel.onClickVolume())

        verifySequence {
            interactor.volume
            callback.showVolumeDialog(value)
        }
    }

    @Test fun onResultVolume() {
        val value = Random.nextInt()
        val summary = Random.nextString()

        every { interactor.updateVolume(value) } returns summary

        viewModel.onResultVolume(value)

        verifySequence {
            interactor.updateVolume(value)
            callback.updateVolumeSummary(summary)
        }
    }

}