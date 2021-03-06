package sgtmelon.scriptum.presentation.factory

import android.content.Context
import androidx.fragment.app.FragmentManager
import sgtmelon.safedialog.MessageDialog
import sgtmelon.safedialog.MultipleDialog
import sgtmelon.safedialog.OptionsDialog
import sgtmelon.safedialog.SingleDialog
import sgtmelon.safedialog.annotation.MessageType
import sgtmelon.safedialog.time.DateDialog
import sgtmelon.safedialog.time.TimeDialog
import sgtmelon.scriptum.R
import sgtmelon.scriptum.domain.model.key.NoteType
import sgtmelon.scriptum.extension.getFragmentByTag
import sgtmelon.scriptum.presentation.dialog.*

/**
 * Factory for create/get dialogs
 */
object DialogFactory {

    class Alarm(private val fm: FragmentManager) {

        fun getRepeatDialog(): SheetRepeatDialog {
            return fm.getFragmentByTag(REPEAT) ?: SheetRepeatDialog()
        }

        companion object {
            private const val PREFIX = "DIALOG_ALARM"

            const val REPEAT = "${PREFIX}_REPEAT"
        }
    }

    class Main(private val context: Context?, private val fm: FragmentManager) {

        fun getRenameDialog(): RenameDialog = fm.getFragmentByTag(RENAME) ?: RenameDialog()

        fun getAddDialog(): SheetAddDialog = fm.getFragmentByTag(ADD) ?: SheetAddDialog()

        fun getOptionsDialog(): OptionsDialog = fm.getFragmentByTag(OPTIONS) ?: OptionsDialog()

        fun getDateDialog(): DateDialog = fm.getFragmentByTag(DATE) ?: DateDialog()

        fun getTimeDialog(): TimeDialog = fm.getFragmentByTag(TIME) ?: TimeDialog()

        fun getClearBinDialog(): MessageDialog {
            val dialog = fm.getFragmentByTag(CLEAR_BIN) ?: MessageDialog()

            if (context == null) return dialog

            dialog.type = MessageType.CHOICE
            dialog.title = context.getString(R.string.dialog_title_clear_bin)
            dialog.message = context.getString(R.string.dialog_text_clear_bin)

            return dialog
        }

        companion object {
            private const val PREFIX = "DIALOG_MAIN"

            const val RENAME = "${PREFIX}_RENAME"

            const val ADD = "${PREFIX}_ADD"
            const val OPTIONS = "${PREFIX}_OPTIONS"
            const val DATE = "${PREFIX}_DATE"
            const val TIME = "${PREFIX}_TIME"

            const val CLEAR_BIN = "${PREFIX}_CLEAR_BIN"
        }
    }

    class Note(private val context: Context?, private val fm: FragmentManager) {

        fun getConvertDialog(type: NoteType): MessageDialog {
            val dialog = fm.getFragmentByTag(CONVERT) ?: MessageDialog()

            if (context == null) return dialog

            dialog.type = MessageType.CHOICE
            dialog.title = context.getString(R.string.dialog_title_convert)
            dialog.message = when (type) {
                NoteType.TEXT -> context.getString(R.string.dialog_text_convert_text)
                NoteType.ROLL -> context.getString(R.string.dialog_roll_convert_roll)
            }

            return dialog
        }

        fun getRankDialog(): SingleDialog {
            val dialog = fm.getFragmentByTag(RANK) ?: SingleDialog()

            if (context == null) return dialog

            dialog.title = context.getString(R.string.dialog_title_rank)

            return dialog
        }

        fun getColorDialog(): ColorDialog {
            val dialog = fm.getFragmentByTag(COLOR) ?: ColorDialog()

            if (context == null) return dialog

            dialog.title = context.getString(R.string.dialog_title_color)

            return dialog
        }

        fun getDateDialog(): DateDialog = fm.getFragmentByTag(DATE) ?: DateDialog()

        fun getTimeDialog(): TimeDialog = fm.getFragmentByTag(TIME) ?: TimeDialog()

        companion object {
            private const val PREFIX = "DIALOG_NOTE"

            const val DATE = "${PREFIX}_DATE"
            const val TIME = "${PREFIX}_TIME"
            const val CONVERT = "${PREFIX}_CONVERT"

            const val RANK = "${PREFIX}_RANK"
            const val COLOR = "${PREFIX}_COLOR"
        }
    }

    object Preference {

        class Main(private val context: Context?, private val fm: FragmentManager) {

            fun getThemeDialog(): SingleDialog {
                val dialog = fm.getFragmentByTag(THEME) ?: SingleDialog()

                if (context == null) return dialog

                dialog.title = context.getString(R.string.pref_title_app_theme)
                dialog.itemArray = context.resources.getStringArray(R.array.pref_text_app_theme)

                return dialog
            }

            fun getAboutDialog(): AboutDialog = fm.getFragmentByTag(ABOUT) ?: AboutDialog()

            companion object {
                private const val PREFIX = "DIALOG_PREF_MAIN"

                const val THEME = "${PREFIX}_THEME"
                const val ABOUT = "${PREFIX}_ABOUT"
            }
        }

        class Backup(private val context: Context?, private val fm: FragmentManager) {

            fun getExportPermissionDialog(): MessageDialog {
                val dialog = fm.getFragmentByTag(EXPORT_PERMISSION) ?: MessageDialog()

                if (context == null) return dialog

                dialog.type = MessageType.INFO
                dialog.title = context.getString(R.string.dialog_title_export_permission)
                dialog.message = context.getString(R.string.dialog_text_export_permission)

                return dialog
            }

