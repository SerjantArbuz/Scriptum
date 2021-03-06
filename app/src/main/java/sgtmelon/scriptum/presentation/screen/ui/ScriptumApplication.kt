package sgtmelon.scriptum.presentation.screen.ui

import android.app.Application
import android.content.Context
import android.os.Build
import sgtmelon.scriptum.dagger.component.DaggerScriptumComponent
import sgtmelon.scriptum.dagger.component.ScriptumComponent
import sgtmelon.scriptum.extension.initLazy
import sgtmelon.scriptum.presentation.screen.system.SystemLogic
import sgtmelon.scriptum.presentation.service.EternalService

/**
 * Guideline for maintain project:
 *
 * ---------------------------------------------
 * Need to be careful with lazy properties!
 *
 * 1. Inside fragment setup view's ONLY manually. Inside activity setup view's with lazy func.
 *    Need setup manually because after rotation lazy function will return null.
 *
 * 2. Use [initLazy] for properties which contains [Context] in constructor.
 *    Troubles happen after rotation if property wasn't initialized.
 * ---------------------------------------------
 * [EternalService] and [SystemLogic]:
 *
 * 1. For API >= [Build.VERSION_CODES.O] need use [EternalService] and hide channel of
 *    service notification in application settings
 *
 * 2. For API < [Build.VERSION_CODES.O] need use [SystemLogic] inside [ScriptumApplication],
 *    for prevent unbind service notification inside status bar.
 */
class ScriptumApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        component = DaggerScriptumComponent.builder().set(application = this).build()

        /**
         * See explanation of this if/else inside [EternalService.start].
         */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            EternalService.start(context = this)
        } else {
            SystemLogic().onCreate(context = this)
        }
    }

    companion object {
        lateinit var component: ScriptumComponent
    }
}