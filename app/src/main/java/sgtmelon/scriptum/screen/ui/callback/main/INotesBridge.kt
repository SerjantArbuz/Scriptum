package sgtmelon.scriptum.screen.ui.callback.main

import sgtmelon.scriptum.control.alarm.AlarmControl
import sgtmelon.scriptum.control.bind.BindControl
import sgtmelon.scriptum.control.clipboard.ClipboardControl
import sgtmelon.scriptum.interactor.main.NotesInteractor
import sgtmelon.scriptum.screen.ui.main.NotesFragment

/**
 * Interface for communication [NotesInteractor] with [NotesFragment]
 */
interface INotesBridge : AlarmControl.Bridge.Cancel,
        BindControl.Bridge.Full,
        ClipboardControl.Bridge