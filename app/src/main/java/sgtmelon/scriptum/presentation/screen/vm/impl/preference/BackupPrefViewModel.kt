package sgtmelon.scriptum.presentation.screen.vm.impl.preference

import android.app.Application
import android.os.Bundle
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import sgtmelon.scriptum.R
import sgtmelon.scriptum.domain.interactor.callback.preference.IBackupPrefInteractor
import sgtmelon.scriptum.domain.model.annotation.test.RunPrivate
import sgtmelon.scriptum.domain.model.result.ExportResult
import sgtmelon.scriptum.domain.model.result.ImportResult
import sgtmelon.scriptum.extension.runBack
import sgtmelon.scriptum.presentation.screen.ui.callback.preference.IBackupPrefFragment
import sgtmelon.scriptum.presentation.screen.vm.callback.preference.IBackupPrefViewModel
import sgtmelon.scriptum.presentation.screen.vm.impl.ParentViewModel
import sgtmelon.scriptum.domain.model.key.PermissionResult as Permission

/**
 * ViewModel for [IBackupPrefFragment].
 */
class BackupPrefViewModel(
    application: Application
) : ParentViewModel<IBackupPrefFragment>(application),
    IBackupPrefViewModel {

    private lateinit var interactor: IBackupPrefInteractor

    fun setInteractor(interactor: IBackupPrefInteractor) {
        this.interactor = interactor
    }


    override fun onSetup(bundle: Bundle?) {
        callback?.setup()

        val exportResult = callback?.getExportPermissionResult() ?: return
        val importResult = callback?.getImportPermissionResult() ?: return

        val isExportAllowed = exportResult == Permission.LOW_API
                || exportResult == Permission.GRANTED
        val isImportAllowed = importResult == Permission.LOW_API
                || importResult == Permission.GRANTED

        if (!isExportAllowed) {
            callback?.updateExportEnabled(isEnabled = true)
            callback?.updateExportSummary(R.string.pref_summary_permission_need)
        }

        if (!isImportAllowed) {
            callback?.updateImportEnabled(isEnabled = true)
            callback?.updateImportSummary(R.string.pref_summary_permission_need)
        }

        if (isExportAllowed && isImportAllowed) {
            viewModelScope.launch { setupBackground() }
        }
    }

    @RunPrivate suspend fun setupBackground() {
        callback?.updateExportEnabled(isEnabled = false)
        callback?.resetExportSummary()

        callback?.updateImportEnabled(isEnabled = false)
        callback?.updateImportSummary(R.string.pref_summary_backup_import_search)

        val fileList = runBack { interactor.getFileList() }

        if (fileList.isEmpty()) {
            callback?.updateImportSummary(R.string.pref_summary_backup_import_empty)
        } else {
            callback?.updateImportSummaryFound(fileList.size)
            callback?.updateImportEnabled(isEnabled = true)
        }

        callback?.updateExportEnabled(isEnabled = true)
    }

    /**
     * Need reset list, because user can change permission or
     * delete some files or remove sd card.
     *
     * It calls even after permission dialog.
     */
    override fun onPause() {
        interactor.resetFileList()
    }

    //region Export/import functions

    /**
     * This functions for check both permissions and if one of them is [Permission.FORBIDDEN]
     * need call [onClickExport] with that [Permission].
     */
    override fun onClickExport() {
        val exportResult = callback?.getExportPermissionResult() ?: return
        val importResult = callback?.getImportPermissionResult() ?: return

        if (exportResult == Permission.FORBIDDEN || importResult == Permission.FORBIDDEN) {
            onClickExport(Permission.FORBIDDEN)
        } else {
            onClickExport(exportResult)
        }
    }

    /**
     * Call [startExport] only if [result] equals [Permission.LOW_API] or
     * [Permission.GRANTED]. Otherwise we must show dialog.
     */
    override fun onClickExport(result: sgtmelon.scriptum.domain.model.key.PermissionResult) {
        when (result) {
            Permission.ALLOWED -> callback?.showExportPermissionDialog()
            Permission.LOW_API, Permission.GRANTED -> viewModelScope.launch { startExport() }
            Permission.FORBIDDEN -> callback?.showExportDenyDialog()
        }
    }

    @RunPrivate suspend fun startExport() {
        callback?.showExportLoadingDialog()
        val result: ExportResult = runBack { interactor.export() }
        callback?.hideExportLoadingDialog()

        when (result) {
            is ExportResult.Success -> {
                callback?.showExportPathToast(result.path)

                /**
                 * Need update file list for feature import.
                 */
                interactor.resetFileList()
                setupBackground()
            }
            is ExportResult.Error -> {
                callback?.showToast(R.string.pref_toast_export_error)
            }
        }
    }


    /**
     * This functions for check both permissions and if one of them is [Permission.FORBIDDEN]
     * need call [onClickImport] with that [Permission].
     */
    override fun onClickImport() {
        val exportResult = callback?.getExportPermissionResult() ?: return
        val importResult = callback?.getImportPermissionResult() ?: return

        if (exportResult == Permission.FORBIDDEN || importResult == Permission.FORBIDDEN) {
            onClickImport(Permission.FORBIDDEN)
        } else {
            onClickImport(importResult)
        }
    }

    /**
     * Call [prepareImportDialog] only if [result] equals [Permission.LOW_API] or
     * [Permission.GRANTED]. Otherwise we must show dialog.
     */
    override fun onClickImport(result: sgtmelon.scriptum.domain.model.key.PermissionResult) {
        when (result) {
            Permission.ALLOWED -> callback?.showImportPermissionDialog()
            Permission.LOW_API, Permission.GRANTED -> viewModelScope.launch {
                prepareImportDialog()
            }
            Permission.FORBIDDEN -> callback?.showImportDenyDialog()
        }
    }

    @RunPrivate suspend fun prepareImportDialog() {
        val fileList = runBack { interactor.getFileList() }
        val titleArray = fileList.map { it.name }.toTypedArray()

        if (titleArray.isEmpty()) {
            callback?.updateImportSummary(R.string.pref_summary_backup_import_empty)
            callback?.updateImportEnabled(isEnabled = false)
        } else {
            callback?.showImportDialog(titleArray)
        }
    }

    override fun onResultImport(name: String) {
        viewModelScope.launch {
            callback?.showImportLoadingDialog()
            val result: ImportResult = runBack { interactor.import(name) }
            callback?.hideImportLoadingDialog()

            when (result) {
                is ImportResult.Simple -> callback?.showToast(R.string.pref_toast_import_result)
                is ImportResult.Skip -> callback?.showImportSkipToast(result.skipCount)
                is ImportResult.Error -> callback?.showToast(R.string.pref_toast_import_error)
            }

            if (result == ImportResult.Error) return@launch

            // TODO update alarm binds (all) after adding new notes
            callback?.sendNotifyNotesBroadcast()
            callback?.sendNotifyInfoBroadcast()
        }
    }

    //endregion

}