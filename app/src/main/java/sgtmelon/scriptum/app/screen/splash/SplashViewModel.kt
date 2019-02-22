package sgtmelon.scriptum.app.screen.splash

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModel
import sgtmelon.scriptum.app.screen.intro.IntroActivity
import sgtmelon.scriptum.app.screen.main.MainActivity
import sgtmelon.scriptum.app.screen.note.NoteActivity
import sgtmelon.scriptum.office.utils.PrefUtils

/**
 * ViewModel для [SplashActivity]
 */
class SplashViewModel : ViewModel() {

    lateinit var context: Context
    lateinit var callback: SplashCallback

    private val prefUtils by lazy { PrefUtils(context) }

    fun onStartApplication(bundle: Bundle?) {
        if (bundle != null && bundle.getBoolean(SplashActivity.STATUS_OPEN)) {
            callback.startFromNotification(arrayOf(
                    Intent(context, MainActivity::class.java),
                    NoteActivity.getIntent(context, bundle.getLong(SplashActivity.NOTE_ID))
            ))
        } else {
            callback.startNormal(Intent(context, when (prefUtils.firstStart) {
                true -> IntroActivity::class.java
                false -> MainActivity::class.java
            }))
        }
    }

}