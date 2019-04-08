package sgtmelon.scriptum.app.screen.vm

import android.app.Application
import sgtmelon.scriptum.R
import sgtmelon.scriptum.app.screen.callback.AppCallback
import sgtmelon.scriptum.app.screen.view.AppActivity
import sgtmelon.scriptum.office.annot.def.ThemeDef

/**
 * ViewModel для [AppActivity]
 *
 * @author SerjantArbuz
 */
class AppViewModel(application: Application) : ParentViewModel(application) {

    lateinit var callback: AppCallback

    @ThemeDef private var currentTheme: Int = 0

    fun onSetupTheme() {
        currentTheme = preference.theme

        when (currentTheme) {
            ThemeDef.light -> callback.setTheme(R.style.App_Light_UI)
            ThemeDef.dark -> callback.setTheme(R.style.App_Dark_UI)
        }
    }

    fun isThemeChange() = currentTheme != preference.theme

}