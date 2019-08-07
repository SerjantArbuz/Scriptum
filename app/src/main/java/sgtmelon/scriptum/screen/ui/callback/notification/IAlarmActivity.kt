package sgtmelon.scriptum.screen.ui.callback.notification

import android.content.Intent
import android.net.Uri
import androidx.annotation.ColorInt
import sgtmelon.scriptum.model.NoteModel
import sgtmelon.scriptum.model.annotation.Theme
import sgtmelon.scriptum.screen.ui.notification.AlarmActivity
import sgtmelon.scriptum.screen.vm.notification.AlarmViewModel

/**
 * Interface for communication [AlarmViewModel] with [AlarmActivity]
 *
 * @author SerjantArbuz
 */
interface IAlarmActivity {

    fun setupView(@Theme theme: Int)

    fun setupMelody(uri: Uri)

    fun notifyDataSetChanged(noteModel: NoteModel)

    fun startRippleAnimation(@Theme theme: Int, @ColorInt fillColor: Int)

    fun startButtonFadeInAnimation()

    fun melodyStart()

    fun melodyStop()

    fun vibrateStart(pattern: LongArray)

    fun vibrateCancel()

    fun startActivity(intent: Intent)

    fun finish()

}