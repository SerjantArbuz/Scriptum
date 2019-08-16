package sgtmelon.scriptum.receiver

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import sgtmelon.scriptum.model.annotation.Color
import sgtmelon.scriptum.model.data.ReceiverData.Values
import sgtmelon.scriptum.room.entity.NoteEntity
import sgtmelon.scriptum.screen.ui.SplashActivity
import sgtmelon.scriptum.screen.ui.notification.AlarmActivity

/**
 * Receiver for open [AlarmActivity] by time
 *
 * @author SerjantArbuz
 */
class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if (context == null || intent == null) return

        val id = intent.getLongExtra(Values.NOTE_ID, Values.ND_NOTE_ID)
        val color = intent.getIntExtra(Values.NOTE_COLOR, Values.ND_NOTE_COLOR)

        if (id == Values.ND_NOTE_ID || color == Values.ND_NOTE_COLOR) return

        context.startActivity(SplashActivity.getAlarmInstance(context, id, color))
    }

    companion object {
        fun getInstance(context: Context, noteEntity: NoteEntity) =
                getInstance(context, noteEntity.id, noteEntity.color)

        fun getInstance(context: Context, id: Long, @Color color: Int): PendingIntent {
            val intent = Intent(context, AlarmReceiver::class.java)
                    .putExtra(Values.NOTE_ID, id)
                    .putExtra(Values.NOTE_COLOR, color)

            return PendingIntent.getBroadcast(
                    context, id.toInt(), intent, PendingIntent.FLAG_UPDATE_CURRENT
            )
        }
    }

}