package sgtmelon.scriptum.presentation.factory

import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.os.Build.VERSION_CODES
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.TaskStackBuilder
import sgtmelon.scriptum.R
import sgtmelon.scriptum.domain.model.item.NoteItem
import sgtmelon.scriptum.domain.model.item.RollItem
import sgtmelon.scriptum.domain.model.key.ColorShade
import sgtmelon.scriptum.domain.model.key.NoteType
import sgtmelon.scriptum.extension.getAppSimpleColor
import sgtmelon.scriptum.extension.hide
import sgtmelon.scriptum.presentation.control.system.BindControl
import sgtmelon.scriptum.presentation.receiver.UnbindReceiver
import sgtmelon.scriptum.presentation.screen.ui.impl.SplashActivity

/**
 * Factory for create notifications
 */
object NotificationFactory {

    /**
     * Model for [BindControl.notifyNote]
     *
     * Don't care about [NoteItem.Roll.list] if:
     * - If note type is [NoteType.TEXT]
     * - If type is [NoteType.ROLL] and [NoteItem.Roll.list] is completely load
     * - If you need only call [BindControl.cancelNote]
     */
    fun getBind(context: Context, noteItem: NoteItem): Notification {
        val icon = when (noteItem) {
            is NoteItem.Text -> R.drawable.notif_bind_text
            is NoteItem.Roll -> R.drawable.notif_bind_roll
        }

        val color = context.getAppSimpleColor(noteItem.color, ColorShade.DARK)
        val title = noteItem.getStatusTitle(context)
        val text = when (noteItem) {
            is NoteItem.Text -> noteItem.text
            is NoteItem.Roll -> noteItem.list.let {
                if (noteItem.isVisible) it else it.hide()
            }.toStatusText().takeIf {
                it.isNotEmpty()
            } ?: context.getString(R.string.info_roll_hide_title)
        }

        val id = noteItem.id.toInt()
        val contentIntent = TaskStackBuilder.create(context)
            .addNextIntent(SplashActivity.getBindInstance(context, noteItem))
            .getPendingIntent(id, PendingIntent.FLAG_UPDATE_CURRENT)

        return NotificationCompat.Builder(context, context.getString(R.string.notification_notes_channel_id))
            .setSmallIcon(icon)
            .setColor(color)
            .setContentTitle(title)
            .setContentText(text)
            .setCategory(NotificationCompat.CATEGORY_EVENT)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setStyle(NotificationCompat.BigTextStyle().bigText(text))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(contentIntent)
            .addAction(0, context.getString(R.string.notification_button_unbind), UnbindReceiver[context, noteItem])
            .setAutoCancel(false)
            .setOngoing(true)
            .setGroup(context.getString(R.string.notification_group_notes))
            .build()
    }

    private fun NoteItem.getStatusTitle(context: Context): String {
        return (if (type == NoteType.ROLL) "$text | " else "")
            .plus(if (name.isEmpty()) context.getString(R.string.hint_text_name) else name)
    }

    private fun List<RollItem>.toStatusText() = joinToString(separator = "\n") {
        "${if (it.isCheck) "\u25CF" else "\u25CB"} ${it.text}"
    }

    @RequiresApi(VERSION_CODES.N)
    fun getBindSummary(context: Context): Notification {
        return NotificationCompat.Builder(context, context.getString(R.string.notification_notes_channel_id))
            .setSmallIcon(R.drawable.notif_bind_group)
            .setCategory(NotificationCompat.CATEGORY_EVENT)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(false)
            .setOngoing(true)
            .setGroup(context.getString(R.string.notification_group_notes))
            .setGroupSummary(true)
            .build()
    }


    fun getInfo(context: Context, id: Int, count: Int): Notification {
        val contentIntent = TaskStackBuilder.create(context)
            .addNextIntent(SplashActivity.getNotificationInstance(context))
            .getPendingIntent(id, PendingIntent.FLAG_UPDATE_CURRENT)

        return NotificationCompat.Builder(context, context.getString(R.string.notification_info_channel_id))
            .setSmallIcon(R.drawable.notif_info)
            .setContentTitle(context.resources.getQuantityString(R.plurals.notification_info_title, count, count))
            .setContentText(context.getString(R.string.notification_info_description))
            .setCategory(NotificationCompat.CATEGORY_EVENT)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(contentIntent)
            .setAutoCancel(false)
            .setOngoing(true)
            .setGroup(context.getString(R.string.notification_group_info))
            .build()
    }

}