            fun getExportDenyDialog(): MessageDialog {
                val dialog = fm.getFragmentByTag(EXPORT_DENY) ?: MessageDialog()

                if (context == null) return dialog

                dialog.type = MessageType.INFO
                dialog.title = context.getString(R.string.dialog_title_export_deny)
                dialog.message = context.getString(R.string.dialog_text_export_deny)

                return dialog
            }

            fun getLoadingDialog(): LoadingDialog {
                return fm.getFragmentByTag(LOADING) ?: LoadingDialog()
            }

            fun getImportPermissionDialog(): MessageDialog {
                val dialog = fm.getFragmentByTag(IMPORT_PERMISSION) ?: MessageDialog()

                if (context == null) return dialog

                dialog.type = MessageType.INFO
                dialog.title = context.getString(R.string.dialog_title_import_permission)
                dialog.message = context.getString(R.string.dialog_text_import_permission)

                return dialog
            }

            fun getImportDenyDialog(): MessageDialog {
                val dialog = fm.getFragmentByTag(IMPORT_DENY) ?: MessageDialog()

                if (context == null) return dialog

                dialog.type = MessageType.INFO
                dialog.title = context.getString(R.string.dialog_title_import_deny)
                dialog.message = context.getString(R.string.dialog_text_import_deny)

                return dialog
            }

            fun getImportDialog(): SingleDialog {
                val dialog = fm.getFragmentByTag(IMPORT) ?: SingleDialog()

                if (context == null) return dialog

                dialog.applyEnable = true
                dialog.title = context.getString(R.string.dialog_title_import)

                return dialog
            }

            companion object {
                private const val PREFIX = "DIALOG_PREF_BACKUP"

                const val EXPORT_PERMISSION = "${PREFIX}_EXPORT_PERMISSION"
                const val EXPORT_DENY = "${PREFIX}_EXPORT_DENY"
                const val IMPORT_PERMISSION = "${PREFIX}_IMPORT_PERMISSION"
                const val IMPORT_DENY = "${PREFIX}_IMPORT_DENY"
                const val IMPORT = "${PREFIX}_IMPORT"
                const val LOADING = "${PREFIX}_LOADING"
            }
        }

        class Notes(private val context: Context?, private val fm: FragmentManager) {

            fun getSortDialog(): SingleDialog {
                val dialog = fm.getFragmentByTag(SORT) ?: SingleDialog()

                if (context == null) return dialog

                dialog.title = context.getString(R.string.pref_title_note_sort)
                dialog.itemArray = context.resources.getStringArray(R.array.pref_text_note_sort)

                return dialog
            }

            fun getColorDialog(): ColorDialog {
                val dialog = fm.getFragmentByTag(COLOR) ?: ColorDialog()

                if (context == null) return dialog

                dialog.title = context.getString(R.string.pref_title_note_color)

                return dialog
            }

            fun getSavePeriodDialog(): SingleDialog {
                val dialog = fm.getFragmentByTag(SAVE_PERIOD) ?: SingleDialog()

                if (context == null) return dialog

                dialog.title = context.getString(R.string.pref_title_note_save_period)
                dialog.itemArray = context.resources.getStringArray(R.array.pref_text_note_save_period)

                return dialog
            }

            companion object {
                private const val PREFIX = "DIALOG_PREF_NOTES"

                const val SORT = "${PREFIX}_SORT"
                const val COLOR = "${PREFIX}_COLOR"
                const val SAVE_PERIOD = "${PREFIX}_SAVE_PERIOD"
            }
        }

        class Alarm(private val context: Context?, private val fm: FragmentManager) {

            fun getRepeatDialog(): SingleDialog {
                val dialog = fm.getFragmentByTag(REPEAT) ?: SingleDialog()

                if (context == null) return dialog

                dialog.title = context.getString(R.string.pref_title_alarm_repeat)
                dialog.itemArray = context.resources.getStringArray(R.array.pref_text_alarm_repeat)

                return dialog
            }

            fun getSignalDialog(): MultipleDialog {
                val dialog = fm.getFragmentByTag(SIGNAL) ?: MultipleDialog()

                if (context == null) return dialog

                dialog.needOneSelect = true
                dialog.title = context.getString(R.string.pref_title_alarm_signal)
                dialog.itemArray = context.resources.getStringArray(R.array.pref_text_alarm_signal)

                return dialog
            }

            fun getMelodyPermissionDialog(): MessageDialog {
                val dialog = fm.getFragmentByTag(MELODY_PERMISSION) ?: MessageDialog()

                if (context == null) return dialog

                dialog.type = MessageType.INFO
                dialog.title = context.getString(R.string.dialog_title_melody_permission)
                dialog.message = context.getString(R.string.dialog_text_melody_permission)

                return dialog
            }

            fun getMelodyDialog(): SingleDialog {
                val dialog = fm.getFragmentByTag(MELODY) ?: SingleDialog()

                if (context == null) return dialog

                dialog.title = context.getString(R.string.pref_title_alarm_melody)

                return dialog
            }

            fun getVolumeDialog(): VolumeDialog {
                val dialog = fm.getFragmentByTag(VOLUME) ?: VolumeDialog()

                if (context == null) return dialog

                dialog.title = context.getString(R.string.pref_title_alarm_volume)

                return dialog
            }

            companion object {
                private const val PREFIX = "DIALOG_PREF_ALARM"

                const val REPEAT = "${PREFIX}_REPEAT"
                const val SIGNAL = "${PREFIX}_SIGNAL"
                const val MELODY_PERMISSION = "${PREFIX}_MELODY_PERMISSION"
                const val MELODY = "${PREFIX}_MELODY"
                const val VOLUME = "${PREFIX}_VOLUME"
            }
        }
    }
}