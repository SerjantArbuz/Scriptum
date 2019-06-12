package sgtmelon.scriptum.screen.vm

import android.app.Application
import android.content.Intent
import android.os.Bundle
import sgtmelon.scriptum.model.data.NoteData
import sgtmelon.scriptum.model.key.NoteType
import sgtmelon.scriptum.screen.callback.SplashCallback
import sgtmelon.scriptum.screen.view.SplashActivity
import sgtmelon.scriptum.screen.view.SplashActivity.Companion.OpenFrom
import sgtmelon.scriptum.screen.view.intro.IntroActivity
import sgtmelon.scriptum.screen.view.main.MainActivity
import sgtmelon.scriptum.screen.view.note.NoteActivity.Companion.getNoteIntent
import sgtmelon.scriptum.screen.view.notification.AlarmActivity.Companion.getAlarmIntent

/**
 * ViewModel для [SplashActivity]
 *
 * @author SerjantArbuz
 */
class SplashViewModel(application: Application) : ParentViewModel(application) {

    lateinit var callback: SplashCallback

    fun onSetup(bundle: Bundle?) {
        if (bundle == null) {
            onSimpleStart()
        } else {
            when (bundle.getString(SplashActivity.OPEN_SCREEN) ?: "") {
                OpenFrom.BIND -> {
                    val intent = context.getNoteIntent(
                            NoteType.values()[bundle.getInt(NoteData.Intent.TYPE)],
                            bundle.getLong(NoteData.Intent.ID)
                    )

                    callback.startActivities(arrayOf(MainActivity.getInstance(context), intent))
                }
                OpenFrom.ALARM -> {
                    callback.startActivity(context.getAlarmIntent(
                            bundle.getLong(NoteData.Intent.ID),
                            bundle.getInt(NoteData.Intent.COLOR)
                    ))
                }
                else -> onSimpleStart()
            }
        }
    }

    private fun onSimpleStart() = callback.startActivity(
            Intent(context, if (iPreferenceRepo.firstStart) {
                IntroActivity::class.java
            } else {
                MainActivity::class.java
            })
    )

}