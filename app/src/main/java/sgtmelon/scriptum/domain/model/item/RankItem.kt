package sgtmelon.scriptum.domain.model.item

import androidx.room.ColumnInfo
import androidx.room.TypeConverters
import org.json.JSONArray
import org.json.JSONObject
import sgtmelon.scriptum.data.room.converter.type.BoolConverter
import sgtmelon.scriptum.data.room.converter.type.StringConverter
import sgtmelon.scriptum.domain.model.data.DbData.Rank
import sgtmelon.scriptum.domain.model.data.DbData.Rank.Default
import sgtmelon.scriptum.presentation.adapter.RankAdapter

/**
 * Model for store short information about rank, use in [RankAdapter].
 */
@TypeConverters(BoolConverter::class, StringConverter::class)
data class RankItem(
    @ColumnInfo(name = Rank.ID) val id: Long,
    @ColumnInfo(name = Rank.NOTE_ID) val noteId: MutableList<Long> = Default.NOTE_ID,
    @ColumnInfo(name = Rank.POSITION) var position: Int = Default.POSITION,
    @ColumnInfo(name = Rank.NAME) var name: String,
    @ColumnInfo(name = Rank.VISIBLE) var isVisible: Boolean = Default.VISIBLE,
    var bindCount: Int = Default.BIND_COUNT,
    var notificationCount: Int = Default.NOTIFICATION_COUNT
) {

    fun switchVisible() = apply { isVisible = !isVisible }

    fun toJson(): String = JSONObject().apply {
        put(Rank.ID, id)
        put(Rank.NOTE_ID, JSONArray().apply { for (item in noteId) put(item) })
        put(Rank.POSITION, position)
        put(Rank.NAME, name)
        put(Rank.VISIBLE, isVisible)
        put(Rank.BIND_COUNT, bindCount)
        put(Rank.NOTIFICATION_COUNT, notificationCount)
    }.toString()

    companion object {
        operator fun get(data: String): RankItem? = try {
            JSONObject(data).let {
                val noteIdArray = it.getJSONArray(Rank.NOTE_ID)
                val noteId = mutableListOf<Long>()
                for (i in 0 until noteIdArray.length()) {
                    noteId.add(noteIdArray.getLong(i))
                }

                return@let RankItem(
                    it.getLong(Rank.ID),
                    noteId,
                    it.getInt(Rank.POSITION),
                    it.getString(Rank.NAME),
                    it.getBoolean(Rank.VISIBLE),
                    it.getInt(Rank.BIND_COUNT),
                    it.getInt(Rank.NOTIFICATION_COUNT)
                )
            }
        } catch (e: Throwable) {
            null
        }
    }
}